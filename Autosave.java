
package com.mycompany.mavenproject1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * This saves backup copies of drawing tabs.
 */
public class AutoSave {

    private static final int AUTOSAVE_INTERVAL_SECONDS = 60;

    private final Map<Tab, StackPane> drawingAreas;
    private final FileManager fileManager;
    private final Timeline autosaveTimer;

    private final File autosaveDirectory = new File(
        System.getProperty("user.home"),
        ".joespaint/autosave"
    );

    public AutoSave(
            Map<Tab, StackPane> drawingAreas,
            FileManager fileManager) {

        this.drawingAreas = drawingAreas;
        this.fileManager = fileManager;

        autosaveTimer = new Timeline(
            new KeyFrame(
                Duration.seconds(AUTOSAVE_INTERVAL_SECONDS),
                event -> autosaveAllTabs()
            )
        );

        autosaveTimer.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * This starts the repeating autosave timer.
     */
    public void start() {
        autosaveTimer.play();
    }

    /**
     * STOP!
     */
    public void stop() {
        autosaveTimer.stop();
    }

    /**
     * This saves a backup PNG for every tab currently open.
     */
    public void autosaveAllTabs() {

        try {
            Files.createDirectories(autosaveDirectory.toPath());
        } catch (IOException ex) {
            System.err.println(
                "Could not create autosave directory: "
                + ex.getMessage()
            );
            return;
        }

        int tabNumber = 1;

        for (Map.Entry<Tab, StackPane> entry
                : drawingAreas.entrySet()) {

            StackPane drawingArea = entry.getValue();

            if (drawingArea == null) {
                continue;
            }

            File backupFile = new File(
                autosaveDirectory,
                "tab-" + tabNumber + ".png"
            );

            boolean saved = fileManager.saveImage(
                drawingArea,
                backupFile,
                "png"
            );

            if (saved) {
                System.out.println(
                    "Autosaved: " + backupFile.getAbsolutePath()
                );
            }

            tabNumber++;
        }
    }
}
