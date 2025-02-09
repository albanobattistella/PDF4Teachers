package fr.clementgre.pdf4teachers;

import fr.clementgre.pdf4teachers.utils.dialogs.AlertIconType;
import fr.clementgre.pdf4teachers.datasaving.SyncUserData;
import fr.clementgre.pdf4teachers.datasaving.settings.Settings;
import fr.clementgre.pdf4teachers.interfaces.autotips.AutoTipsManager;
import fr.clementgre.pdf4teachers.interfaces.windows.LicenseWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.LanguageWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.interfaces.windows.log.LogWindow;
import fr.clementgre.pdf4teachers.utils.PlatformUtils;
import fr.clementgre.pdf4teachers.utils.fonts.AppFontsLoader;
import fr.clementgre.pdf4teachers.utils.fonts.FontUtils;
import fr.clementgre.pdf4teachers.utils.image.ImageUtils;
import fr.clementgre.pdf4teachers.utils.style.StyleManager;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.DataFormat;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Main extends Application{
    
    public static MainWindow window;
    
    public static Settings settings;
    public static SyncUserData syncUserData;
    
    public static HostServices hostServices;
    
    public static String dataFolder = System.getProperty("user.home") + File.separator + ".PDF4Teachers" + File.separator;
    public static final String APP_NAME = "PDF4Teachers";
    public static final String VERSION = "sn1-1.3.0";
    public static final int VERSION_ID = 1;
    public static final boolean DEBUG = true;
    public static final boolean COPY_CONSOLE = true;
    public static final boolean TRANSLATIONS_IN_CODE = true;
    
    public static boolean firstLaunch;
    public static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getBounds();
    public static final Rectangle2D SCREEN_VISUAL_BOUNDS = Screen.getPrimary().getVisualBounds();
    public static String systemShortcut = "Ctrl";
    public static List<String> params;
    
    public static DecimalFormatSymbols baseDecimalFormatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    static{
        baseDecimalFormatSymbols.setDecimalSeparator('.');
    }
    public static DecimalFormat baseFormat = new DecimalFormat("0.####", baseDecimalFormatSymbols);
    public static DecimalFormat oneDigFormat = new DecimalFormat("0.#", baseDecimalFormatSymbols);
    
    public static final DataFormat INTERNAL_FORMAT = new DataFormat("application/pdf4teachers-internal-format; class=java.lang.String");
    
    public static void main(String[] args){
        if(COPY_CONSOLE) LogWindow.copyLogs();
        System.out.println("Starting PDF4Teachers... (Java " + System.getProperty("java.version") + ")");
        
        // Enable anti aliasing
        //System.setProperty("prism.lcdtext", "false");
        ///// START APP /////
        launch(args);
    }
    
    // OSX FILE COPY
    private void copyDirToNewOSXLocation(File source, File output){
        if(!output.mkdirs()) throw new RuntimeException("Unable to create dir " + output.getAbsolutePath());
        for(File file : Objects.requireNonNull(source.listFiles())){
            File destFile = new File(output.getAbsolutePath() + "/" + file.getName());
            if(file.isDirectory()){
                copyDirToNewOSXLocation(file, destFile);
            }else{
                try{
                    copyFileUsingStream(file, destFile);
                    file.delete();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        source.delete();
    }
    private void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
    
    @Override
    public void start(Stage stage){
        
        // define crucial vars
        if(isWindows()) dataFolder = System.getenv("APPDATA") + "\\PDF4Teachers\\";
        else if(isOSX()){
            dataFolder = System.getProperty("user.home") + "/Library/Application Support/PDF4Teachers/";
            systemShortcut = "Cmd";
    
            // OSX PDF4Teachers directory moved
            try{
                if(!new File(dataFolder).exists() && new File(System.getProperty("user.home") + "/.PDF4Teachers/").exists()){
                    copyDirToNewOSXLocation(new File(System.getProperty("user.home") + "/.PDF4Teachers/"), new File(dataFolder));
                    PlatformUtils.runLaterOnUIThread(5000, () -> {
                        MainWindow.showNotification(AlertIconType.INFORMATION, TR.tr("osx.moveDataFolderNotification"), 10);
                        new File(System.getProperty("user.home") + "/.PDF4Teachers/").delete();
                    });
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        firstLaunch = !new File(dataFolder + File.separator + "settings.yml").exists();
        hostServices = getHostServices();
        
        // read params
        if(DEBUG && (!getParameters().getRaw().isEmpty() || !getParameters().getNamed().isEmpty())){
            System.out.println("Starting with parameters: \nRaw: " + getParameters().getRaw().toString()
                    + "\n Unnamed: " + getParameters().getUnnamed().toString()
                    + "\n Named: " + getParameters().getNamed().toString());
        }
        
        params = getParameters().getRaw();
        
        // PREPARATION
        
        settings = new Settings();
        syncUserData = new SyncUserData();
        
        // setups
        TR.setup();
        StyleManager.setup();
        AutoTipsManager.setup();
        ImageUtils.setupListeners();
        FontUtils.setup();
        AppFontsLoader.loadFont(AppFontsLoader.NOTO_SANS_KR);
        AppFontsLoader.loadFont(AppFontsLoader.NOTO_SANS_KR_BOLD);
        AppFontsLoader.loadFontPath(AppFontsLoader.OPEN_SANS);
        
        
        if(languageAsk()){
            if(licenceAsk()){
                startMainWindowAuto();
            }
        }
    }
    
    public boolean languageAsk(){
        if(settings.language.getValue().isEmpty()){
            String language = TR.getLanguageFromComputerLanguage();
            if(language != null){
                Main.settings.language.setValue(language);
                Main.settings.saveSettings();
                TR.updateLocale();
            }else{
                showLanguageWindow(true);
                return false;
            }
        }
        return true;
    }

    public static void showLanguageWindow(boolean firstStartBehaviour){
        LanguageWindow.checkUpdatesAndShow(value -> {
            if(!value.isEmpty() && !value.equals(Main.settings.language.getValue())){
                String oldDocPath = TR.getDocFile().getAbsolutePath();
                
                Main.settings.language.setValue(value);
                Main.settings.saveSettings();
                
                if(!firstStartBehaviour){
                    Main.window.restart(true, oldDocPath);
                }else{
                    TR.updateLocale();
                    if(licenceAsk()){
                        startMainWindowAuto();
                    }
                }
            }
        });
    }

    public static void showAboutWindow(){
        try{
            FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("/fxml/AboutWindow.fxml")));
        }catch(IOException e){ e.printStackTrace(); }
    }
    
    public static boolean licenceAsk(){
        
        // Disabling the license
        if(true) return true;
        
        if(firstLaunch){
            new LicenseWindow(value -> {
                startMainWindowAuto();
            });
            return false;
        }else{
            return true;
        }
    }
    
    public static void startMainWindow(boolean openDocumentation){
        window = new MainWindow();
        window.setup(openDocumentation);
    }
    public static void startMainWindowAuto(){
        window = new MainWindow();
        window.setup(firstLaunch || settings.hasVersionChanged());
    }
    
    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    
    public static boolean isOSX(){
        return System.getProperty("os.name").toLowerCase().contains("mac os x");
    }
    
    public static boolean isLinux(){
        return !isWindows() && !isOSX();
    }
    
    
}
