package fr.clementgre.pdf4teachers.panel.sidebar.grades.export;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.utils.PaneUtils;
import fr.clementgre.pdf4teachers.utils.StringUtils;
import fr.clementgre.pdf4teachers.utils.dialog.DialogBuilder;
import fr.clementgre.pdf4teachers.utils.style.Style;
import fr.clementgre.pdf4teachers.utils.style.StyleManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

public class GradeExportWindow extends Stage{
    
    TabPane tabPane = new TabPane();
    
    ExportPane exportAllTab = new ExportPane(this, TR.tr("gradeTab.gradeExportWindow.exportMode.together.name"), 0, false, true, true, false);
    ExportPane exportAllSplitTab = new ExportPane(this, TR.tr("gradeTab.gradeExportWindow.exportMode.separately.name"), 1, true, true, true, true);
    ExportPane exportThisTab = new ExportPane(this, TR.tr("gradeTab.gradeExportWindow.exportMode.onlyThis.name"), 2, false, false, false, true);
    
    public GradeExportWindow(){
        
        VBox root = new VBox();
        Scene scene = new Scene(root);
        
        initOwner(Main.window);
        initModality(Modality.WINDOW_MODAL);
        getIcons().add(new Image(getClass().getResource("/logo.png") + ""));
        setWidth(650);
        setResizable(false);
        setTitle(TR.tr("gradeTab.gradeExportWindow.title"));
        setScene(scene);
        StyleManager.putStyle(root, Style.DEFAULT);
        
        Text info = new Text(TR.tr("gradeTab.gradeExportWindow.header"));
        VBox.setMargin(info, new Insets(40, 0, 40, 10));
        
        tabPane.getTabs().addAll(exportAllTab, exportAllSplitTab, exportThisTab);
        root.getChildren().addAll(info, tabPane);
        
        show();
    }
    
    class ExportPane extends Tab{
        
        public int type;
        GradeExportWindow window;
        
        boolean fileNameCustom, studentNameCustom, multipleFilesCustom, canExportTextElements;
        
        VBox root = new VBox();
        
        public TextField fileNameSimple, fileNamePrefix, fileNameSuffix, fileNameReplace, fileNameBy;
        public TextField studentNameSimple, studentNameReplace, studentNameBy;
        public TextField filePath;
        
        public CheckBox settingsOnlySameGradeScale = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.onlySameGradeScale"));
        public CheckBox settingsOnlyCompleted = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.onlyCompleted"));
        public CheckBox settingsOnlySameDir = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.onlySameDir"));
        public CheckBox settingsAttributeTotalLine = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.attributeTotalLine"));
        public CheckBox settingsAttributeAverageLine = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.attributeAverageLine"));
        public CheckBox settingsWithTxtElements = new CheckBox(TR.tr("gradeTab.gradeExportWindow.options.withTxtElements"));
        public Slider settingsTiersExportSlider = new Slider(1, 5, MainWindow.userData.settingsTiersExportSlider);
        
        
        public ExportPane(GradeExportWindow window, String tabName, int type, boolean fileNameCustom, boolean studentNameCustom, boolean multipleFilesCustom, boolean canExportTextElements){
            
            super(tabName);
            this.window = window;
            this.type = type;
            this.fileNameCustom = fileNameCustom;
            this.studentNameCustom = studentNameCustom;
            this.multipleFilesCustom = multipleFilesCustom;
            this.canExportTextElements = canExportTextElements;
            
            setClosable(false);
            setContent(root);
            root.setStyle("-fx-padding: 10;");
            
            setupFileNameForm();
            setupStudentNameForm();
            setupPathForm();
            setupSettingsForm();
            setupBtns();
            
        }
        
        public void setupFileNameForm(){
            
            VBox info = generateInfo(TR.tr("file.documentName") + " :", false);
            
            if(fileNameCustom){
                HBox fileNamePrefixSuffixBox = new HBox();
                HBox fileNameReplaceBox = new HBox();
                
                fileNamePrefix = new TextField(MainWindow.userData.lastExportFileNamePrefix);
                fileNamePrefix.setPromptText(TR.tr("string.prefix"));
                PaneUtils.setHBoxPosition(fileNamePrefix, -1, 30, 0, 2.5);
                fileNamePrefix.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportFileNamePrefix = newValue);
                
                TextField fileName = new TextField(TR.tr("file.documentName"));
                fileName.setDisable(true);
                fileName.setAlignment(Pos.CENTER);
                PaneUtils.setHBoxPosition(fileName, 0, 30, 0, 2.5);
                
                fileNameSuffix = new TextField(MainWindow.userData.lastExportFileNameSuffix);
                fileNameSuffix.setPromptText(TR.tr("string.suffix"));
                PaneUtils.setHBoxPosition(fileNameSuffix, -1, 30, 0, 2.5);
                fileNameSuffix.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportFileNameSuffix = newValue);
                
