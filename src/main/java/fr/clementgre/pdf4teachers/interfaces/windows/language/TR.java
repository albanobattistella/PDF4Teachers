package fr.clementgre.pdf4teachers.interfaces.windows.language;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.datasaving.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class TR{
    
    public static Locale locale;
    public static ResourceBundle bundle;
    
    public static Locale ENLocale;
    public static ResourceBundle ENBundle;
    private static HashMap<String, Object> languages = getLanguagesDefaultConfig();
    
    public static void setup(){
        
        // Translate translations from 1.2.0- naming system
        if(Main.settings.language.getValue().equals("Français France (Defaut)")){
            Main.settings.language.setValue("fr_fr");
            Main.settings.saveSettings();
        }else if(Main.settings.language.getValue().equals("English US")){
            Main.settings.language.setValue("en_us");
            Main.settings.saveSettings();
        }
        
        // Delete Old files
        if(Main.settings.getSettingsVersionCode().startsWith("1.2") || Main.settings.getSettingsVersionCode().startsWith("1.1") || Main.settings.getSettingsVersionCode().startsWith("1.0")){
            for(File file : Objects.requireNonNull(new File(Main.dataFolder + "translations").listFiles())) file.delete();
        }
        // Copy files if version has changed
        copyFiles(Main.settings.hasVersionChanged() || Main.DEBUG);
        
        // Load locales
        
        ENLocale = new Locale("en", "us");
        ENBundle = getBundleByLocale(ENLocale);
        
        locale = new Locale(getSettingsLocaleLanguage(), getSettingsLocaleCountry());
        bundle = getBundleByLocale(locale);
        
    }
    
    public static ResourceBundle getBundleByLocale(Locale locale){
        if(Main.TRANSLATIONS_IN_CODE){ // Load the locale from ressource
            return getBundleByLocaleInCode(locale);
        }else{
            try{ // Load the locale from user files
                if(Main.DEBUG) System.out.println("Loading locale " + locale.toString() + " from user files...");
                FileInputStream fis = new FileInputStream(getLocaleFile(locale));
                return new PropertyResourceBundle(fis);
            }catch(Exception e){
                System.err.println("Unable to load translation in user files, trying to load from ressource...");
                e.printStackTrace();
                return getBundleByLocaleInCode(locale);
            }
        }
    }
    
    public static ResourceBundle getBundleByLocaleInCode(Locale locale){
        try{ // Load the locale from ressource
            if(Main.DEBUG) System.out.println("Loading locale " + locale.toString() + " from code...");
            return new PropertyResourceBundle(getCodeLocaleFile(locale));
        }catch(IOException e2){
            System.err.println("Unable to load translation in code");
            e2.printStackTrace();
            
            if(locale != ENLocale){ // Load the EN locale
                System.err.println("Trying to load the English locale from user files...");
                return getBundleByLocale(ENLocale);
            }else{
                System.err.println("Return empty ressourceBundle...");
                return new ResourceBundle(){
                    @Override
                    protected Object handleGetObject(String key){
                        return null;
                    }
                    
                    @Override
                    public Enumeration<String> getKeys(){
                        return Collections.emptyEnumeration();
                    }
                };
            }
        }
    }
    
    public static String getLanguageFromComputerLanguage(){
        String language = System.getProperty("user.language").toLowerCase();
        //String country = System.getProperty("user.country").toLowerCase();
        switch(language){
            case "fr" -> {
                return "fr_fr";
            }
            case "en" -> {
                return "en_us";
            }
            case "it" -> {
                return "it_it";
            }
        }
        return null;
    }
    
    public static String getSettingsLocaleLanguage(){
        if(Main.settings.language.getValue().split("[-_]").length < 2) return "en";
        return Main.settings.language.getValue().split("[-_]")[0].toLowerCase();
    }
    
    public static String getSettingsLocaleCountry(){
        if(Main.settings.language.getValue().split("[-_]").length < 2) return "us";
        return Main.settings.language.getValue().split("[-_]")[1].toLowerCase();
    }
    
    public static File getLocaleFile(Locale locale){
        return new File(Main.dataFolder + "translations" + File.separator + getLocaleString(locale) + ".properties");
    }
    
    public static InputStream getCodeLocaleFile(Locale locale){
        return TR.class.getResourceAsStream("/translations/" + getLocaleString(locale) + ".properties");
    }
    
    public static String getLocaleString(Locale locale){
        return locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toLowerCase();
    }
    
    public static void updateLocale(){
        locale = new Locale(getSettingsLocaleLanguage(), getSettingsLocaleCountry());
        bundle = getBundleByLocale(locale);
    }
    
    // translate with arguments
    public static String tr(String key, ResourceBundle bundle, boolean trEn){
        if(!bundle.containsKey(key) || bundle.getString(key).isBlank()){
            if(trEn) return tr(key, ENBundle, false);
            else return key;
        }
        return bundle.getString(key);
    }
    
    public static String tr(String key, ResourceBundle bundle, boolean trEn, String... args){
        if(!bundle.containsKey(key) || bundle.getString(key).isBlank()){
            if(!bundle.containsKey(key) || bundle.getString(key).isBlank()){
                if(trEn) return tr(key, ENBundle, false, args);
                else return key + " {" + String.join(", ", args) + "}";
            }
        }
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(locale);
        
        formatter.applyPattern(bundle.getString(key).replace("'", "''"));
        return formatter.format(args);
    }
    
    // return translation
    public static String tr(String key){
        return tr(key, bundle, true);
    }
    
    public static String tr(String key, String... args){
        return tr(key, bundle, true, args);
    }
    
    public static String tr(String key, int... args){
        return tr(key, bundle, true, Arrays.stream(args).mapToObj(String::valueOf).toArray(String[]::new));
    }
    
    public static String trO(String text){
        return "[" + text + "]";
    }
    
    
    //////////////////////////////////////////////////////////
    ////////////// Language config system ////////////////////
    //////////////////////////////////////////////////////////
    
    public static String getLanguageName(String shortName){
        HashMap<String, Object> languages = getLanguagesConfig();
        for(Map.Entry<String, Object> language : languages.entrySet()){
            if(shortName.equals(language.getKey())){
                HashMap<?, ?> data = (HashMap<?, ?>) language.getValue();
                return (String) data.get("name");
            }
        }
        return shortName;
    }
    
    public static int getLanguageVersion(String shortName){
        HashMap<String, Object> languages = getLanguagesConfig();
        for(Map.Entry<String, Object> language : languages.entrySet()){
            if(shortName.equals(language.getKey())){
                HashMap<?, ?> data = (HashMap<?, ?>) language.getValue();
                return (int) data.get("version");
            }
        }
        return 0;
    }
    
    public static void copyFiles(boolean force){
        try{
            File translationsDir = new File(Main.dataFolder + "translations" + File.separator);
            translationsDir.mkdirs();
            
            for(String name : getLanguagesDefaultConfig().keySet()){
                copyFile(name + ".properties", force);
                copyFile(name + ".pdf", force);
                copyFile(name + ".png", force);
                copyFile(name + ".odt", force);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private static void copyFile(String fileName, boolean force) throws IOException{
        if(LanguageWindow.class.getResource("/translations/" + fileName) == null) return;
        
        File dest = new File(Main.dataFolder + "translations" + File.separator + fileName);
        if(!dest.exists() || force){
            InputStream res = LanguageWindow.class.getResourceAsStream("/translations/" + fileName);
            Files.copy(res, dest.getAbsoluteFile().toPath(), REPLACE_EXISTING);
        }
    }
    
    public static File getDocFile(){
        File doc = new File(Main.dataFolder + "translations" + File.separator + Main.settings.language.getValue() + ".pdf");
        if(!doc.exists()){
            return new File(Main.dataFolder + "translations" + File.separator + "en_us.pdf");
        }
        return doc;
    }
    
    public static HashMap<String, Object> getLanguagesConfig(){
        return languages;
    }
    
    public static void loadLanguagesConfig(HashMap<String, Object> data){
        // if the version has changed, keep the default values
        if(Main.settings.hasVersionChanged()) return;
        
        // Add default languages if they were deleted
        for(Map.Entry<String, Object> language : getLanguagesDefaultConfig().entrySet()){
            if(!data.containsKey(language.getKey())) data.put(language.getKey(), language.getValue());
        }
        languages = data;
    }
    
    public static HashMap<String, Object> getLanguagesDefaultConfig(){
        HashMap<String, Object> data = new HashMap<>();
        
        Config.set(data, "fr_fr.version", 0);
        Config.set(data, "fr_fr.name", "Français France");
        
        Config.set(data, "en_us.version", 0);
        Config.set(data, "en_us.name", "English US");
        
        Config.set(data, "it_it.version", 0);
        Config.set(data, "it_it.name", "Italiano Italia");
        
        return data;
    }
    
    public static void addLanguageToConfig(String name, String displayName, int version){
        Config.set(languages, name + ".version", version);
        Config.set(languages, name + ".name", displayName);
    }
}
