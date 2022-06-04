/*
 * Copyright (c) 2022. Clément Grennerat
 * All rights reserved. You must refer to the licence Apache 2.
 */

package fr.clementgre.pdf4teachers.interfaces.windows.skillsassessment;

import fr.clementgre.pdf4teachers.interfaces.windows.AlternativeWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.panel.sidebar.skills.data.SkillsAssessment;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SkillsAssessmentWindow extends AlternativeWindow<VBox> {
    
    private final SkillsAssessment assessment;
    
    private final GridPane assessmentSettings = new GridPane();
    private final TextField name = new TextField();
    private final TextField clasz = new TextField();
    private final DatePicker date = new DatePicker();
    
    private final TabPane tabPane = new TabPane();
    private SkillsListingPane skillsListingPane;
    private NotationsListingPane notationsListingPane;
    
    public SkillsAssessmentWindow(SkillsAssessment assessment){
        super(new VBox(), StageWidth.LARGE, TR.tr("skillsSettingsWindow.title"), TR.tr("skillsSettingsWindow.title"), TR.tr("skillsSettingsWindow.subtitle"));
        this.assessment = assessment;
        
    }
    
    @Override
    public void setupSubClass(){
        setupUI();
        setupBtns();
    }
    
    @Override
    public void afterShown(){
    }
    
    public void setupBtns(){
        Button ok = new Button(TR.tr("actions.ok"));
        ok.setDefaultButton(true);
        Button sacocheImport = new Button(TR.tr("skillsSettingsWindow.sacocheImport"));
        Button sacocheExport = new Button(TR.tr("skillsSettingsWindow.sacocheExport"));
        Button csvExport = new Button(TR.tr("skillsSettingsWindow.csvExport"));
        
        ok.setOnAction(e -> close());
        sacocheImport.setOnAction(e -> {
        
        });
        sacocheExport.setOnAction(e -> {
        
        });
        csvExport.setOnAction(e -> {
        
        });
        
        setButtons(ok);
        setLeftButtons(sacocheImport, sacocheExport, csvExport);
        
    }
    
    private void setupUI(){
        container.minHeightProperty().bind(Bindings.createDoubleBinding(() -> scrollPane.getViewportBounds().getHeight()-5, scrollPane.viewportBoundsProperty()));
        
        VBox.setVgrow(root, Priority.ALWAYS);
        root.setStyle("-fx-padding: 0;");
        assessmentSettings.setStyle("-fx-padding: 15;");
    
        assessmentSettings.setHgap(10);
        assessmentSettings.setVgap(2);
        
        date.setPrefHeight(28);
        date.minHeightProperty().bind(new SimpleDoubleProperty(28));
        
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(33);
        assessmentSettings.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);
        assessmentSettings.setMaxWidth(600);
        
        assessmentSettings.add(new Label(TR.tr("skillsSettingsWindow.settings.name")), 0, 0);
        assessmentSettings.add(new Label(TR.tr("skillsSettingsWindow.settings.clasz")), 1, 0);
        assessmentSettings.add(new Label(TR.tr("skillsSettingsWindow.settings.date")), 2, 0);
        
        assessmentSettings.add(name, 0, 1);
        assessmentSettings.add(clasz, 1, 1);
        assessmentSettings.add(date, 2, 1);
    
        skillsListingPane = new SkillsListingPane(this);
        notationsListingPane = new NotationsListingPane(this);
        tabPane.getTabs().addAll(skillsListingPane, notationsListingPane);
        root.getChildren().addAll(assessmentSettings, tabPane);
    
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    
    }
    
    public SkillsAssessment getAssessment(){
        return assessment;
    }
}
