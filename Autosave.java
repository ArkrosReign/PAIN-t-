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
 * Saves backup copies of drawing tabs and optionally
 * displays desktop notifications when autosaving completes.
 */
public class AutoSave {

    private static final int AUTOSAVE_INTERVAL_SECONDS = 30;

    private final Map<Tab, StackPane> drawingAreas;
    private final FileManager fileManager;
    private final Timeline autosaveTimer;

    private boolean notificationsEnabled = true;

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
     * Starts the repeating autosave timer.
     */
    public void start() {
        autosaveTimer.play();
    }

    /**
     * Stops the autosave timer.
     */
    public void stop() {
        autosaveTimer.stop();
    }

    /**
     * Enables or disables desktop autosave notifications.
     * This does not disable autosaving.
     */
    public void setNotificationsEnabled(boolean enabled) {
        notificationsEnabled = enabled;
    }

    /**
     * Saves a backup PNG for every tab currently open.
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
        int savedCount = 0;

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
                savedCount++;
            }

            tabNumber++;
        }

        if (notificationsEnabled && savedCount > 0) {
            showDesktopNotification(
                "Autosave complete",
                savedCount + (savedCount == 1
                    ? " drawing backup saved."
                    : " drawing backups saved.")
            );
        }
    }

    /**
     * Showcases a desktop notification through the operating system.
     */
    private void showDesktopNotification(String title, String message) {

        try {
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println(
                    "Desktop notifications are not supported on this system."
                );
                return;
            }

            java.awt.SystemTray tray =
                java.awt.SystemTray.getSystemTray();

            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(
                createNotificationImage(),
                "JoesPaint"
            );

            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);

            trayIcon.displayMessage(
                title,
                message,
                java.awt.TrayIcon.MessageType.INFO
            );

            javax.swing.Timer removalTimer = new javax.swing.Timer(
                5000,
                event -> tray.remove(trayIcon)
            );

            removalTimer.setRepeats(false);
            removalTimer.start();

        } catch (Exception ex) {
            System.err.println(
                "Could not display autosave notification: "
                + ex.getMessage()
            );
        }
    }

    /**
     * Creates a icon for the operating system notification.
     */
    private java.awt.Image createNotificationImage() {

        java.awt.image.BufferedImage image =
            new java.awt.image.BufferedImage(
                16,
                16,
                java.awt.image.BufferedImage.TYPE_INT_ARGB
            );

        java.awt.Graphics2D graphics = image.createGraphics();

        try {
            graphics.setColor(new java.awt.Color(45, 120, 210));
            graphics.fillRoundRect(1, 1, 14, 14, 4, 4);

            graphics.setColor(java.awt.Color.WHITE);
            graphics.drawString("J", 4, 12);
        } finally {
            graphics.dispose();
        }

        return image;
    }
}
