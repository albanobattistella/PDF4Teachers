package fr.clementgre.pdf4teachers.panel.MainScreen;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.document.render.display.PageRenderer;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.utils.StringUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ZoomOperator{
    
    private final Timeline timeline;
    private boolean isPlaying = false;
    
    private final Pane pane;
    
    public ScrollBar vScrollBar = new ScrollBar();
    public ScrollBar hScrollBar = new ScrollBar();
    
    private double aimTranslateY = 0;
    private double aimTranslateX = 0;
    private double aimScale = 0;
    
    public ZoomOperator(Pane pane, MainScreen mainScreen){
        
        this.pane = pane;
        
        this.timeline = new Timeline(60);
        timeline.setOnFinished((ActionEvent e) -> {
            new Thread(() -> {
                try{
                    Thread.sleep(200);
                }catch(InterruptedException ex){
                    ex.printStackTrace();
                }
                isPlaying = false;
                
            }).start();
        });
        
        vScrollBar.setOrientation(Orientation.VERTICAL);
        hScrollBar.setOrientation(Orientation.HORIZONTAL);
        vScrollBar.setVisible(false);
        hScrollBar.setVisible(false);
        vScrollBar.setMax(1);
        hScrollBar.setMax(1);
        
        vScrollBar.layoutXProperty().bind(mainScreen.widthProperty().subtract(vScrollBar.widthProperty()));
        hScrollBar.layoutYProperty().bind(mainScreen.heightProperty().subtract(hScrollBar.heightProperty()));
        vScrollBar.prefHeightProperty().bind(mainScreen.heightProperty());
        hScrollBar.prefWidthProperty().bind(Bindings.createDoubleBinding(this::getMainScreenWidth, mainScreen.widthProperty(), vScrollBar.visibleProperty()));
        
        mainScreen.getChildren().addAll(hScrollBar, vScrollBar);
        
        
        // Actualise la longeur des curseur de scroll lors du zoom
        pane.scaleXProperty().addListener((observable, oldValue, newValue) -> {
            hScrollBar.setVisibleAmount(getMainScreenWidth() / (pane.getWidth() * newValue.doubleValue()));
            vScrollBar.setVisibleAmount(getMainScreenHeight() / (pane.getHeight() * newValue.doubleValue()));
        });
        
        // Vérifie si pane peut rentrer entièrement dans MainScreen quand MainScreen est recardé.
        // Vérifie aussi si pane ne pourait plus rentrer dans MainScreen et vérifie les translations dans ce cas
        mainScreen.heightProperty().addListener((observable, oldValue, newValue) -> {
            double scrollableHeight = pane.getHeight() * pane.getScaleX() - (hScrollBar.isVisible() ? newValue.doubleValue() - hScrollBar.getHeight() : newValue.doubleValue());
            
            if(scrollableHeight <= 0){
                vScrollBar.setVisible(false);
                pane.setTranslateY(centerTranslationY());
                aimTranslateY = pane.getTranslateY();
            }else{
                if(!vScrollBar.isVisible()){
                    vScrollBar.setVisible(true);
                    vScrollBar.setValue(0.5);
                }else{
                    vScrollBar.setVisible(true);
                    double translateY = -vScrollBar.getValue() * scrollableHeight + getPaneShiftY();
                    if(translateY != pane.getTranslateY()){
                        pane.setTranslateY(translateY);
                        aimTranslateY = pane.getTranslateY();
                    }
                }
            }
            vScrollBar.setVisibleAmount(getMainScreenHeight() / (pane.getHeight() * pane.getScaleX()));
            
        });
        mainScreen.widthProperty().addListener((observable, oldValue, newValue) -> {
            double scrollableWidth = pane.getWidth() * pane.getScaleX() - (vScrollBar.isVisible() ? newValue.doubleValue() - vScrollBar.getWidth() : newValue.doubleValue());
            
            if(scrollableWidth <= 0){
                hScrollBar.setVisible(false);
                pane.setTranslateX(centerTranslationX());
                aimTranslateX = pane.getTranslateX();
            }else{
                if(!hScrollBar.isVisible()){
                    hScrollBar.setVisible(true);
                    hScrollBar.setValue(0.5);
                }else{
                    hScrollBar.setVisible(true);
                    double translateX = -hScrollBar.getValue() * scrollableWidth + getPaneShiftX();
                    if(translateX != pane.getTranslateX()){
                        pane.setTranslateX(translateX);
                        aimTranslateX = pane.getTranslateX();
                    }
                }
            }
            hScrollBar.setVisibleAmount(getMainScreenWidth() / (pane.getWidth() * pane.getScaleX()));
            
        });
        
        // Modifie translateY lorsque la valeur de la scrollBar est modifié.
        vScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            
            double translateY = -newValue.doubleValue() * getScrollableHeight() + getPaneShiftY();
            if(((int) translateY) != ((int) pane.getTranslateY())){
                pane.setTranslateY(translateY);
                aimTranslateY = pane.getTranslateY();
            }
        });
        // Modifie la valeur de la scrollBar lorsque translateY est modifié.
        pane.translateYProperty().addListener((observable, oldValue, newValue) -> {
            updateVScrollBar();
        });
        
        // Modifie translateX lorsque la valeur de la scrollBar est modifié.
        hScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            
            double translateX = -newValue.doubleValue() * getScrollableWidth() + getPaneShiftX();
            if(((int) translateX) != ((int) pane.getTranslateX())){
                pane.setTranslateX(translateX);
                aimTranslateX = pane.getTranslateX();
            }
        });
        // Modifie la valeur de la scrollBar lorsque translateX est modifié.
        pane.translateXProperty().addListener((observable, oldValue, newValue) -> {
            updateHScrollBar();
        });
        
        pane.heightProperty().addListener((observable, oldValue, newValue) -> {
            updatePaneHeight(vScrollBar.getValue(), hScrollBar.getValue());
        });
        
        
    }
    
    public void updateVScrollBar(){
        if(getScrollableHeight() <= 0){
            vScrollBar.setVisible(false);
            pane.setTranslateY(centerTranslationY());
            aimTranslateY = pane.getTranslateY();
        }else{
            vScrollBar.setVisible(true);
            double vValue = (-pane.getTranslateY() + getPaneShiftY()) / getScrollableHeight();
            if(vValue != vScrollBar.getValue()){
                vScrollBar.setValue(vValue);
            }
        }
    }
    public void updateHScrollBar(){
        if(getScrollableWidth() <= 0){
            hScrollBar.setVisible(false);
            pane.setTranslateX(centerTranslationX());
            aimTranslateX = pane.getTranslateX();
        }else{
            hScrollBar.setVisible(true);
            double hValue = (-pane.getTranslateX() + getPaneShiftX()) / getScrollableWidth();
            if(hValue != hScrollBar.getValue()){
                hScrollBar.setValue(hValue);
            }
        }
    }
    
    public void updatePaneHeight(double newVValue, double newHValue){
        aimScale = pane.getScaleX();
        
        // Définis sur 1 pour actualiser avant de donner la vraie valeur
        vScrollBar.setValue(1);
        hScrollBar.setValue(1);
        
        // Update le système
        // Copie du code du listener de translateYProperty et de translateXProperty
        if(getScrollableHeight() <= 0){
            vScrollBar.setVisible(false);
            pane.setTranslateY(centerTranslationY());
            aimTranslateY = pane.getTranslateY();
        }else{
            vScrollBar.setVisible(true);
            double vValue = (-pane.getHeight() + getPaneShiftY()) / getScrollableHeight();
            if(vValue != vScrollBar.getValue()){
                vScrollBar.setValue(vValue);
            }
        }
        if(getScrollableWidth() <= 0){
            hScrollBar.setVisible(false);
            pane.setTranslateX(centerTranslationX());
            aimTranslateX = pane.getTranslateX();
        }else{
            hScrollBar.setVisible(true);
            double hValue = (-pane.getHeight() + getPaneShiftX()) / getScrollableWidth();
            if(hValue != hScrollBar.getValue()){
                hScrollBar.setValue(hValue);
            }
        }
        
        // Repasse les bonnes valeurs
        vScrollBar.setValue(newVValue);
        hScrollBar.setValue(newHValue);
        
        vScrollBar.setVisibleAmount(getMainScreenHeight() / (pane.getHeight() * pane.getScaleX()));
        hScrollBar.setVisibleAmount(getMainScreenWidth() / (pane.getWidth() * pane.getScaleX()));
    }
    
    // x and y should be relative to Scene because MainScreen is bigger than the visible part (e.getX()/Y couldn't work)
    public void zoom(double factor, double x, double y, boolean trackpad){
    
        // determine offset that we will have to move the node
        // Since we are relative to Scene, we have to apply the current Scale transformation
        Bounds bounds = pane.localToScene(pane.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX())) / Main.settings.zoom.getValue();
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY())) / Main.settings.zoom.getValue();
    
        double scale = Math.min(5, Math.max(aimScale * factor, 0.05));
        double f = (scale / pane.getScaleX()) - 1;
        
        zoom(doRemoveZoomAnimations(factor, trackpad, false), scale, dx, dy, f);
        
    }
    public void zoomFactor(double factor, boolean removeTransitions, boolean trackpad){
        zoom(Math.min(5, Math.max(aimScale * factor, 0.05)), doRemoveZoomAnimations(factor, trackpad, removeTransitions));
    }
    public void zoom(double scale, boolean removeTransitions){
        if(scale == pane.getScaleX()) return;
        
        // Get bounds relative to scene
        Bounds bounds = pane.localToScene(pane.getBoundsInLocal());
        // Find the middle coordinates of MainScreen relative to scene (To zoom center);
        Bounds mainScreenBounds = MainWindow.mainScreen.localToScene(MainWindow.mainScreen.getLayoutBounds());
        double mainScreenMiddleXInScene = (mainScreenBounds.getMinX() + mainScreenBounds.getWidth()/2);
        double mainScreenMiddleYInScene = (mainScreenBounds.getMinY() + mainScreenBounds.getHeight()/2);
    
        // determine offset that we will have to move the node
        // Since we are relative to Scene, we have to apply the current Scale transformation
        double dx = (mainScreenMiddleXInScene - (bounds.getWidth() / 2 + bounds.getMinX())) / Main.settings.zoom.getValue();
        double dy = (mainScreenMiddleYInScene - (bounds.getHeight() / 2 + bounds.getMinY())) / Main.settings.zoom.getValue();
    
        double f = (scale / pane.getScaleX()) - 1;
        
        zoom(removeTransitions, scale, dx, dy, f);
        
    }
    public void zoom(boolean removeTransition, double scale, double horizontal, double vertical, double translateFactor){
        
        if(!isPlaying){
            aimTranslateY = pane.getTranslateY();
            aimTranslateX = pane.getTranslateX();
            aimScale = pane.getScaleX();
        }
    
        
        double newTranslateX;
        double newTranslateY;
        
        // Donnés pour le traitement de juste après
        final double paneShiftX = (pane.getWidth() * scale - pane.getWidth()) / 2;
        final double paneShiftY = (pane.getHeight() * scale - pane.getHeight()) / 2;
        final double scrollableWidth = pane.getWidth() * scale - getMainScreenWidth();
        final double scrollableHeight = pane.getHeight() * scale - getMainScreenHeight();
        
        
        // Vérifie si pane peut rentrer entièrement dans MainScreen ? centre pane : vérifie les translations
        // X
        if(scrollableWidth <= 0){
            // Centre pane dans MainScreen sur l'axe X
            hScrollBar.setVisible(false);
            newTranslateX = centerTranslationX();
        }else{
            // Vérifie les limites des translations
            hScrollBar.setVisible(true);
            newTranslateX = StringUtils.clamp(pane.getTranslateX() - translateFactor * horizontal,
                    paneShiftX-scrollableWidth, paneShiftX);
        }
        // Y
        if(scrollableHeight <= 0){
            // Centre pane dans MainScreen sur l'axe Y
            vScrollBar.setVisible(false);
            newTranslateY = centerTranslationY();
        }else{
            // Vérifie les limites des translations
            vScrollBar.setVisible(true);
            newTranslateY = StringUtils.clamp(pane.getTranslateY() - translateFactor * vertical,
                    paneShiftY - scrollableHeight, paneShiftY);
            
        }
    
        aimTranslateY = newTranslateY;
        aimTranslateX = newTranslateX;
        aimScale = scale;
        
        if(!removeTransition && Main.settings.animations.getValue()){
            
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.translateXProperty(), newTranslateX)),
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.translateYProperty(), newTranslateY)),
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.scaleXProperty(), scale)),
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.scaleYProperty(), scale))
            );
            timeline.stop();
            isPlaying = true;
            timeline.play();
            timeline.setOnFinished((e) -> {
                pane.setScaleX(scale);
                pane.setScaleY(scale);
                pane.setTranslateX(newTranslateX);
                pane.setTranslateY(newTranslateY);
                updateVScrollBar();
                updateHScrollBar();
                timeline.setOnFinished(null);
            });
        }else{
            pane.setTranslateY(newTranslateY);
            pane.setTranslateX(newTranslateX);
            pane.setScaleY(scale);
            pane.setScaleX(scale);
            updateVScrollBar();
            updateHScrollBar();
        }
        
    }
    
    public void fitWidth(boolean removeTransition){
        
        double pageWidth = PageRenderer.PAGE_WIDTH + 2*PageRenderer.PAGE_HORIZONTAL_MARGIN;
        double availableWidth = (MainWindow.mainScreen.getWidth() - 40);
        
        double targetScale = (availableWidth / pageWidth);
        
        zoom(targetScale, removeTransition);
    }
    public void overviewWidth(boolean removeTransition){
        zoom(.4, removeTransition);
    }
    
    // V SCROLL
    
    public void scrollDown(int factor, boolean removeTransition, boolean trackpad){
        scroll(factor, removeTransition, trackpad);
    }
    public void scrollUp(int factor, boolean removeTransition, boolean trackpad){
        scroll(-factor, removeTransition, trackpad);
    }
    
    public void scroll(int factor, boolean removeTransition, boolean trackpad){
        if(!isPlaying){
            aimTranslateY = pane.getTranslateY();
            aimTranslateX = pane.getTranslateX();
            aimScale = pane.getScaleX();
        }
        
        double newTranslateY = aimTranslateY - factor;
        if(newTranslateY - getPaneShiftY() > 0) newTranslateY = getPaneShiftY();
        if(newTranslateY - getPaneShiftY() < -getScrollableHeight())
            newTranslateY = -getScrollableHeight() + getPaneShiftY();
        
        aimTranslateY = newTranslateY;
        
        if(!doRemoveScrollAnimations(factor, trackpad, removeTransition)){
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.translateYProperty(), newTranslateY))
            );
            timeline.stop();
            isPlaying = true;
            timeline.play();
        }else{
            pane.setTranslateY(newTranslateY);
        }
    }
    
    // H SCROLL
    
    public void scrollRight(int factor, boolean removeTransition, boolean trackpad){
        scrollHorizontally(factor, removeTransition, trackpad);
    }
    public void scrollLeft(int factor, boolean removeTransition, boolean trackpad){
        scrollHorizontally(-factor, removeTransition, trackpad);
    }
    public void scrollHorizontally(int factor, boolean removeTransition, boolean trackpad){
        if(!isPlaying){
            aimTranslateY = pane.getTranslateY();
            aimTranslateX = pane.getTranslateX();
            aimScale = pane.getScaleX();
        }
        
        double newTranslateX = aimTranslateX - factor;
        if(newTranslateX - getPaneShiftX() > 0) newTranslateX = getPaneShiftX();
        if(newTranslateX - getPaneShiftX() < -getScrollableWidth())
            newTranslateX = -getScrollableWidth() + getPaneShiftX();
        
        aimTranslateX = newTranslateX;
        
        if(!doRemoveScrollAnimations(factor, trackpad, removeTransition)){
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().addAll(
                    new KeyFrame(Duration.millis(200), new KeyValue(pane.translateXProperty(), newTranslateX))
            );
            timeline.stop();
            isPlaying = true;
            timeline.play();
        }else{
            pane.setTranslateX(newTranslateX);
        }
    }
    
    private boolean doRemoveScrollAnimations(double factor, boolean trackpad, boolean removeTransition){
        return !Main.settings.animations.getValue() || Math.abs(factor) < 25 || removeTransition || (trackpad && Main.settings.trackpadMode.getValue());
    }
    private boolean doRemoveZoomAnimations(double factor, boolean trackpad, boolean removeTransition){
        return !Main.settings.animations.getValue() || Math.abs(factor) < 0.05 || removeTransition || (trackpad && Main.settings.trackpadMode.getValue());
    }
    
    // Renvoie le décalage entre les vrais coordonés de pane et entre les coordonés de sa partie visible.
    // Lors d'un zoom le shift est négatif | Lors d'un dé-zoom il est positif
    public double getPaneShiftY(){
        return (pane.getHeight() * pane.getScaleX() - pane.getHeight()) / 2;
    }
    
    public double getPaneShiftX(){
        return (pane.getWidth() * pane.getScaleX() - pane.getWidth()) / 2;
    }
    
    // Renvoie les dimensions de MainScreen sans compter les scrolls bars, si elles sonts visibles.
    // Il est conseillé d'utiliser ces méthodes pour récupérer les dimensions de MainScreen.
    public double getMainScreenWidth(){
        if(MainWindow.mainScreen == null) return 0;
        if(!vScrollBar.isVisible()) return MainWindow.mainScreen.getWidth();
        else return MainWindow.mainScreen.getWidth() - vScrollBar.getWidth();
    }
    
    public double getMainScreenHeight(){
        if(MainWindow.mainScreen == null) return 0;
        if(!hScrollBar.isVisible()) return MainWindow.mainScreen.getHeight();
        else return MainWindow.mainScreen.getHeight() - hScrollBar.getHeight();
    }
    
    // Renvoie les dimensions de la partie visible de pane (multiplication par sa Scale pour avoir sa partie visible)
    // en enlevant les dimensions de MainScreen, on obtient donc la hauteur scrollable.
    public double getScrollableHeight(){
        return pane.getHeight() * pane.getScaleX() - getMainScreenHeight();
    }
    
    public double getScrollableWidth(){
        return pane.getWidth() * pane.getScaleX() - getMainScreenWidth();
    }
    
    // Renvoie la translation qui centre Pane sur MainScreen
    public double centerTranslationY(){
        return (getMainScreenHeight() - pane.getHeight()) / 2;
    }
    
    public double centerTranslationX(){
        return (getMainScreenWidth() - pane.getWidth()) / 2;
    }
}