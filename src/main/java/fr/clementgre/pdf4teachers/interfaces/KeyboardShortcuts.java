package fr.clementgre.pdf4teachers.interfaces;


import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.components.KeyableHBox;
import fr.clementgre.pdf4teachers.document.editions.elements.Element;
import fr.clementgre.pdf4teachers.document.editions.elements.GradeElement;
import fr.clementgre.pdf4teachers.document.editions.elements.TextElement;
import fr.clementgre.pdf4teachers.panel.sidebar.SideBar;
import fr.clementgre.pdf4teachers.panel.sidebar.grades.GradeTreeItem;
import fr.clementgre.pdf4teachers.panel.sidebar.grades.GradeTreeView;
import fr.clementgre.pdf4teachers.panel.sidebar.texts.TextListItem;
import fr.clementgre.pdf4teachers.panel.sidebar.texts.TextTreeItem;
import fr.clementgre.pdf4teachers.panel.sidebar.texts.TreeViewSections.TextTreeSection;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.input.*;

import java.util.ArrayList;

public class KeyboardShortcuts{
    
    public KeyboardShortcuts(Scene main){
        
        main.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
    
            if(e.isShortcutDown()){
                String keyName = e.getText();
                if(!e.isAltDown()){
                    
                    try{
                        int i = Integer.parseInt(e.getCode().getChar());
                        numberPressed(i, true, false);
                        return;
                    }catch(NumberFormatException ignored){}
                    try{
                        int i = Integer.parseInt(keyName);
                        numberPressed(i, true, false);
                        return;
                    }catch(NumberFormatException ignored){}
                    
                    if(e.getCode() == KeyCode.T){
                        if(!MainWindow.mainScreen.hasDocument(false)) return;
                        
                        SideBar.selectTab(MainWindow.textTab);
                        MainWindow.textTab.newBtn.fire();
                        Element selected = MainWindow.mainScreen.getSelected();
                        if(selected != null){
                            if(selected instanceof TextElement){
                                selected.setRealX(selected.getPage().getNewElementXOnGrid(false));
                            }
                        }
                        return;
                        
                    }else if(e.getCode() == KeyCode.N){
                        if(!MainWindow.mainScreen.hasDocument(false)) return;
                        
                        SideBar.selectTab(MainWindow.gradeTab);
                        MainWindow.gradeTab.treeView.getSelectionModel().select(GradeTreeView.getNextLogicGradeNonNull());
                        return;
                        
                    }else if(e.getCode() == KeyCode.G){
                        if(!MainWindow.mainScreen.hasDocument(false)) return;
                        if(!MainWindow.gradeTab.isSelected()) return;
                        
                        GradeTreeItem item = (GradeTreeItem) MainWindow.gradeTab.treeView.getSelectionModel().getSelectedItem();
                        if(item == null) return;
                        if(item.isRoot()){
                            GradeElement element = MainWindow.gradeTab.newGradeElementAuto(item);
                            element.select();
                        }else{
                            GradeElement element = MainWindow.gradeTab.newGradeElementAuto(((GradeTreeItem) item.getParent()));
                            element.select();
                        }
                        // Update total (Fix the bug when a total is predefined (with no children))
                        item.makeSum(false);
                        return;
                        
                    }else if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.KP_UP){
                        e.consume();
                        MainWindow.mainScreen.zoomMore();
                        return;
                    }else if(e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.KP_DOWN){
                        e.consume();
                        if(MainWindow.mainScreen.hasDocument(false)) MainWindow.mainScreen.zoomOperator.fitWidth(false);
                        return;
                    }
                    
                }else{ // SHORTCUT + ALT
                    
                    try{
                        int i = Integer.parseInt(e.getCode().getChar());
                        numberPressed(i, true, true);
                        return;
                    }catch(NumberFormatException ignored){}
                    try{
                        int i = Integer.parseInt(keyName) ;
                        numberPressed(i, true, true);
                        return;
                    }catch(NumberFormatException ignored){}
    
                    if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.KP_UP){
                        e.consume();
                        MainWindow.mainScreen.pageUp();
                        return;
                    }else if(e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.KP_DOWN){
                        e.consume();
                        MainWindow.mainScreen.pageDown();
                        return;
                    }else if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.KP_LEFT){
                        e.consume();
                        MainWindow.filesTab.loadPreviousFile();
                        return;
                    }else if(e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.KP_RIGHT){
                        e.consume();
                        MainWindow.filesTab.loadNextFile();
                        return;
                    }
                }
                
                
            }else{ // NO SHORTCUT PRESSED
                
                if(e.getCode() == KeyCode.BEGIN || e.getCode() == KeyCode.HOME){ // NAVIGATE IN DOCUMENT SHORTCUTS
                    e.consume();
                    MainWindow.mainScreen.navigateBegin();
                    return;
                }else if(e.getCode() == KeyCode.END){
                    e.consume();
                    MainWindow.mainScreen.navigateEnd();
                    return;
                }else if(e.getCode() == KeyCode.PAGE_UP){
                    e.consume();
                    MainWindow.mainScreen.pageUp();
                    return;
                }else if(e.getCode() == KeyCode.PAGE_DOWN){
                    e.consume();
                    MainWindow.mainScreen.pageDown();
                    return;
                }
            }
    
            if(Main.window.getScene().getFocusOwner() instanceof SideBar
                    || Main.window.getScene().getFocusOwner() instanceof Slider
                    || Main.window.getScene().getFocusOwner() instanceof Button
                    || (
                    !(Main.window.getScene().getFocusOwner() instanceof Control)
                            && !(Main.window.getScene().getFocusOwner() instanceof Element)
                            && !(Main.window.getScene().getFocusOwner() instanceof KeyableHBox)
            )){
                MainWindow.keyboardShortcuts.reportKeyPressedForMultipleUsesKeys(e);
            }
            
        });
        
        main.setOnKeyPressed(this::reportKeyPressedForMultipleUsesKeys);
        
    }
    
    public void reportKeyPressedForMultipleUsesKeys(KeyEvent e){
        if(!e.isShortcutDown()){ // NO SHORTCUT PRESSED
    
            if(e.getCode() == KeyCode.TAB){
                
                if(!MainWindow.textTab.isSelected()){
                    MainWindow.textTab.select();
                    e.consume();
                }else if(!MainWindow.paintTab.isSelected()){
                    MainWindow.paintTab.select();
                    e.consume();
                }
        
            }else if(e.getCode() == KeyCode.UP || e.getCode() == KeyCode.KP_UP){
                e.consume();
                MainWindow.mainScreen.navigateUp();
            }else if(e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.KP_DOWN){
                e.consume();
                MainWindow.mainScreen.navigateDown();
            }else if(e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.KP_LEFT){
                e.consume();
                MainWindow.mainScreen.navigateLeft();
            }else if(e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.KP_RIGHT){
                e.consume();
                MainWindow.mainScreen.navigateRight();
            }
        }else{ // SHORTCUT PRESSED
            
            if(e.getCode() == KeyCode.V){
                e.consume();
                
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                String string = clipboard.getString();
                if(string == null) string = clipboard.getRtf();
                if(string == null) string = clipboard.getUrl();
                if(string == null) string = clipboard.getHtml();
                if(string != null){
                    MainWindow.mainScreen.pasteText(string);
                }
            }
            
        }
    }
    
    private void numberPressed(int i, boolean shortcut, boolean alt){
    
        if(shortcut){
            if(alt){
                if(!MainWindow.mainScreen.hasDocument(false)) return;
                
                if(i == 0){ // save
                    MainWindow.textTab.treeView.favoritesSection.listsManager.saveListBtn.fire();
                    return;
                }
                if(i <= TextTreeSection.lists.size()){ // load
                    int k = 1;
                    for(ArrayList<TextListItem> list : TextTreeSection.lists.values()){
                        if(k == i){
                            SideBar.selectTab(MainWindow.textTab);
                            MainWindow.textTab.treeView.favoritesSection.listsManager.loadList(list, true);
                            return;
                        }
                        k++;
                    }
                }
                
            }else{ // Shortcut without Alt
                if(!MainWindow.mainScreen.hasDocument(false)) return;
                
                if(MainWindow.textTab.treeView.favoritesSection.sortToggleBtn.isSelected()) i++;
                if(i <= MainWindow.textTab.treeView.favoritesSection.getChildren().size() && i != 0){
                    ((TextTreeItem) MainWindow.textTab.treeView.favoritesSection.getChildren().get(i - 1)).addToDocument(false, false);
                    MainWindow.textTab.selectItem();
                }
            }
        }
       
    }
    
}
