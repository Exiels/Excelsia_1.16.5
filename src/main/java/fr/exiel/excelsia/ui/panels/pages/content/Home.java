package fr.exiel.excelsia.ui.panels.pages.content;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.exiel.excelsia.Launcher;
import fr.exiel.excelsia.game.MinecraftInfos;
import fr.exiel.excelsia.ui.PanelManager;
import fr.flowarg.flowio.FileUtils;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.CurseFileInfo;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class Home extends  ContentPanel {
    private final Saver saver = Launcher.getInstance().getSaver();
    GridPane boxPane = new GridPane();
    ProgressBar progressBar = new ProgressBar();
    Label stepLabel = new Label();
    Label fileLabel = new Label();
    boolean isDownloading = false;

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getStylesheetPath() {
        return "css/content/home.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setMinHeight(75);
        rowConstraints.setMaxHeight(75);
        this.layout.getRowConstraints().addAll(rowConstraints, new RowConstraints());
        boxPane.getStyleClass().add("box-pane");
        setCanTakeAllSize(boxPane);
        boxPane.setPadding(new Insets(20));
        this.layout.add(boxPane, 0, 0);
        this.layout.getStyleClass().add("home-layout");

        progressBar.getStyleClass().add("download-progress");
        stepLabel.getStyleClass().add("download-status");
        fileLabel.getStyleClass().add("download-status");

        progressBar.setTranslateY(-15);
        setCenterH(progressBar);
        setCanTakeAllWidth(progressBar);

        stepLabel.setTranslateY(5);
        setCenterH(stepLabel);
        setCanTakeAllSize(stepLabel);

        fileLabel.setTranslateY(20);
        setCenterH(fileLabel);
        setCanTakeAllSize(fileLabel);

        this.showPlayButton();
    }

    private void showPlayButton() {
        boxPane.getChildren().clear();
        Button playBtn = new Button("Jouer");
        FontAwesomeIconView playIcon = new FontAwesomeIconView(FontAwesomeIcon.GAMEPAD);
        playIcon.getStyleClass().add("play-icon");
        setCanTakeAllSize(playBtn);
        setCenterH(playBtn);
        setCenterV(playBtn);
        playBtn.getStyleClass().add("play-btn");
        playBtn.setGraphic(playIcon);
        playBtn.setOnMouseClicked(e -> this.play());
        boxPane.getChildren().add(playBtn);
    }

    private void play() {
        isDownloading = true;
        boxPane.getChildren().clear();
        setProgress(0, 0);
        boxPane.getChildren().addAll(progressBar, stepLabel, fileLabel);

        Platform.runLater(() -> new Thread(this::update).start());
    }

    public void updateLauncherForge() {
        try {
            FileUtils.deleteDirectory(Paths.get(Launcher.getInstance().getLauncherDir().toString() + "\\mods"));
            FileUtils.deleteDirectory(Paths.get(Launcher.getInstance().getLauncherDir().toString() + "\\assets"));
            FileUtils.deleteDirectory(Paths.get(Launcher.getInstance().getLauncherDir().toString() + "\\libraries"));
            FileUtils.deleteDirectory(Paths.get(Launcher.getInstance().getLauncherDir().toString() + "\\natives"));
            File file1 = new File(Launcher.getInstance().getLauncherDir().toString() + "\\client.jar");
            file1.delete();
        } catch (IOException e) {
            this.logger.err(e + " : LauncherModsDir not found.");
            return;
        }
        saver.set("updForge", "false");
        saver.set("md5", "0");
    }

    public void updateLauncherMods() {
        try {
            FileUtils.deleteDirectory(Paths.get(Launcher.getInstance().getLauncherDir().toString() + "\\mods"));
        } catch (IOException e) {
            this.logger.err(e + " : LauncherModsDir not found.");
            return;
        }
        saver.set("updMods", "false");
        saver.set("md5", "0");
    }

    private int checkLauncherOptions(int val) {
        String jsonString;
        try {
            URL url = new URL(MinecraftInfos.LAUNCHER_OPTIONS_URL);
            InputStream is = url.openStream();
            jsonString = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            this.logger.err(e + " : MD5 Update file not found.");
            return 0;
        }
        JSONObject obj = new JSONObject(jsonString);
        String updMods = obj.getString("updMods");
        String updForge = obj.getString("updForge");
        String localUpdMods = saver.get("updMods");
        String localUpdForge = saver.get("updForge");
        if (val == 1) {
            if (Objects.equals(updForge, "true")) {
                this.logger.info("Update Forge");
                updateLauncherForge();
            } else if (Objects.equals(updMods, "true")) {
                this.logger.info("Update Mods");
                updateLauncherMods();
            }
        } else {
            if (Objects.equals(localUpdMods, "true")){
                this.logger.info("Update Mods by Client");
                updateLauncherMods();
                return 1;
            } else if (Objects.equals(localUpdForge, "true")) {
                this.logger.info("Update Forge by Client");
                updateLauncherForge();
                return 1;
            }
        }
        return 0;
    }

    private Boolean checkUpdate() {
        String onlineMD5;
        try {
            URL url = new URL(MinecraftInfos.CURSE_MODS_LIST_URL);
            InputStream is = url.openStream();
            onlineMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        } catch (IOException e) {
            this.logger.err(e + " : MD5 Update file not found.");
            return false;
        }
        int val = checkLauncherOptions(0);
        if (val == 1) {
            return true;
        }
        String actualMD5 = saver.get("md5");
        String findMods = saver.get("findMods");
        this.logger.info("OnlineMD5 : " + onlineMD5);
        this.logger.info("ActualMD5 : " + actualMD5);
        if (Objects.equals(findMods, "true")) {
            saver.set("findMods", "false");
            return true;
        } else if (onlineMD5.equals(actualMD5))
            return false;
        checkLauncherOptions(1);
        saver.set("md5", onlineMD5);
        return true;
    }

    public void update() {
        AbstractForgeVersion forge;
        IProgressCallback callback = new IProgressCallback() {
            private  final DecimalFormat decimalFormat = new DecimalFormat("#.#");
            private String stepTxt = "";
            private String percentTxt = "0.0%";

            @Override
            public void step(Step step) {
                Platform.runLater(() -> {
                    stepTxt = StepInfo.valueOf(step.name()).getDetails();
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                });
            }

            @Override
            public void update(DownloadList.DownloadInfo info) {
                Platform.runLater(() -> {
                    percentTxt = decimalFormat.format(info.getDownloadedFiles() * 100.d / info.getTotalToDownloadFiles()) + "%";
                    setStatus(String.format("%s, (%s)", stepTxt, percentTxt));
                    setProgress(info.getDownloadedFiles(), info.getTotalToDownloadFiles());
                });
            }

            @Override
            public void onFileDownloaded(Path path) {
                Platform.runLater(() -> {
                    String p = path.toString();
                    fileLabel.setText("..." + p.replace(Launcher.getInstance().getLauncherDir().toFile().getAbsolutePath(), ""));
                });
            }
        };

        try {
            final VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
                    .withName(MinecraftInfos.GAME_VERSION)
                    .withVersionType(MinecraftInfos.VERSION_TYPE)
                    .build();

            List<CurseFileInfo> curseMods = CurseFileInfo.getFilesFromJson(MinecraftInfos.CURSE_MODS_LIST_URL);

            final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
                    .withSilentRead(false)
                    .build();

            if (!checkUpdate()) {
                forge = new ForgeVersionBuilder(MinecraftInfos.FORGE_VERSION_TYPE)
                        .withForgeVersion(MinecraftInfos.FORGE_VERSION)
                        .build();
            } else {
                forge = new ForgeVersionBuilder(MinecraftInfos.FORGE_VERSION_TYPE)
                        .withForgeVersion(MinecraftInfos.FORGE_VERSION)
                        .withCurseMods(curseMods)
                        .build();
            }

            final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
                    .withVanillaVersion(vanillaVersion)
                    .withForgeVersion(forge)
                    .withLogger(Launcher.getInstance().getLogger())
                    .withProgressCallback(callback)
                    .withUpdaterOptions(options)
                    .build();

            updater.update(Launcher.getInstance().getLauncherDir());
            this.startGame(updater.getVanillaVersion().getName());
        } catch (Exception exception) {
            Launcher.getInstance().getLogger().err(exception.toString());
            exception.printStackTrace();
            Platform.runLater(() -> panelManager.getStage().show());
        }
    }

    public void startGame(String gameVersion) {
        GameInfos infos = new GameInfos(
                MinecraftInfos.SERVER_NAME,
                true,
                new GameVersion(gameVersion, MinecraftInfos.OLL_GAME_TYPE.setNFVD(MinecraftInfos.OLL_FORGE_DISCRIMINATOR)),
                new GameTweak[]{}
        );

        try {
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, Launcher.getInstance().getAuthInfos());
            profile.getVmArgs().add(this.getRamArgsFromSaver());
            ExternalLauncher launcher = new ExternalLauncher(profile);

            Platform.runLater(() -> panelManager.getStage().hide());

            Process p = launcher.launch();

            Platform.runLater(() -> {
                try {
                    p.waitFor();
                    Platform.exit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            Launcher.getInstance().getLogger().err(exception.toString());
        }
    }

    public String getRamArgsFromSaver() {
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

        return "-Xmx" + val + "M";
    }

    public void setStatus(String status) {
        this.stepLabel.setText(status);
    }

    public void setProgress(double current, double max) {
        this.progressBar.setProgress(current / max);
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public enum StepInfo {
        INTEGRATION("Intégration..."),
        READ("Lecture du fichier json..."),
        DL_LIBS("Téléchargement des libraries..."),
        DL_ASSETS("Téléchargement des ressources..."),
        EXTRACT_NATIVES("Extraction des natives..."),
        FORGE("Installation de forge..."),
        FABRIC("Installation de fabric..."),
        MODS("Téléchargement des mods..."),
        EXTERNAL_FILES("Téléchargement des fichier externes..."),
        POST_EXECUTIONS("Exécution post-installation..."),
        END("Fini !");
        String details;

        StepInfo(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }
}
