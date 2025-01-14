package fr.clementgre.pdf4teachers.document.editions.elements;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.document.editions.Edition;
import fr.clementgre.pdf4teachers.document.render.display.PageRenderer;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.utils.PlatformUtils;
import fr.clementgre.pdf4teachers.utils.StringUtils;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Element extends Region{
    
    public static BorderStroke STROKE_DEFAULT = new BorderStroke(Color.color(0 / 255.0, 100 / 255.0, 255 / 255.0),
            BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, new BorderWidths(1.5));
    
    // Size for A4 - 200dpi (Static)
    public static float GRID_WIDTH = 165400;
    public static float GRID_HEIGHT = 233900;
    public static float GRID_RATIO = GRID_WIDTH / GRID_HEIGHT;
    
    // ATTRIBUTES
    
    protected IntegerProperty realX = new SimpleIntegerProperty();
    protected IntegerProperty realY = new SimpleIntegerProperty();
    
    protected int pageNumber;
    protected double shiftX = 0;
    protected double shiftY = 0;
    protected boolean wasInEditPagesModeWhenMousePressed = false;
    
    public ContextMenu menu = new ContextMenu();
    
    public Element(int x, int y, int pageNumber){
        this.pageNumber = pageNumber;
        this.realX.set(x);
        this.realY.set(y);
    }
    
    // SETUP / EVENTS CALLBACK
    
    private final ChangeListener<Element> mainScreenSelectedListener = (observable, oldElement, newElement) -> {
        if(oldElement == this && newElement != this) onDeSelected();
        else if(oldElement != this && newElement == this) onSelected();
    };
    
    protected void setupGeneral(boolean setupEvents, Node... components){
        getChildren().addAll(components);
        
        layoutXProperty().bind(getPage().widthProperty().multiply(realX.divide(Element.GRID_WIDTH)));
        layoutYProperty().bind(getPage().heightProperty().multiply(realY.divide(Element.GRID_HEIGHT)));
        
        checkLocation(false);
        setCursor(PlatformUtils.CURSOR_MOVE);
        
        //////////////////////////// EVENTS ///////////////////////////////////
    
        MainWindow.mainScreen.selectedProperty().addListener(mainScreenSelectedListener);
        if(setupEvents){
    
            AtomicBoolean lastClickSelected = new AtomicBoolean(false);
            setOnMousePressed(e -> {
                wasInEditPagesModeWhenMousePressed = PageRenderer.isEditPagesMode();
                if(wasInEditPagesModeWhenMousePressed) return;
                e.consume();
    
                if(e.getClickCount() == 1){
                    lastClickSelected.set(MainWindow.mainScreen.getSelected() == this);
    
                    shiftX = (int) e.getX();
                    shiftY = (int) e.getY();
                    menu.hide();
                    select();
    
                    if(e.getButton() == MouseButton.SECONDARY){
                        menu.show(getPage(), e.getScreenX(), e.getScreenY());
                    }
                    
                }else if(e.getClickCount() == 2 && lastClickSelected.get()){
                    onDoubleClickAfterSelected();
                }
            });
            setOnMouseClicked(e -> {
                if(PageRenderer.isEditPagesMode()) return;
                e.consume();
                requestFocus();
                if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2){
                    onDoubleCLick();
                }
            });
            setOnMouseDragged(e -> {
                if(wasInEditPagesModeWhenMousePressed) return;
                double itemX = getLayoutX() + e.getX() - shiftX;
                double itemY = getLayoutY() + e.getY() - shiftY;
                checkLocation(itemX, itemY, true);
            });
            setOnMouseReleased(e -> {
                if(wasInEditPagesModeWhenMousePressed) return;
                Edition.setUnsave("ElementMouseRelease");
                double itemX = getLayoutX() + e.getX() - shiftX;
                double itemY = getLayoutY() + e.getY() - shiftY;

                checkLocation(itemX, itemY, true);

                PageRenderer newPage = MainWindow.mainScreen.document.getPreciseMouseCurrentPage();
                if(newPage != null){
                    if(newPage.getPage() != getPageNumber()){
                        MainWindow.mainScreen.setSelected(null);

                        switchPage(newPage.getPage());
                        itemY = newPage.getPreciseMouseY() - shiftY;
                        checkLocation(itemX, itemY, true);

                        MainWindow.mainScreen.setSelected(this);
                    }
                }
                checkLocation(false);
                onMouseRelease();
            });
        }
        
        setOnMouseClicked(e -> {
            if(PageRenderer.isEditPagesMode()) return;
            e.consume();
        });
        
        /////////////////////////////////////////////////////////////////////////
        
        setupBindings();
        setupMenu();
    }
    
    protected abstract void setupBindings();
    
    protected abstract void setupMenu();
    
    protected abstract void onMouseRelease();
    protected abstract void onDoubleCLick();
    
    protected void onSelected(){
        setBorder(new Border(STROKE_DEFAULT));
    }
    protected void onDeSelected(){
        setBorder(null);
        menu.hide();
    }
    
    // CHECKS
    
    public void checkLocation(boolean allowSwitchPage){
        checkLocation(getLayoutX(), getLayoutY(), allowSwitchPage);
    }
    public void checkLocation(double itemX, double itemY, boolean allowSwitchPage){
        checkLocation(itemX, itemY, getWidth(), getHeight(), allowSwitchPage);
    }
    public void checkLocation(double itemX, double itemY, double width, double height, boolean allowSwitchPage){
    
        if(getPageNumber() == 0 || !allowSwitchPage) if(itemY < 0) itemY = 0;
        if(getPageNumber() == MainWindow.mainScreen.document.totalPages - 1 || !allowSwitchPage)
            if(itemY > getPage().getHeight() - height) itemY = getPage().getHeight() - height;

        if(itemX < 0) itemX = 0;
        if(itemX > getPage().getWidth() - width) itemX = getPage().getWidth() - width;
    
        realX.set(getPage().toGridX(itemX));
        realY.set(getPage().toGridY(itemY));

        if(this instanceof GraphicElement){

            if(getHeight() != height){
                int value = getPage().toGridY(height);
                ((GraphicElement) this).setRealHeight(StringUtils.clamp(value, 0, (int) Element.GRID_HEIGHT));
            }

            if(getWidth() != width){
                int value = getPage().toGridX(width);
                ((GraphicElement) this).setRealWidth(StringUtils.clamp(value, 0, (int) Element.GRID_WIDTH));
            }
        }
    }
    
    // ACTIONS
    
    public abstract void select();
    
    public abstract void onDoubleClickAfterSelected();
    
    protected void selectPartial(){
        MainWindow.mainScreen.setSelected(this);
        toFront();
        getPage().toFront();
    }
    
    public abstract void addedToDocument(boolean markAsUnsave);
    
    public void removedFromDocument(boolean markAsUnsave){
        // Useless because bound values are stored in a weak reference.
        // But if this element leaks, the PageRenderer will leak too if we do not unbind this.
        layoutXProperty().unbind();
        layoutYProperty().unbind();
        MainWindow.mainScreen.selectedProperty().removeListener(mainScreenSelectedListener);
    }
    
    public void delete(boolean markAsUnsave){
        if(getPage() != null){
            if(equals(MainWindow.mainScreen.getSelected())) MainWindow.mainScreen.setSelected(null);
            getPage().removeElement(this, markAsUnsave);
        }
    }
    
    public void switchPage(int page){
        getPage().switchElementPage(this, MainWindow.mainScreen.document.getPage(page));
        layoutXProperty().bind(getPage().widthProperty().multiply(realX.divide(Element.GRID_WIDTH)));
        layoutYProperty().bind(getPage().heightProperty().multiply(realY.divide(Element.GRID_HEIGHT)));
    }
    
    public void centerOnCoordinatesY(){
        setRealY(getRealY() - getRealHeight() / 2);
    }
    
    // READER AND WRITERS
    
    public abstract LinkedHashMap<Object, Object> getYAMLData();
    
    protected LinkedHashMap<Object, Object> getYAMLPartialData(){
        LinkedHashMap<Object, Object> data = new LinkedHashMap<>();
        data.put("x", getRealX());
        data.put("y", getRealY());
        return data;
    }
    
    // GETTERS AND SETTERS
    
    public abstract float getAlwaysHeight();
    
    public int getRealHeight(){
        return getPage().toGridY(getAlwaysHeight());
    }
    
    // COORDINATES GETTERS AND SETTERS
    
    public int getRealX(){
        return realX.get();
    }
    
    public IntegerProperty realXProperty(){
        return realX;
    }
    
    public void setRealX(int x){
        this.realX.set(x);
    }
    
    public int getRealY(){
        return realY.get();
    }
    
    public IntegerProperty realYProperty(){
        return realY;
    }
    
    public void setRealY(int y){
        this.realY.set(y);
    }
    
    // PAGE GETTERS AND SETTERS
    
    public PageRenderer getPage(){
        if(MainWindow.mainScreen.document == null) return null;
        if(MainWindow.mainScreen.document.getPagesNumber() > pageNumber){
            return MainWindow.mainScreen.document.getPage(pageNumber);
        }
        return null;
    }
    
    public int getPageNumber(){
        return pageNumber;
    }
    public void setPage(PageRenderer page){
        this.pageNumber = page.getPage();
    }
    public void setPage(int pageNumber){
        this.pageNumber = pageNumber;
    }
    
    // TRANSFORMATIONS
    
    public abstract Element clone();
    
    public void cloneOnDocument(){
        Element element = clone();
        element.setRealX((int) (getRealX() + (10 / getPage().getWidth() * GRID_WIDTH)));
        element.setRealY((int) (getRealY() + (10 / getPage().getHeight() * GRID_HEIGHT)));
        element.getPage().addElement(element, true);
        element.select();
    }
}