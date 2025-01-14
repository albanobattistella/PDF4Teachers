package fr.clementgre.pdf4teachers.panel;

import de.jangassen.MenuToolkit;
import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.components.menus.EmptyMenu;
import fr.clementgre.pdf4teachers.components.menus.NodeMenu;
import fr.clementgre.pdf4teachers.components.menus.NodeMenuItem;
import fr.clementgre.pdf4teachers.components.menus.NodeRadioMenuItem;
import fr.clementgre.pdf4teachers.document.editions.Edition;
import fr.clementgre.pdf4teachers.document.editions.EditionExporter;
import fr.clementgre.pdf4teachers.document.render.convert.ConvertDocument;
import fr.clementgre.pdf4teachers.document.render.display.PageEditPane;
import fr.clementgre.pdf4teachers.document.render.export.ExportWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.settings.SettingsWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.interfaces.windows.log.LogWindow;
import fr.clementgre.pdf4teachers.panel.MainScreen.MainScreen;
import fr.clementgre.pdf4teachers.utils.FilesUtils;
import fr.clementgre.pdf4teachers.utils.PlatformUtils;
import fr.clementgre.pdf4teachers.utils.dialogs.FIlesChooserManager;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.ButtonPosition;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.CustomAlert;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.OKAlert;
import fr.clementgre.pdf4teachers.utils.dialogs.alerts.WrongAlert;
import fr.clementgre.pdf4teachers.utils.image.ImageUtils;
import fr.clementgre.pdf4teachers.utils.svg.SVGPathIcons;
import fr.clementgre.pdf4teachers.utils.style.Style;
import fr.clementgre.pdf4teachers.utils.style.StyleManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("serial")
public class MenuBar extends javafx.scene.control.MenuBar{
    
    ////////// ICONS COLOR //////////
    
    public static ColorAdjust colorAdjust = new ColorAdjust();
    
    static{
        if(StyleManager.ACCENT_STYLE == jfxtras.styles.jmetro.Style.DARK) colorAdjust.setBrightness(-0.5);
        else colorAdjust.setBrightness(-1);
    }
    
    ////////// FILE //////////
    
