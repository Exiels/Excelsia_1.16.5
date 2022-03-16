package fr.exiel.excelsia.ui.panels.pages.content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.exiel.excelsia.Launcher;
import fr.exiel.excelsia.ui.PanelManager;
import fr.exiel.excelsia.ui.panels.pages.App;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

public class Settings extends ContentPanel{
    private final Saver saver = Launcher.getInstance().getSaver();
    GridPane contentPane = new GridPane();

    @Override
    public String getName() {
        return "settings";
    }

    @Override
    public String getStylesheetPath() {
        return "css/content/settings.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background
        this.layout.getStyleClass().add("settings-layout");
        this.layout.setPadding(new Insets(40));
        setCanTakeAllSize(this.layout);

        // Content
        contentPane.getStyleClass().add("content-pane");
        setCanTakeAllSize(contentPane);
        this.layout.getChildren().add(contentPane);

        // Titre
        Label title = new Label("Paramètres");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 25f));
        title.getStyleClass().add("settings-title");
        setLeft(title);
        setCanTakeAllSize(title);
        setTop(title);
        title.setTextAlignment(TextAlignment.LEFT);
        title.setTranslateY(40d);
        title.setTranslateX(25d);
        contentPane.getChildren().add(title);

        // RAM
        Label ramLabel = new Label("Mémoire max");
        ramLabel.getStyleClass().add("settings-labels");
        setLeft(ramLabel);
        setCanTakeAllSize(ramLabel);
        setTop(ramLabel);
        ramLabel.setTextAlignment(TextAlignment.LEFT);
        ramLabel.setTranslateX(25d);
        ramLabel.setTranslateY(100d);
        contentPane.getChildren().add(ramLabel);

        // RAM Slider
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getStyleClass().add("ram-selector");
        for(int i = 512; i <= Math.ceil(memory.getTotal() / Math.pow(1024, 2)); i+=512) {
            comboBox.getItems().add(i/1024.0+" Go");
        }

        int val = 1024;
        try {
            if (saver.get("maxRam") != null) {
                val = Integer.parseInt(saver.get("maxRam"));
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException error) {
            saver.set("maxRam", String.valueOf(val));
            saver.save();
        }

        if (comboBox.getItems().contains(val/1024.0+" Go")) {
            comboBox.setValue(val / 1024.0 + " Go");
        } else {
            comboBox.setValue("1.0 Go");
        }

        setLeft(comboBox);
        setCanTakeAllSize(comboBox);
        setTop(comboBox);
        comboBox.setTranslateX(35d);
        comboBox.setTranslateY(130d);
        contentPane.getChildren().add(comboBox);

        // Mods Find Reset Label
        Label resetFindMods = new Label("Reset Mods Finder");
        resetFindMods.getStyleClass().add("settings-labels");
        setLeft(resetFindMods);
        setCanTakeAllSize(resetFindMods);
        setTop(resetFindMods);
        resetFindMods.setTextAlignment(TextAlignment.LEFT);
        resetFindMods.setTranslateX(24d);
        resetFindMods.setTranslateY(165d);
        contentPane.getChildren().add(resetFindMods);

        // Mods Find Reset Button
        Button resetModsFindBtn = new Button("Reset");
        resetModsFindBtn.getStyleClass().add("reset-mods");
        FontAwesomeIconView iconRst = new FontAwesomeIconView(FontAwesomeIcon.UNDO);
        resetModsFindBtn.setGraphic(iconRst);
        setLeft(resetModsFindBtn);
        setTop(resetModsFindBtn);
        setCanTakeAllSize(resetModsFindBtn);
        resetModsFindBtn.setTranslateX(35d);
        resetModsFindBtn.setTranslateY(190d);
        resetModsFindBtn.setOnMouseClicked(e -> {
            saver.set("findMods", "true");
            panelManager.showPanel(new App());
        });
        contentPane.getChildren().add(resetModsFindBtn);

        // Mods Reset Label
        Label resetMods = new Label("Reset Mods");
        resetMods.getStyleClass().add("settings-labels");
        setLeft(resetMods);
        setCanTakeAllSize(resetMods);
        setTop(resetMods);
        resetMods.setTextAlignment(TextAlignment.LEFT);
        resetMods.setTranslateX(24d);
        resetMods.setTranslateY(235d);
        contentPane.getChildren().add(resetMods);

        // Mods Reset Button
        Button resetModsBtn = new Button("Reset");
        resetModsBtn.getStyleClass().add("reset-mods");
        FontAwesomeIconView iconRst2 = new FontAwesomeIconView(FontAwesomeIcon.UNDO);
        resetModsBtn.setGraphic(iconRst2);
        setLeft(resetModsBtn);
        setTop(resetModsBtn);
        setCanTakeAllSize(resetModsBtn);
        resetModsBtn.setTranslateX(35d);
        resetModsBtn.setTranslateY(260d);
        resetModsBtn.setOnMouseClicked(e -> {
            saver.set("updMods", "true");
            panelManager.showPanel(new App());
        });
        contentPane.getChildren().add(resetModsBtn);

        // Forge Reset Label
        Label resetForge = new Label("Reset Forge");
        resetForge.getStyleClass().add("settings-labels");
        setLeft(resetForge);
        setCanTakeAllSize(resetForge);
        setTop(resetForge);
        resetForge.setTextAlignment(TextAlignment.LEFT);
        resetForge.setTranslateX(24d);
        resetForge.setTranslateY(305);
        contentPane.getChildren().add(resetForge);

        // Forge Reset Button
        Button resetForgeBtn = new Button("Reset");
        resetForgeBtn.getStyleClass().add("reset-mods");
        FontAwesomeIconView iconRst3 = new FontAwesomeIconView(FontAwesomeIcon.UNDO);
        resetForgeBtn.setGraphic(iconRst3);
        setLeft(resetForgeBtn);
        setTop(resetForgeBtn);
        setCanTakeAllSize(resetForgeBtn);
        resetForgeBtn.setTranslateX(35d);
        resetForgeBtn.setTranslateY(330d);
        resetForgeBtn.setOnMouseClicked(e -> {
            saver.set("updForge", "true");
            panelManager.showPanel(new App());
        });
        contentPane.getChildren().add(resetForgeBtn);

        /*
         * Save Button
         */
        Button saveBtn = new Button("Enregistrer");
        saveBtn.getStyleClass().add("save-btn");
        FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.SAVE);
        iconView.getStyleClass().add("save-icon");
        saveBtn.setGraphic(iconView);
        setCanTakeAllSize(saveBtn);
        setBottom(saveBtn);
        setCenterH(saveBtn);
        saveBtn.setOnMouseClicked(e -> {
            double _val = Double.parseDouble(comboBox.getValue().replace(" Go", ""));
            _val *= 1024;
            saver.set("maxRam", String.valueOf((int) _val));
            panelManager.showPanel(new App());
        });
        contentPane.getChildren().add(saveBtn);
    }
}