                fileNamePrefixSuffixBox.getChildren().addAll(fileNamePrefix, fileName, fileNameSuffix);
                
                
                Label replaceText = new Label(TR.tr("exportWindow.multipleFiles.replaceFields.replace"));
                PaneUtils.setHBoxPosition(replaceText, 0, 30, 2.5);
                
                fileNameReplace = new TextField(MainWindow.userData.lastExportFileNameReplace);
                PaneUtils.setHBoxPosition(fileNameReplace, -1, 30, 0, 2.5);
                fileNameReplace.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportFileNameReplace = newValue);
                
                Label byText = new Label(TR.tr("exportWindow.multipleFiles.replaceFields.by"));
                PaneUtils.setHBoxPosition(byText, 0, 30, 2.5);
                
                fileNameBy = new TextField(MainWindow.userData.lastExportFileNameBy);
                PaneUtils.setHBoxPosition(fileNameBy, -1, 30, 0, 2.5);
                fileNameBy.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportFileNameBy = newValue);
                
                fileNameReplaceBox.getChildren().addAll(replaceText, fileNameReplace, byText, fileNameBy);
                
                root.getChildren().addAll(info, fileNamePrefixSuffixBox, fileNameReplaceBox);
                
            }else{
                
                fileNameSimple = new TextField(MainWindow.userData.lastExportFileName.isEmpty() || type == 2 ? StringUtils.removeAfterLastRegex(MainWindow.mainScreen.document.getFileName(), ".pdf") + ".csv" : MainWindow.userData.lastExportFileName);
                fileNameSimple.setPromptText(TR.tr("file.documentName"));
                PaneUtils.setHBoxPosition(fileNameSimple, 0, 30, 0, 2.5);
                if(type != 2)
                    fileNameSimple.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportFileName = newValue);
                root.getChildren().addAll(info, fileNameSimple);
                
            }
            
        }
        
        public void setupStudentNameForm(){
            
            
            if(studentNameCustom){
                VBox info = generateInfo(TR.tr("gradeTab.gradeExportWindow.fields.studentNameAutoGenerated"), true);
                HBox studentNameReplaceBox = new HBox();
                
                Label replaceText = new Label(TR.tr("exportWindow.multipleFiles.replaceFields.replace"));
                PaneUtils.setHBoxPosition(replaceText, 0, 30, 2.5);
                
                studentNameReplace = new TextField(MainWindow.userData.lastExportStudentNameReplace);
                PaneUtils.setHBoxPosition(studentNameReplace, -1, 30, 0, 2.5);
                studentNameReplace.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportStudentNameReplace = newValue);
                
                Label byText = new Label(TR.tr("exportWindow.multipleFiles.replaceFields.by"));
                PaneUtils.setHBoxPosition(byText, 0, 30, 2.5);
                
                studentNameBy = new TextField(MainWindow.userData.lastExportStudentNameBy);
                PaneUtils.setHBoxPosition(studentNameBy, -1, 30, 0, 2.5);
                studentNameBy.textProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.lastExportStudentNameBy = newValue);
                
                studentNameReplaceBox.getChildren().addAll(replaceText, studentNameReplace, byText, studentNameBy);
                
                root.getChildren().addAll(info, studentNameReplaceBox);
                
            }else{
                VBox info = generateInfo(TR.tr("gradeTab.gradeExportWindow.fields.studentName"), true);
                
                studentNameSimple = new TextField(StringUtils.removeAfterLastRegex(MainWindow.mainScreen.document.getFileName(), ".pdf"));
                studentNameSimple.setPromptText(TR.tr("gradeTab.gradeExportWindow.fields.studentName"));
                PaneUtils.setHBoxPosition(studentNameSimple, 0, 30, 0, 2.5);
                
                root.getChildren().addAll(info, studentNameSimple);
                
            }
            
        }
        
        public void setupPathForm(){
            
            VBox info = generateInfo(TR.tr("file.destinationFolder") + " :", true);
            
            HBox filePathBox = new HBox();
            
            filePath = new TextField(MainWindow.mainScreen.document.getFile().getParentFile().getPath() + File.separator);
            PaneUtils.setHBoxPosition(filePath, -1, 30, 0, 2.5);
            
            Button changePath = new Button(TR.tr("file.browse"));
            PaneUtils.setHBoxPosition(changePath, 0, 30, new Insets(2.5, 0, 2.5, 2.5));
            
            filePathBox.getChildren().addAll(filePath, changePath);
            
            root.getChildren().addAll(info, filePathBox);
            
            changePath.setOnAction(event -> {
                
                final DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle(TR.tr("dialog.file.selectFolder.title"));
                chooser.setInitialDirectory((new File(filePath.getText()).exists() ? new File(filePath.getText()) : new File(MainWindow.mainScreen.document.getFile().getParentFile().getPath() + File.separator)));
                
                File file = chooser.showDialog(Main.window);
                if(file != null) filePath.setText(file.getAbsolutePath() + File.separator);
            });
            
        }
        
        public void setupSettingsForm(){
            
            VBox info = generateInfo(TR.tr("convertWindow.options.title"), true);
            
            HBox tiersExport = new HBox();
            Label tiersExportLabel = new Label(TR.tr("gradeTab.gradeExportWindow.options.tiersExportSlider"));
            tiersExport.getChildren().addAll(tiersExportLabel, settingsTiersExportSlider);
            settingsTiersExportSlider.valueProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsTiersExportSlider = newValue.intValue());
            
            settingsTiersExportSlider.setSnapToTicks(true);
            settingsTiersExportSlider.setMajorTickUnit(1);
            settingsTiersExportSlider.setMinorTickCount(0);
            
            PaneUtils.setHBoxPosition(settingsOnlySameGradeScale, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsOnlyCompleted, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsOnlySameDir, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsAttributeTotalLine, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsAttributeAverageLine, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsWithTxtElements, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(settingsTiersExportSlider, 0, 30, 0, 2.5);
            PaneUtils.setHBoxPosition(tiersExportLabel, 0, 30, 2.5, 2.5);
            
            root.getChildren().add(info);
            
            settingsAttributeTotalLine.setSelected(MainWindow.userData.settingsAttributeTotalLine);
            if(multipleFilesCustom){
                settingsOnlySameGradeScale.setSelected(MainWindow.userData.settingsOnlySameGradeScale);
                settingsOnlyCompleted.setSelected(MainWindow.userData.settingsOnlyCompleted);
                settingsOnlySameDir.setSelected(MainWindow.userData.settingsOnlySameDir);
                settingsAttributeAverageLine.setSelected(MainWindow.userData.settingsAttributeMoyLine);
                root.getChildren().addAll(settingsOnlySameGradeScale, settingsOnlyCompleted, settingsOnlySameDir, settingsAttributeTotalLine, settingsAttributeAverageLine);
            }else root.getChildren().add(settingsAttributeTotalLine);
            
            if(canExportTextElements){
                settingsWithTxtElements.setSelected(MainWindow.userData.settingsWithTxtElements);
                root.getChildren().add(settingsWithTxtElements);
            }
            root.getChildren().add(tiersExport);
            
            settingsOnlySameGradeScale.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsOnlySameGradeScale = newValue);
            settingsOnlyCompleted.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsOnlyCompleted = newValue);
            settingsOnlySameDir.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsOnlySameDir = newValue);
            settingsAttributeTotalLine.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsAttributeTotalLine = newValue);
            settingsAttributeAverageLine.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsAttributeMoyLine = newValue);
            settingsWithTxtElements.selectedProperty().addListener((observable, oldValue, newValue) -> MainWindow.userData.settingsWithTxtElements = newValue);
            
        }
        
        public void setupBtns(){
            
            HBox btnBox = new HBox();
            
            Button cancel = new Button(TR.tr("actions.cancel"));
            Button export = new Button(TR.tr("actions.export"));
            export.requestFocus();
            
            btnBox.getChildren().addAll(cancel, export);
            btnBox.setAlignment(Pos.CENTER_RIGHT);
            
            HBox.setMargin(cancel, new Insets(20, 5, 0, 10));
            HBox.setMargin(export, new Insets(20, 10, 0, 5));
            
            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);
            
            export.setOnAction(event -> {
                end(new GradeExportRenderer(this).start());
            });
            cancel.setOnAction(event -> {
                window.close();
            });
            
            root.getChildren().addAll(spacer, btnBox);
        }
        
        public VBox generateInfo(String text, boolean topBar){
            
            VBox box = new VBox();
            
            if(topBar){
                Separator separator = new Separator();
                PaneUtils.setVBoxPosition(separator, 0, 0, new Insets(5, -5, 0, -5));
                box.getChildren().add(separator);
            }
            
            Label info = new Label(text);
            PaneUtils.setVBoxPosition(info, 0, 0, 2.5);
            box.getChildren().add(info);
            
            return box;
        }
        
        private void end(int exported){
            close();
            
            String header;
            if(exported == 0) header = TR.tr("exportWindow.dialogs.completed.header.noDocument");
            else if(exported == 1) header = TR.tr("exportWindow.dialogs.completed.header.oneDocument");
            else header = TR.tr("exportWindow.dialogs.completed.header.multipleDocument", exported);
            
            DialogBuilder.showAlertWithOpenDirButton(TR.tr("actions.export.completedMessage"), header, null, filePath.getText());
            
        }
        
    }
}