    Menu file = new Menu(TR.tr("menuBar.file"));
    public MenuItem file1Open = createMenuItem(TR.tr("menuBar.file.openFiles"), SVGPathIcons.PDF_FILE, new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.openFiles.tooltip"));
    
    public MenuItem file2OpenDir = createMenuItem(TR.tr("menuBar.file.openDir"), SVGPathIcons.FOLDER, new KeyCodeCombination(KeyCode.O, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.openDir.tooltip"));
    
    MenuItem file3Clear = createMenuItem(TR.tr("menuBar.file.clearList"), SVGPathIcons.LIST, new KeyCodeCombination(KeyCode.W, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.clearList.tooltip"), false, true, false);
    
    MenuItem file4Save = createMenuItem(TR.tr("menuBar.file.saveEdit"), SVGPathIcons.SAVE_LITE, new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.saveEdit.tooltip"), true, false, false);
    
    MenuItem file5Delete = createMenuItem(TR.tr("menuBar.file.deleteEdit"), SVGPathIcons.TRASH, null,
            TR.tr("menuBar.file.deleteEdit.tooltip"), true, false, false);
    
    MenuItem file6Close = createMenuItem(TR.tr("menuBar.file.closeDocument"), SVGPathIcons.CROSS, new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.closeDocument.tooltip"), true, false, false);
    
    MenuItem file7Export = createMenuItem(TR.tr("menuBar.file.export"), SVGPathIcons.EXPORT, new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.export.tooltip"), true, false, false);
    
    MenuItem file8ExportAll = createMenuItem(TR.tr("menuBar.file.exportAll"), SVGPathIcons.EXPORT, new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.file.exportAll.tooltip"), false, true, false);
    
    
    ////////// TOOLS //////////
    
    public Menu tools = new Menu(TR.tr("menuBar.tools"));
    
    MenuItem tools1Convert = createMenuItem(TR.tr("menuBar.tools.convertImages"), SVGPathIcons.PICTURES, new KeyCodeCombination(KeyCode.C, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.tools.convertImages.tooltip"), false, false, false);
    
    Menu tools3AddPages = createSubMenu(TR.tr("menuBar.tools.addPages"), SVGPathIcons.PLUS,
            TR.tr("menuBar.tools.addPages.tooltip"), true);
    
    MenuItem tools4DeleteAllEdits = createMenuItem(TR.tr("menuBar.tools.deleteAllEdits"), SVGPathIcons.TRASH, null,
            TR.tr("menuBar.tools.deleteAllEdits.tooltip"));
    
    Menu tools5SameNameEditions = createSubMenu(TR.tr("menuBar.tools.sameNameEdits"), SVGPathIcons.EXCHANGE,
            TR.tr("menuBar.tools.sameNameEdits.tooltip"), true);
    MenuItem tools5SameNameEditionsNull = createMenuItem(TR.tr("menuBar.tools.sameNameEdits.noEditFounded"), null);
    
    Menu tools6ExportImportEdition = createSubMenu(TR.tr("menuBar.tools.exportOrImportEditOrGradeScale"), SVGPathIcons.EXPORT,
            TR.tr("menuBar.tools.exportOrImportEditOrGradeScale.tooltip"), true);
    
    MenuItem tools6ExportEdition1All = createMenuItem(TR.tr("menuBar.tools.exportEdit"), null, null,
            TR.tr("menuBar.tools.exportEdit.tooltip"), true, false, false);
    MenuItem tools6ExportEdition2Grades = createMenuItem(TR.tr("menuBar.tools.exportGradeScale"), null, null,
            TR.tr("menuBar.tools.exportGradeScale.tooltip"), true, false, false);
    
    MenuItem tools6ImportEdition1All = createMenuItem(TR.tr("menuBar.tools.importEdit"), null, null,
            TR.tr("menuBar.tools.importEdit.tooltip"), true, false, false);
    MenuItem tools6ImportEdition2Grades = createMenuItem(TR.tr("menuBar.tools.importGradeScale"), null, null,
            TR.tr("menuBar.tools.importGradeScale.tooltip"), true, false, false);
    
    MenuItem tools8FullScreen = createMenuItem(TR.tr("menuBar.tools.fullScreenMode"), SVGPathIcons.FULL_SCREEN, null,
            TR.tr("menuBar.tools.fullScreenMode.tooltip"));
    
    Menu tools9Debug = createSubMenu(TR.tr("menuBar.tools.debug"), SVGPathIcons.COMMAND_PROMPT,
            TR.tr("menuBar.tools.debug.tooltip"), false);
    
    MenuItem tools9Debug1OpenConsole = createMenuItem(TR.tr("menuBar.tools.debug.openPrintStream") + " (" + (Main.COPY_CONSOLE ? "Activée" : "Désactivée") + ")", null, new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN, KeyCombination.SHORTCUT_DOWN),
            TR.tr("menuBar.tools.debug.openPrintStream.tooltip"), false, false, false);
    MenuItem tools9Debug2OpenAppFolder = createMenuItem(TR.tr("menuBar.tools.debug.openDataFolder"), null, null,
            TR.tr("menuBar.tools.debug.openDataFolder.tooltip"), false, false, false);
    MenuItem tools9Debug3OpenEditionFile = createMenuItem(TR.tr("menuBar.tools.debug.openCurrentEditFile"), null, null,
            TR.tr("menuBar.tools.debug.openCurrentEditFile.tooltip"), true, false, false);
    
    ////////// HELP //////////
    
    Menu help = new Menu(TR.tr("menuBar.help"));
    MenuItem help1LoadDoc = createMenuItem(TR.tr("menuBar.help.loadDocumentation"), SVGPathIcons.INFO);
    MenuItem help2GitHubIssue = createMenuItem(TR.tr("menuBar.help.gitHubIssue"), SVGPathIcons.GITHUB);
    MenuItem help3Twitter = createMenuItem(TR.tr("menuBar.help.twitter"), SVGPathIcons.TWITTER);
    MenuItem help4Website = createMenuItem(TR.tr("menuBar.help.website"), SVGPathIcons.GLOBE);
    
    ////////// EMPTY MENUS SETTINGS //////////
    
    public EmptyMenu settings = new EmptyMenu(TR.tr("menuBar.settings"), this);
    public EmptyMenu about = new EmptyMenu(TR.tr("menuBar.about"), this);
    
    
    public MenuBar(){
        setup();
    }
    
    public static boolean isSystemMenuBarSupported(){
        return Main.isOSX();
    }
    
    public void setup(){
        if(isSystemMenuBarSupported()) setUseSystemMenuBar(true);
        
        ////////// FILE //////////
        
        file.getItems().addAll(file1Open, file2OpenDir, file3Clear, new SeparatorMenuItem(), file4Save, file5Delete, file6Close, new SeparatorMenuItem(), file7Export, file8ExportAll);
        
        ////////// TOOLS //////////
        
        tools3AddPages.getItems().add(new MenuItem(""));
        tools6ExportImportEdition.getItems().addAll(tools6ExportEdition1All, tools6ExportEdition2Grades, tools6ImportEdition1All, tools6ImportEdition2Grades);
        tools5SameNameEditions.getItems().add(tools5SameNameEditionsNull);
        tools9Debug.getItems().addAll(tools9Debug1OpenConsole, tools9Debug2OpenAppFolder, tools9Debug3OpenEditionFile);
        
        tools.getItems().addAll(tools1Convert, /*tools2QRCode,*/ tools3AddPages, new SeparatorMenuItem(), tools4DeleteAllEdits, tools5SameNameEditions, tools6ExportImportEdition, new SeparatorMenuItem(), tools8FullScreen, new SeparatorMenuItem(), tools9Debug);
        
        
        ////////// HELP //////////
        
        help.getItems().addAll(help1LoadDoc, help2GitHubIssue, help3Twitter, help4Website);
        
        ////////// SETUP ITEMS WIDTH ///////////
        
        NodeMenuItem.setupMenu(file);
        NodeMenuItem.setupMenu(tools);
        NodeMenuItem.setupMenu(tools6ExportImportEdition);
        NodeMenuItem.setupMenu(tools9Debug);
        NodeMenuItem.setupMenu(help);
        
        ////////// FILE //////////
        
        file1Open.setOnAction((ActionEvent actionEvent) -> {
            
            File[] files = FIlesChooserManager.showPDFFilesDialog(FIlesChooserManager.SyncVar.LAST_OPEN_DIR);
            if(files != null){
                MainWindow.filesTab.openFiles(files);
                if(files.length == 1){
                    MainWindow.mainScreen.openFile(files[0]);
                }
            }
        });
        file2OpenDir.setOnAction((ActionEvent actionEvent) -> {
            
            File directory = FIlesChooserManager.showDirectoryDialog(FIlesChooserManager.SyncVar.LAST_OPEN_DIR);
            if(directory != null){
                MainWindow.filesTab.openFiles(new File[]{directory});
            }
        });
        file3Clear.setOnAction((ActionEvent actionEvent) -> {
            MainWindow.filesTab.clearFiles();
        });
        file4Save.setOnAction((ActionEvent actionEvent) -> {
            if(MainWindow.mainScreen.hasDocument(true)){
                MainWindow.mainScreen.document.edition.save();
            }
        });
        file5Delete.setOnAction((ActionEvent e) -> {
            if(MainWindow.mainScreen.hasDocument(true)){
                MainWindow.mainScreen.document.edition.clearEdit(true);
            }
        });
        file6Close.setOnAction((ActionEvent e) -> {
            if(MainWindow.mainScreen.hasDocument(true)){
                MainWindow.mainScreen.closeFile(true);
            }
        });
        file7Export.setOnAction((ActionEvent actionEvent) -> {
            
            MainWindow.mainScreen.document.save();
            new ExportWindow(Collections.singletonList(MainWindow.mainScreen.document.getFile()));
            
        });
        file8ExportAll.setOnAction((ActionEvent actionEvent) -> {
            
            if(MainWindow.mainScreen.hasDocument(false)) MainWindow.mainScreen.document.save();
            new ExportWindow(MainWindow.filesTab.files.getItems());
            
        });
        
        ////////// TOOLS //////////
        
        tools1Convert.setOnAction(e -> {
            new ConvertDocument();
        });
        
        tools3AddPages.setOnShowing(e -> {
            tools3AddPages.getItems().setAll(PageEditPane.getNewPageMenu(0, MainWindow.mainScreen.document.totalPages, isSystemMenuBarSupported()));
            NodeMenuItem.setupMenu(tools3AddPages);
        });
        
        tools4DeleteAllEdits.setOnAction((ActionEvent e) -> {
            CustomAlert dialog = new CustomAlert(Alert.AlertType.WARNING, TR.tr("dialog.deleteEdits.confirmation.title"), TR.tr("dialog.deleteEdits.confirmation.header"));
            
            float yesButSize = FilesUtils.convertOctetToMo(FilesUtils.getSize(new File(Main.dataFolder + "editions")));
            float yesSize = 0L;
            for(File file : MainWindow.filesTab.files.getItems()){
                File editFile = Edition.getEditFile(file);
                yesSize += FilesUtils.getSize(editFile);
            }
            yesSize = FilesUtils.convertOctetToMo((long) yesSize);
            
            dialog.addNoButton(ButtonPosition.CLOSE);
            dialog.addButton(TR.tr("actions.yes") + " (" + yesSize + "Mi" + TR.tr("data.byte") + ")", ButtonPosition.DEFAULT);
            dialog.addButton(TR.tr("dialog.deleteEdits.confirmation.buttons.deleteAll") + " (" + yesButSize + "Mi" + TR.tr("data.byte") + ")", ButtonPosition.OTHER_RIGHT);
            
            ButtonPosition option = dialog.getShowAndWaitGetButtonPosition(ButtonPosition.CLOSE);
            float size;
            if(option == ButtonPosition.DEFAULT){
                if(MainWindow.mainScreen.hasDocument(false)) MainWindow.mainScreen.document.edition.clearEdit(false);
                for(File file : MainWindow.filesTab.files.getItems()) Edition.getEditFile(file).delete();
                size = yesSize;
            }else if(option == ButtonPosition.OTHER_RIGHT){
                if(MainWindow.mainScreen.hasDocument(false)) MainWindow.mainScreen.document.edition.clearEdit(false);
                for(File file : Objects.requireNonNull(new File(Main.dataFolder + "editions").listFiles()))
                    file.delete();
                size = yesButSize;
            }else return;
            
            new OKAlert(TR.tr("dialog.deleteEdits.completed.title"),
                    TR.tr("dialog.deleteEdits.completed.header"), TR.tr("dialog.deleteEdits.completed.details", String.valueOf(size))).show();
        });
        tools5SameNameEditions.setOnShowing((Event event) -> {
            tools5SameNameEditions.getItems().clear();
            int i = 0;
            for(Map.Entry<File, File> files : Edition.getEditFilesWithSameName(MainWindow.mainScreen.document.getFile()).entrySet()){
                
                MenuItem item = createMenuItem(files.getValue().getAbsolutePath(), null);
                if(files.getValue().getParentFile() != null){
                    item.setText(files.getValue().getParentFile().getAbsolutePath().replace(System.getProperty("user.home"), "~") + File.separator);
                }
                
                
                tools5SameNameEditions.getItems().add(item);
                item.setOnAction((ActionEvent actionEvent) -> {
                    CustomAlert dialog = new CustomAlert(Alert.AlertType.CONFIRMATION, TR.tr("dialog.importEdit.confirm.title"), TR.tr("dialog.loadSameNameEdit.confirmation.header"));
                    
                    dialog.addNoButton(ButtonPosition.CLOSE);
                    dialog.addYesButton(ButtonPosition.DEFAULT);
                    dialog.addButton(TR.tr("dialog.loadSameNameEdit.confirmation.buttons.yesForAllSameFolder"), ButtonPosition.OTHER_RIGHT);
                    
                    ButtonPosition option = dialog.getShowAndWaitGetButtonPosition(ButtonPosition.CLOSE);
                    if(option == ButtonPosition.DEFAULT){
                        if(MainWindow.mainScreen.hasDocument(true)){
                            
                            MainWindow.mainScreen.document.edition.clearEdit(false);
                            Edition.mergeEditFileWithEditFile(files.getKey(), Edition.getEditFile(MainWindow.mainScreen.document.getFile()));
                            MainWindow.mainScreen.document.loadEdition();
                        }
                    }else if(option == ButtonPosition.OTHER_RIGHT){
                        if(MainWindow.mainScreen.hasDocument(true)){
                            
                            MainWindow.mainScreen.document.edition.clearEdit(false);
                            Edition.mergeEditFileWithEditFile(files.getKey(), Edition.getEditFile(MainWindow.mainScreen.document.getFile()));
                            MainWindow.mainScreen.document.loadEdition();
                            
                            for(File otherFileDest : MainWindow.filesTab.files.getItems()){
                                if(otherFileDest.getParentFile().getAbsolutePath().equals(MainWindow.mainScreen.document.getFile().getParentFile().getAbsolutePath()) && !otherFileDest.equals(MainWindow.mainScreen.document.getFile())){
                                    File fromEditFile = Edition.getEditFile(new File(files.getValue().getParentFile().getAbsolutePath() + "/" + otherFileDest.getName()));
                                    
                                    if(fromEditFile.exists()){
                                        Edition.mergeEditFileWithEditFile(fromEditFile, Edition.getEditFile(otherFileDest));
                                    }else{
                                        WrongAlert alert = new WrongAlert(TR.tr("dialog.loadSameNameEdit.fileNotFound.title"),
                                                TR.tr("dialog.loadSameNameEdit.fileNotFound.header", otherFileDest.getName(), FilesUtils.getPathReplacingUserHome(files.getValue().getParentFile())), true);
                                        if(alert.execute()) return;
                                    }
                                }
                            }
                        }
                    }
                });
                i++;
            }
            if(i == 0) tools5SameNameEditions.getItems().add(tools5SameNameEditionsNull);
            else NodeMenuItem.setupMenu(tools5SameNameEditions);
        });
        
        tools6ExportEdition1All.setOnAction((e) -> EditionExporter.showExportDialog(false));
        tools6ExportEdition2Grades.setOnAction((e) -> EditionExporter.showExportDialog(true));
        tools6ImportEdition1All.setOnAction((e) -> EditionExporter.showImportDialog(false));
        tools6ImportEdition2Grades.setOnAction((e) -> EditionExporter.showImportDialog(true));
        
        tools8FullScreen.setOnAction((e) -> {
            Main.window.setFullScreen(!Main.window.isFullScreen());
        });
        
        tools9Debug1OpenConsole.setOnAction((e) -> new LogWindow());
        tools9Debug2OpenAppFolder.setOnAction((e) -> PlatformUtils.openDirectory(Main.dataFolder));
        tools9Debug3OpenEditionFile.setOnAction((e) -> PlatformUtils.openFile(Edition.getEditFile(MainWindow.mainScreen.document.getFile()).getAbsolutePath()));
    
        ////////// ABOUT / HELP //////////
        
        help1LoadDoc.setOnAction((ActionEvent actionEvent) -> MainWindow.mainScreen.openFile(TR.getDocFile()));
        help2GitHubIssue.setOnAction((ActionEvent actionEvent) -> {
            try{
                Desktop.getDesktop().browse(new URI("https://github.com/themsou/PDF4Teachers/issues/new"));
            }catch(IOException | URISyntaxException e){
                e.printStackTrace();
            }
        });
        help3Twitter.setOnAction((ActionEvent t) -> Main.hostServices.showDocument("https://twitter.com/PDF4Teachers"));
        help4Website.setOnAction((ActionEvent t) -> Main.hostServices.showDocument("https://pdf4teachers.org"));
        
        ////////// END PROCESS - OSX ADAPTION & MENU //////////
        
        // UI Style
        setStyle("");
        StyleManager.putStyle(this, Style.ACCENT);
        
        if(isSystemMenuBarSupported()){
    
            if(Main.isOSX()){
                getMenus().addAll(file, tools, help);
    
                MenuToolkit tk = MenuToolkit.toolkit(TR.locale);
                
                MenuItem about = tk.createAboutMenuItem("");
                about.setText(TR.tr("menuBar.osx.about", Main.APP_NAME));
                about.setOnAction((e) -> Main.showAboutWindow());
    
                MenuItem settings = tk.createAboutMenuItem("", (e) -> new SettingsWindow());
                settings.setText(TR.tr("menuBar.settings"));
                settings.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
                
                MenuItem hide = tk.createHideMenuItem("");
                hide.setText(TR.tr("menuBar.osx.hide", Main.APP_NAME));
                
                MenuItem hideOthers = tk.createHideOthersMenuItem();
                hideOthers.setText(TR.tr("menuBar.osx.hideOthers"));
                
                MenuItem unhideAll = tk.createUnhideAllMenuItem();
                unhideAll.setText(TR.tr("menuBar.osx.unhideAll"));
                
                MenuItem quit = tk.createQuitMenuItem("");
                quit.setText(TR.tr("menuBar.osx.quit", Main.APP_NAME));
                quit.setOnAction((e) ->{
                    if(!MainWindow.requestCloseApp()) e.consume();
                });
                
                Menu defaultApplicationMenu = new Menu(Main.APP_NAME, null,
                        about, new SeparatorMenuItem(), settings, new SeparatorMenuItem(),
                        hide, hideOthers, unhideAll, new SeparatorMenuItem(), quit);
                tk.setApplicationMenu(defaultApplicationMenu);
            }
            
        }else{
            settings.setOnClick(e -> new SettingsWindow());
            about.setOnClick(e -> Main.showAboutWindow());
            
            getMenus().addAll(file, tools, help, settings, about);
    
            setupMenus();
            Main.settings.menuForceOpenDelay.valueProperty().addListener((observable, oldValue, newValue) -> {
                setupMenus();
            });
        }
        
    }
    
    public void setupMenus(){
        for(Menu menu : getMenus()){
            if(!menu.getItems().isEmpty()){
                menu.setStyle("-fx-padding: 5 7 5 7;");
                if(Main.settings.menuForceOpenDelay.getValue() == 0){
                    menu.setOnShowing((e) -> {
                        Platform.runLater(menu::show);
                    });
                }else if(Main.settings.menuForceOpenDelay.getValue() == 1){
                    menu.setOnShowing((e) -> {
                        for(int i = 50; i <= 500; i+=50){
                            PlatformUtils.runLaterOnUIThread(i, () -> {
                                for(Menu m : getMenus()){
                                    if(m.isShowing()) return;
                                }
                                menu.show();
                            });
                        }
                    });
                }else if(Main.settings.menuForceOpenDelay.getValue() > 0){
                    menu.setOnShowing((e) -> {
                        PlatformUtils.runLaterOnUIThread(Main.settings.menuForceOpenDelay.getValue(), () -> {
                            for(Menu m : getMenus()){
                                if(m.isShowing()) return;
                            }
                            menu.show();
                        });
                    });
                }
            }
            else menu.setStyle("-fx-padding: 0;");
        }
    }
    
    
    public static Menu createSubMenu(String name, String image, String toolTip, boolean disableIfNoDoc){
    
        Menu menu;
        if(isSystemMenuBarSupported()){
            menu = new Menu(name);
        }else{
            menu = new NodeMenu(name);
            
            if(image != null){
                if(image.length() >= 30){
                    ((NodeMenu) menu).setImage(SVGPathIcons.generateImage(image, "white", 0, 16, 16, colorAdjust));
                }else{
                    if(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") == null) System.err.println("MenuBar image " + image + " does not exist");
                    else ((NodeMenu) menu).setImage(ImageUtils.buildImage(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") + "", 0, 0, colorAdjust));
                }
            }
            if(toolTip != null && !toolTip.isBlank()) ((NodeMenu) menu).setToolTip(toolTip);
        }
    
        if(disableIfNoDoc){
            menu.disableProperty().bind(Bindings.createBooleanBinding(() -> MainWindow.mainScreen.statusProperty().get() != MainScreen.Status.OPEN, MainWindow.mainScreen.statusProperty()));
        }
        return menu;
    }
    
    public static MenuItem createRadioMenuItem(String text, String image, String toolTip, boolean autoUpdate){
        
        if(isSystemMenuBarSupported()){
            RadioMenuItem menuItem = new RadioMenuItem(text);
            //if(imgName != null) menuItem.setGraphic(ImageUtils.buildImage(getClass().getResource("/img/MenuBar/"+ imgName + ".png")+"", 0, 0));
            
            //OSX selects radioMenuItems upon click, but doesn't unselect it on click :
            AtomicBoolean selected = new AtomicBoolean(false);
            menuItem.selectedProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(() -> {
                    selected.set(newValue);
                });
            });
            menuItem.setOnAction((e) -> {
                menuItem.setSelected(!selected.get());
            });
            
            return menuItem;
            
        }else{
            NodeRadioMenuItem menuItem = new NodeRadioMenuItem(text + "      ", autoUpdate, true);
            
            if(image != null){
                if(image.length() >= 30){
                    menuItem.setImage(SVGPathIcons.generateImage(image, "white", 0, 16, 16, colorAdjust));
                }else{
                    if(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") == null)
                        System.err.println("MenuBar image " + image + " does not exist");
                    else
                        menuItem.setImage(ImageUtils.buildImage(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") + "", 0, 0, colorAdjust));
                }
                
            }
            if(!toolTip.isBlank()) menuItem.setToolTip(toolTip);
            
            return menuItem;
        }
        
        
    }
    
    public static MenuItem createMenuItem(String text, String image, KeyCombination keyCombinaison, String toolTip, boolean disableIfNoDoc, boolean disableIfNoList, boolean leftMargin){
        if(isSystemMenuBarSupported()){
            MenuItem menuItem = new MenuItem(text);
            //if(imgName != null) menuItem.setGraphic(ImageUtils.buildImage(getClass().getResource("/img/MenuBar/"+ imgName + ".png")+"", 0, 0));
            if(keyCombinaison != null) menuItem.setAccelerator(keyCombinaison);
            if(disableIfNoDoc){
                menuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> MainWindow.mainScreen.statusProperty().get() != MainScreen.Status.OPEN, MainWindow.mainScreen.statusProperty()));
            }
            if(disableIfNoList){
                menuItem.disableProperty().bind(Bindings.size(MainWindow.filesTab.getOpenedFiles()).isEqualTo(0));
            }
            return menuItem;
        }else{
            NodeMenuItem menuItem = new NodeMenuItem(text, true);
            
            if(image != null){
                if(image.length() >= 30){
                    menuItem.setImage(SVGPathIcons.generateImage(image, "white", 0, 16, 16, colorAdjust));
                }else{
                    if(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") == null) System.err.println("MenuBar image " + image + " does not exist");
                    else menuItem.setImage(ImageUtils.buildImage(MenuBar.class.getResource("/img/MenuBar/" + image + ".png") + "", 0, 0, colorAdjust));
                }
                
            }
            
            if(keyCombinaison != null) menuItem.setKeyCombinaison(keyCombinaison);
            if(toolTip != null && !toolTip.isBlank()) menuItem.setToolTip(toolTip);
            if(leftMargin) menuItem.setFalseLeftData();
            
            if(disableIfNoDoc){
                menuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> MainWindow.mainScreen.statusProperty().get() != MainScreen.Status.OPEN, MainWindow.mainScreen.statusProperty()));
            }
            if(disableIfNoList){
                menuItem.disableProperty().bind(Bindings.size(MainWindow.filesTab.getOpenedFiles()).isEqualTo(0));
            }
            
            return menuItem;
        }
    }
    
    public static MenuItem createMenuItem(String text, String imgName, KeyCombination keyCombinaison, String toolTip){
        return createMenuItem(text, imgName, keyCombinaison, toolTip, false, false, false);
    }
    
    public static MenuItem createMenuItem(String text, String imgName, KeyCombination keyCombinaison, String toolTip, boolean leftMargin){
        return createMenuItem(text, imgName, keyCombinaison, toolTip, false, false, leftMargin);
    }
    public static MenuItem createMenuItem(String text, String imgName){
        return createMenuItem(text, imgName, null, "", false, false, false);
    }
}