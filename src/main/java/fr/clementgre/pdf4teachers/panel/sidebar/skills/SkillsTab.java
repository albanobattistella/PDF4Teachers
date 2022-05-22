/*
 * Copyright (c) 2022. Clément Grennerat
 * All rights reserved. You must refer to the licence Apache 2.
 */

package fr.clementgre.pdf4teachers.panel.sidebar.skills;

import fr.clementgre.pdf4teachers.interfaces.windows.language.TR;
import fr.clementgre.pdf4teachers.panel.sidebar.SideTab;
import fr.clementgre.pdf4teachers.panel.sidebar.skills.data.SkillsAssessment;
import fr.clementgre.pdf4teachers.utils.image.ImageUtils;
import fr.clementgre.pdf4teachers.utils.panes.PaneUtils;
import fr.clementgre.pdf4teachers.utils.svg.SVGPathIcons;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/* INFO DEVOIR

----- Compétences : ----- (liste dans l'application)

ID / Code / Nom

----- RÉSULTATS : ----- (Enregistrer par défaut)

Sigle / Légende / Touche Clavier / Image / ID

Et en plus les résultats par défaut :
    Signe / Légende / Touche clavier (/ Image à partir du Signe / Pas D'ID)
    AB pour "absent" (touche A)
    DI pour "dispensé" (touche D)
    NE pour "non évalué" (touche E)
    NF pour "non fait" (touche F)
    NN pour "non noté" (touche N)
    NR pour "non rendu" (touche R)
    

----- ÉLÈVES -----

NOM / ID


----- INFO DE BASE -----

Classe / Date / Nom de l'eval

*/

/* INFO ÉDITIONS

- Info élément de grille résultat (position, taille).
- Résultats dans chaque compétence :
    - ID /

*/

public class SkillsTab extends SideTab {
    
    
    private ArrayList<SkillsAssessment> assessments = new ArrayList<>();
    
    private final VBox pane = new VBox();
    private final HBox optionPane = new HBox();
    private final ListView<String> listView = new ListView<>();
    
    private final Button settings = setupButton(SVGPathIcons.LIST, TR.tr("skillsTab.settings.tooltip"), e -> {
        new SkillsAssessmentWindow().show();
    });
    private final Button link = setupButton(SVGPathIcons.SAVE_LITE, TR.tr("skillsTab.link.tooltip"), e -> {
        // TODO: copy grade scale/settings to others pdf of the list and same folder.
    });
    private final Button export = setupButton(SVGPathIcons.EXPORT, TR.tr("skillsTab.export.tooltip"), e -> {
        // TODO: export to csv (SaCoche or Classic).
    });
    
    public SkillsTab(){
        super("skills", SVGPathIcons.A_CIRCLED, 0, 27, new int[]{1, 1});
        
        SkillsAssessment.setup();
        
        setup();
        setContent(pane);
    }
    
    private void setup(){

        optionPane.setStyle("-fx-padding: 5 0 5 0;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        optionPane.getChildren().addAll(spacer, settings, link, export);
        
        // TODO: setup listView for showing skills.
    
        pane.getChildren().addAll(optionPane, listView);
    }
    
    private Button setupButton(String iconPath, String tooltip, EventHandler<ActionEvent> onAction){
        Button button = new Button();
        PaneUtils.setHBoxPosition(button, 45, 35, 0);
        button.setCursor(Cursor.HAND);
        button.setGraphic(SVGPathIcons.generateImage(iconPath, "black", 0, 26, 0, ImageUtils.defaultDarkColorAdjust));
        button.setTooltip(PaneUtils.genWrappedToolTip(tooltip));
        button.setOnAction(onAction);
        return button;
    }
    
    
    public ArrayList<SkillsAssessment> getAssessments(){
        return assessments;
    }
    public void setAssessments(ArrayList<SkillsAssessment> assessments){
        this.assessments = assessments;
    }
}
