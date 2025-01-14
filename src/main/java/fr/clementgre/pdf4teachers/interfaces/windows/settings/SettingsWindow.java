package fr.clementgre.pdf4teachers.interfaces.windows.settings;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.datasaving.settings.Setting;
import fr.clementgre.pdf4teachers.datasaving.settings.SettingsGroup;
import fr.clementgre.pdf4teachers.interfaces.windows.AlternativeWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import java.lang.reflect.Field;

public class SettingsWindow extends AlternativeWindow<VBox>{
    
    public SettingsWindow(){
        super(new VBox(), StageWidth.NORMAL, TR.tr("settingsWindow.title"));
    }
    
    @Override
    public void setupSubClass(){
        setSubHeaderText(TR.tr("settingsWindow.description"));
    
        int i = 0;
        for(Field field : Main.settings.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(SettingsGroup.class)){
                try{
                    Setting<?>[] settings = (Setting<?>[]) field.get(Main.settings);
                    
                    SettingGroupPane groupPane = new SettingGroupPane(field.getAnnotation(SettingsGroup.class).title(), settings);
                    if(i != 0){
                        root.getChildren().add(generateHorizontalLine());
                    }
                    root.getChildren().add(groupPane);
                    i++;
                }catch(IllegalAccessException e){ e.printStackTrace(); }
            }
        }
        
        setupBtns();
    }
    
    private Node generateHorizontalLine(){
        Region region = new Region();
        region.setMaxWidth(Double.MAX_VALUE);
        region.setPrefHeight(1);
        region.setStyle("-fx-background-color: gray;");
        VBox.setMargin(region, new Insets(8, 0, 6, 0));
        return region;
    }
    
    private void setupBtns(){
        Button cancel = new Button(TR.tr("actions.cancel"));
        Button save = new Button(TR.tr("actions.save"));
    
        cancel.setOnAction(event -> {
            close();
            Main.settings.loadSettings();
        });
        save.setOnAction(event -> {
            close();
            Main.settings.saveSettings();
        });
        
        setOnCloseRequest((e) -> {
            close();
            Main.settings.saveSettings();
        });
        
        setButtons(cancel, save);
    }
    
    
    
    @Override
    public void afterShown(){
    
    }
}
