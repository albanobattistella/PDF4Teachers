package fr.clementgre.pdf4teachers.document;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.document.editions.Edition;
import fr.clementgre.pdf4teachers.document.editions.elements.Element;
import fr.clementgre.pdf4teachers.document.editions.elements.GradeElement;
import fr.clementgre.pdf4teachers.document.editions.elements.GraphicElement;
import fr.clementgre.pdf4teachers.document.editions.elements.TextElement;
import fr.clementgre.pdf4teachers.document.render.display.PDFPagesRender;
import fr.clementgre.pdf4teachers.document.render.display.PageRenderer;
import fr.clementgre.pdf4teachers.document.render.display.PageStatus;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.utils.PlatformUtils;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.ButtonPosition;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.CustomAlert;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Document{
    
    private final File file;
    public Edition edition;
    private final ArrayList<PageRenderer> pages = new ArrayList<>();
    
    private int currentPage = -1;
    public int totalPages;
    
    public PDFPagesRender pdfPagesRender;

    private boolean documentSaverNeedToStop = false;
    public void stopDocumentSaver(){
        documentSaverNeedToStop = true;
    }
    private final Thread documentSaver = new Thread(() -> {
        documentSaverNeedToStop = false;
        while(!documentSaverNeedToStop){
            if(Main.settings.regularSave.getValue() != -1){
    
                PlatformUtils.sleepThreadMinutes(Main.settings.regularSave.getValue());
                if(!documentSaverNeedToStop && !Edition.isSave()){
                    Platform.runLater(() -> edition.save());
                }
                
            }else{
                // Should not be too big because it keeps the document as a CG Root Object.
                PlatformUtils.sleepThreadSeconds(30);
            }
        }
    }, "Document AutoSaver");
    
    public Document(File file) throws IOException{
        this.file = file;
        
        pdfPagesRender = new PDFPagesRender(file);
        totalPages = pdfPagesRender.getNumberOfPages();
    }
    
    public void showPages(){
        
        for(int i = 0; i < totalPages; i++){
            PageRenderer page = new PageRenderer(i);
            MainWindow.mainScreen.addPage(page);
            pages.add(page);
        }
        pages.get(0).updatePosition(PageRenderer.PAGE_VERTICAL_MARGIN, false);
        updateShowsStatus();
    }
    
    public void updateShowsStatus(){
        for(PageRenderer page : pages){
            page.updateShowStatus();
        }
    }
    
    public void updateZoom(){
        for(PageRenderer page : pages){
            page.updateZoom();
        }
    }
    
    public void updateBackgrounds(){
        for(PageRenderer page : pages){
            page.setStatus(PageStatus.HIDE);
        }
        updateShowsStatus();
    }
    
    public void loadEdition(){
        this.edition = new Edition(file, this);
        if(!documentSaver.isAlive()) documentSaver.start();
    }
    public double getCurrentScrollValue(){
        return MainWindow.mainScreen.zoomOperator.vScrollBar.getValue();
    }
    public void setCurrentScrollValue(double value){
        MainWindow.mainScreen.zoomOperator.vScrollBar.setValue(value);
    }
    
    public ArrayList<Element> getElements(){
        ArrayList<Element> elements = new ArrayList<>();
        for(PageRenderer page : pages){
            elements.addAll(page.getElements());
        }
        return elements;
    }
    // [1] : Elements
    // [2] : Texts
    // [3] : Grades
    // [4] : Graphics
    public int[] countElements(){
        ArrayList<Element> elements = getElements();
        int texts = (int) elements.stream().filter((e) -> e instanceof TextElement).count();
        int grades = (int) elements.stream().filter((e) -> e instanceof GradeElement).count();
        int graphics = (int) elements.stream().filter((e) -> e instanceof GraphicElement).count();
        return new int[]{elements.size(), texts, grades, graphics};
    }
    
    public void updateEdition(){
        MainWindow.mainScreen.setSelected(null);
        for(PageRenderer page : pages){
            page.clearElements();
        }
        MainWindow.textTab.treeView.onFileSection.updateElementsList();
        MainWindow.gradeTab.treeView.clearElements(false, true);
        this.edition.load();
    }
    
    public void close(){
        pdfPagesRender.close();
        for(int i = 0; i < totalPages; i++){
            if(pages.size() > i) pages.get(i).remove();
        }
        pages.clear();
    }
    
    public boolean save(){
        
        if(Edition.isSave()){
            edition.saveLastScrollValue();
            return true;
        }
        
        if(Main.settings.autoSave.getValue()){
            edition.save();
            return true;
            
        }
        
        CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION, TR.tr("dialog.unsavedEdit.title"), TR.tr("dialog.unsavedEdit.header"), TR.tr("dialog.unsavedEdit.details"));
        alert.addCancelButton(ButtonPosition.CLOSE);
        alert.addButton(TR.tr("actions.save"), ButtonPosition.DEFAULT);
        alert.addIgnoreButton(ButtonPosition.OTHER_RIGHT);
        
        ButtonType option = alert.getShowAndWait();
        if(option == null) return false; // Window close button (null)
        if(option.getButtonData().isDefaultButton()){ // Save button (Default)
            edition.save();
            return true;
        }else{
            edition.saveLastScrollValue();
            return !option.getButtonData().isCancelButton(); // Close button OR Ignore button
        }
        
    }
    
    public String getFileName(){
        return file.getName();
    }
    
    public File getFile(){
        return file;
    }
    
    public int getLastCursorOverPage(){
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
        MainWindow.footerBar.updateCurrentPage();
    }
    public int getPagesNumber(){
        return pages.size();
    }
    
    // PageRenderer getter
    
    
    public ArrayList<PageRenderer> getPages(){
        return pages;
    }
    public PageRenderer getPage(int page){
        return page < pages.size() ? pages.get(page) : null;
    }
    public PageRenderer getPageNonNull(int page){
        return page < pages.size() ? pages.get(page) : pages.get(0);
    }
    public WeakReference<PageRenderer> getPageWeakReference(int page){
        return new WeakReference<>(getPageNonNull(page));
    }
    
    public PageRenderer getLastCursorOverPageObject(){
        return (getLastCursorOverPage() != -1) ? pages.get(getLastCursorOverPage()) : pages.get(0);
    }
    public WeakReference<PageRenderer> getLastCursorOverPageWeakReference(){
        return (getLastCursorOverPage() != -1) ? getPageWeakReference(getLastCursorOverPage()) : getPageWeakReference(0);
    }
    
    public PageRenderer getPreciseMouseCurrentPage(){
        for(PageRenderer page : pages){
            double bottomY = page.getBottomY();
            if(MainWindow.mainScreen.mouseY < bottomY){
                return page;
            }
        }
        return null;
    }
    
    // Return null if there is no top page below the top of MainScreen
    public PageRenderer getFirstTopVisiblePage(){
        
        Bounds mainScreenBoundsInScene = MainWindow.mainScreen.localToScene(MainWindow.mainScreen.getLayoutBounds());
        
        int i = 0;
        int iMax = pages.size();
        for(PageRenderer page : pages){
            Bounds boundsInScene = MainWindow.mainScreen.pane.localToScene(page.getBoundsInParent());
            if(boundsInScene.getMinY() > mainScreenBoundsInScene.getMinY()){
                return page;
            }
            i++;
        }
        return null;
    }
    // Return null if there is no bottom page above the bottom of MainScreen
    public PageRenderer getFirstBottomVisiblePage(){
        
        Bounds mainScreenBoundsInScene = MainWindow.mainScreen.localToScene(MainWindow.mainScreen.getLayoutBounds());
        
        int iMax = pages.size();
        for(int i = 1; i <= iMax; i++){
            PageRenderer page = pages.get(iMax-i);
            
            Bounds boundsInScene = MainWindow.mainScreen.pane.localToScene(page.getBoundsInParent());
            if(boundsInScene.getMaxY() < mainScreenBoundsInScene.getMaxY()){
                return page;
            }
        }
        return null;
    }
}