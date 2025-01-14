package fr.clementgre.pdf4teachers.interfaces.windows.gallery;

import fr.clementgre.pdf4teachers.Main;
import fr.clementgre.pdf4teachers.components.HBoxSpacer;
import fr.clementgre.pdf4teachers.components.SliderWithoutPopup;
import fr.clementgre.pdf4teachers.interfaces.autotips.AutoTipsManager;
import fr.clementgre.pdf4teachers.interfaces.windows.MainWindow;
import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.panel.sidebar.paint.gridviewfactory.ImageGridElement;
import fr.clementgre.pdf4teachers.panel.sidebar.paint.gridviewfactory.ImageGridView;
import fr.clementgre.pdf4teachers.panel.sidebar.paint.gridviewfactory.ShapesGridView;
import fr.clementgre.pdf4teachers.utils.panes.PaneUtils;
import fr.clementgre.pdf4teachers.utils.PlatformUtils;
import fr.clementgre.pdf4teachers.utils.image.ImageUtils;
import fr.clementgre.pdf4teachers.utils.svg.SVGPathIcons;
import fr.clementgre.pdf4teachers.utils.style.Style;
import fr.clementgre.pdf4teachers.utils.style.StyleManager;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryWindow extends Stage{
    
    private final VBox root = new VBox();
    
    private final HBox settings = new HBox();
    private final GridPane sortPanel = new GridPane();
    public final ComboBox<String> filter = new ComboBox<>();
    protected final SliderWithoutPopup zoomSlider = new SliderWithoutPopup(50, 500, 150);
    private final Button reload = new Button();
    private final Label emptyGalleryLabel = new Label(TR.tr("galleryWindow.noImagesMessage"));
    private final HBox messageContainer = new HBox(emptyGalleryLabel);
    
    private final ImageGridView list = new ImageGridView(false,500, zoomSlider, true);
    
    public GalleryWindow(){
        
        Scene scene = new Scene(root);
    
        getIcons().add(new Image(getClass().getResource("/logo.png") + ""));
        setWidth(1200*Main.settings.zoom.getValue());
        setHeight(800*Main.settings.zoom.getValue());
        setMinWidth(700*Main.settings.zoom.getValue());
        setMinHeight(400*Main.settings.zoom.getValue());
        Main.window.centerWindowIntoMe(this);
        setTitle(TR.tr("galleryWindow.title"));
        setScene(scene);
        StyleManager.putStyle(scene, Style.DEFAULT);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        PaneUtils.setupScaling(root, true, false);
        scene.setFill(Color.web("#252525"));
        
        setOnCloseRequest((e) -> {
            AutoTipsManager.hideAll();
            MainWindow.paintTab.galleryWindow = null;
        });
        
        
        setup();
        Main.window.centerWindowIntoMe(this);
        MainWindow.preventWindowOverflowScreen(this);
        show();
        PlatformUtils.runLaterOnUIThread(1000, () -> {
            AutoTipsManager.showByAction("opengallery", this);
        });

    }
    
    private void setupSettings(){
        
        list.setupSortManager(sortPanel, ShapesGridView.SORT_FOLDER, ShapesGridView.SORT_FOLDER, ShapesGridView.SORT_NAME, ShapesGridView.SORT_FILE_EDIT_TIME, ShapesGridView.SORT_SIZE, ShapesGridView.SORT_USE, ShapesGridView.SORT_LAST_USE);
        VBox.setVgrow(list, Priority.ALWAYS);
        root.setFillWidth(true);
        
        filter.setCellFactory(param -> new DirFilterListCell(this));
        filter.setVisibleRowCount(10);
        updateComboBoxItems();
        filter.getSelectionModel().select(0);

        filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) return;
            if(newValue.equals(TR.tr("galleryWindow.filterAndEditCombo.addDirectoryButton")) && !newValue.equals(oldValue)){
                filter.getSelectionModel().select(oldValue);
                return;
            }

            if(newValue.equals(TR.tr("galleryWindow.filterAndEditCombo.everywhere"))){
                list.setFilterType(null);
            }else{
                list.setFilterType(newValue);
            }

        });
        filter.setStyle("-fx-padding: 0 5;");

        PaneUtils.setHBoxPosition(sortPanel, 0, 26, 0);
        PaneUtils.setHBoxPosition(zoomSlider, 0, 26, 0);
        PaneUtils.setHBoxPosition(filter, 0, 26, 0);
        PaneUtils.setHBoxPosition(reload, 26, 26, 0);
        reload.setGraphic(SVGPathIcons.generateImage(SVGPathIcons.REDO, "black", 0, 16, 16,  ImageUtils.defaultDarkColorAdjust));
        reload.setTooltip(PaneUtils.genWrappedToolTip(TR.tr("galleryWindow.reloadButton.tooltip")));
        
        reload.setOnAction((e) -> {
            list.setItems(Collections.emptyList());
            list.addItems(getImages(list));
            reloadImageList();
            updateComboBoxItems();
        });
        
        settings.setSpacing(10);
        settings.setPadding(new Insets(0, 20, 0, 20));
        settings.getChildren().addAll(sortPanel, zoomSlider, new HBoxSpacer(), filter, reload);
    }
    private void setup(){
        setupSettings();
    
        emptyGalleryLabel.setStyle("-fx-font: 18 \"Noto Sans KR\";");
        emptyGalleryLabel.setTextAlignment(TextAlignment.CENTER);
        messageContainer.setAlignment(Pos.CENTER);
        VBox.setVgrow(messageContainer, Priority.ALWAYS);
        list.getItems().addListener((ListChangeListener<ImageGridElement>) c -> {
            updateMessage();
        });
        
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(settings, list);
        list.addItems(getImages(list));
        updateMessage();
    }
    
    private void updateMessage(){
        if(list.getAllItems().isEmpty()){
            root.getChildren().add(messageContainer);
            list.setMaxHeight(0);
        }else{
            root.getChildren().remove(messageContainer);
            list.setMaxHeight(Double.MAX_VALUE);
        }
    }
    
    
    public void updateStyle(){
        StyleManager.putStyle(getScene(), Style.DEFAULT);
        list.getSortManager().updateGraphics();
    }
    void updateComboBoxItems(){
        String selected = filter.getSelectionModel().getSelectedItem();
        List<String> items = GalleryManager.getSavePaths();
        items.sort(String::compareTo);
        items.add(0, TR.tr("galleryWindow.filterAndEditCombo.favourites"));
        items.add(0, TR.tr("galleryWindow.filterAndEditCombo.everywhere"));
        items.add(TR.tr("galleryWindow.filterAndEditCombo.addDirectoryButton"));
        filter.getItems().setAll(items);
        if(filter.getItems().contains(selected)) filter.getSelectionModel().select(selected);
        else filter.getSelectionModel().select(TR.tr("galleryWindow.filterAndEditCombo.everywhere"));
    }
    public void reloadImageList(){
        list.editImages(getImages(list));
        if(MainWindow.paintTab.gallery.isLoaded()){
            MainWindow.paintTab.gallery.reloadGalleryImageList();
        }
    }

    public List<ImageGridElement> getListItems(){
        return list.getAllItems();
    }
    public ShapesGridView<ImageGridElement> getList(){
        return list;
    }
    
    public static List<ImageGridElement> getImages(ImageGridView gridView){
        return GalleryManager.getImages().stream().map((img) -> new ImageGridElement(img.getImageId(), gridView)).collect(Collectors.toList());
    }
}
