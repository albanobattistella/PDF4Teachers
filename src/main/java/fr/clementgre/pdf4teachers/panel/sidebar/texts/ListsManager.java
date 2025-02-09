package fr.clementgre.pdf4teachers.panel.sidebar.texts;

import fr.clementgre.pdf4teachers.components.NoArrowMenuButton;
import fr.clementgre.pdf4teachers.components.menus.NodeMenuItem;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.panel.sidebar.texts.TreeViewSections.TextTreeSection;
import fr.clementgre.pdf4teachers.utils.panes.PaneUtils;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.*;
import fr.clementgre.pdf4teachers.utils.image.ImageUtils;
import fr.clementgre.pdf4teachers.utils.svg.SVGPathIcons;
import fr.clementgre.pdf4teachers.utils.style.StyleManager;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Map;

public class ListsManager{
    
    public final MenuButton loadListBtn = new NoArrowMenuButton();
    public final Button saveListBtn = new Button();
    
    private final TextTreeSection section;
    
    public ListsManager(TextTreeSection section){
        this.section = section;
        
        loadListBtn.setGraphic(SVGPathIcons.generateImage(SVGPathIcons.LIST, "black", 0, 18, 18, ImageUtils.defaultDarkColorAdjust));
        loadListBtn.setTooltip(PaneUtils.genWrappedToolTip(TR.tr("textTab.lists.show.tooltip")));
        saveListBtn.setGraphic(SVGPathIcons.generateImage(SVGPathIcons.SAVE, "black", 0, 18, 18, ImageUtils.defaultDarkColorAdjust));
        saveListBtn.setTooltip(PaneUtils.genWrappedToolTip(TR.tr("textTab.lists.save.tooltip")));
        
        PaneUtils.setPosition(loadListBtn, 0, 0, 30, 30, true);
        PaneUtils.setPosition(saveListBtn, 0, 0, 30, 30, true);
        
        updateGraphics();
        
        setupMenu();
//        loadListBtn.setOnMouseClicked(e -> {
//            menu.show(loadListBtn, e.getScreenX(), e.getScreenY());
//        });
        
        saveListBtn.setOnAction(event -> {
            TextInputAlert inputAlert = new TextInputAlert(TR.tr("textTab.lists.save.dialog.title"), TR.tr("textTab.lists.save.dialog.header"), TR.tr("textTab.lists.save.dialog.details"));
            inputAlert.setText(TR.tr("textTab.lists.defaultName"));
            inputAlert.addOKButton(ButtonPosition.DEFAULT);
            inputAlert.addCancelButton(ButtonPosition.CLOSE);
            
            if(inputAlert.getShowAndWaitIsDefaultButton() && !inputAlert.getText().isBlank()){
                if(TextTreeSection.lists.containsKey(inputAlert.getText())){
                    CustomAlert alert = new CustomAlert(Alert.AlertType.WARNING, TR.tr("textTab.lists.save.alreadyExistDialog.title"),
                            TR.tr("textTab.lists.save.alreadyExistDialog.header"));
                    
                    alert.addButton(TR.tr("dialog.actionError.rename"), ButtonPosition.CLOSE);
                    alert.addButton(TR.tr("dialog.actionError.overwrite"), ButtonPosition.DEFAULT);
                    
                    if(alert.getShowAndWaitIsCancelButton()){
                        saveListBtn.fire();
                    }else{
                        saveList(inputAlert.getText());
                    }
                }else saveList(inputAlert.getText());
            }
        });
    }
    
    public void updateGraphics(){
        loadListBtn.setStyle("-fx-background-color: " + StyleManager.getHexAccentColor() + ";");
        saveListBtn.setStyle("-fx-background-color: " + StyleManager.getHexAccentColor() + ";");
    }
    
    public static void setupMenus(){
        MainWindow.textTab.treeView.favoritesSection.listsManager.setupMenu();
        MainWindow.textTab.treeView.lastsSection.listsManager.setupMenu();
    }
    
    public void setupMenu(){
        loadListBtn.getItems().clear();
        //menu.setMinWidth(400);
        //menu.setPrefWidth(400);
        
        if(TextTreeSection.lists.size() >= 1){
            for(Map.Entry<String, ArrayList<TextListItem>> list : TextTreeSection.lists.entrySet()){
                NodeMenuItem menuItem = new NodeMenuItem(list.getKey(), false);
                loadListBtn.getItems().add(menuItem);
                menuItem.setOnAction(event -> {
                    
                    CustomAlert alert = new CustomAlert(Alert.AlertType.CONFIRMATION, TR.tr("textTab.lists.actionDialog.title"),
                            TR.tr("textTab.lists.actionDialog.header"), TR.tr("textTab.lists.actionDialog.details"));
                    
                    alert.addCancelButton(ButtonPosition.CLOSE);
                    ButtonType load =  alert.addButton(TR.tr("actions.load"), ButtonPosition.DEFAULT);
                    ButtonType loadReplace = alert.addButton(TR.tr("actions.clearAndLoad"), ButtonPosition.OTHER_LEFT);
                    ButtonType delete = alert.addButton(TR.tr("actions.delete"), ButtonPosition.OTHER_LEFT);
                    
                    ButtonType result = alert.getShowAndWait();
                    if(result == load) loadList(list.getValue(), false);
                    else if(result == loadReplace) loadList(list.getValue(), true);
                    else if(result == delete) deleteList(list.getKey());
                    
                });
            }
        }else{
            loadListBtn.getItems().add(new NodeMenuItem(TR.tr("textTab.lists.show.none"), false));
        }
    }
    
    public void loadList(ArrayList<TextListItem> items, boolean flush){
        if(flush) section.clearElements(true);
        for(TextListItem item : items) section.getChildren().add(item.toTextTreeItem(section.sectionType));
        section.sortManager.simulateCall();
    }
    
    public void saveList(String listName){
        TextTreeSection.lists.remove(listName);
        ArrayList<TextListItem> list = new ArrayList<>();
        for(Object item : section.getChildren()){
            if(item instanceof TextTreeItem){
                list.add(((TextTreeItem) item).toTextItem());
            }
        }
        if(list.size() == 0){
            new WrongAlert(Alert.AlertType.ERROR, TR.tr("textTab.lists.save.voidListDialog.title"),
                    TR.tr("textTab.lists.save.voidListDialog.header"), TR.tr("textTab.lists.save.voidListDialog.details"), false).showAndWait();
            return;
        }
        TextTreeSection.lists.put(listName, list);
        
        new OKAlert(TR.tr("textTab.lists.save.completedDialog.title"),
                TR.tr("textTab.lists.save.completedDialog.header"), TR.tr("textTab.lists.save.completedDialog.details")).showAndWait();
        ListsManager.setupMenus();
    }
    
    public void deleteList(String listName){
        TextTreeSection.lists.remove(listName);
        
        new OKAlert(TR.tr("textTab.lists.deleteCompletedDialog.title"),
                TR.tr("textTab.lists.deleteCompletedDialog.header", listName)).show();
        ListsManager.setupMenus();
    }
    
}
