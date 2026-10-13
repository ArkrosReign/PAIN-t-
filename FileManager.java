package com.mycompany.mavenproject1;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * FM handles opening and saving image files for Joes' Pain(t).
 *
 * FM class keeps file input/output code separate from the main
 * application class. It supports PNG, JPG/JPEG, and BMP images.
 */
public class FileManager {

    /**
     * this creates a file chooser for opening supported image files.
     *
     * @return a configured open-file chooser
     */
    public FileChooser createOpenFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "PNG Images", "*.png"
            ),
            new FileChooser.ExtensionFilter(
                "JPG Images", "*.jpg", "*.jpeg"
            ),
            new FileChooser.ExtensionFilter(
                "BMP Images", "*.bmp"
            )
        );
        return fileChooser;
    }

    /**
     * this opens a file chooser and lets the user select an image.
     *
     * @param stage the application window used by the chooser
     * @return the selected file, or null if the user cancels
     */
    public File chooseOpenFile(Stage stage) {
        return createOpenFileChooser().showOpenDialog(stage);
    }

    /**
     * this creates a file chooser for saving an image.
     *
     * @return a configured save-file chooser
     */
    public FileChooser createSaveFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "PNG Images", "*.png"
            ),
            new FileChooser.ExtensionFilter(
                "JPG Images", "*.jpg", "*.jpeg"
            ),
            new FileChooser.ExtensionFilter(
                "BMP Images", "*.bmp"
            )
        );
        return fileChooser;
    }

    /**
     * this loads an image from a file.
     *
     * @param file the image file to load
     * @return the loaded image, or null if loading fails
     */
    public Image openImage(File file) {
        if (file == null) {
            return null;
        }

        return new Image(file.toURI().toString());
    }

    /**
     * FM saves a drawing area to an image file.
     *
     * @param drawingArea the drawing area containing the image
     * @param file the destination file
     * @param format the image format, such as png, jpg, or bmp
     * @throws IOException if the image cannot be written
     */
    public boolean saveImage(
        StackPane drawingArea,
        File file,
        String format
    ) {
        if (drawingArea == null || file == null || format == null) {
            return false;
        }

        try {
            WritableImage snapshot = drawingArea.snapshot(
                new SnapshotParameters(),
                null
            );

            BufferedImage bufferedImage =
                SwingFXUtils.fromFXImage(snapshot, null);

            String normalizedFormat = format.toLowerCase();
            if (normalizedFormat.equals("jpeg")) {
                normalizedFormat = "jpg";
            }

            if (normalizedFormat.equals("jpg")) {
                BufferedImage rgbImage = new BufferedImage(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
                );
                java.awt.Graphics2D graphics = rgbImage.createGraphics();
                graphics.drawImage(bufferedImage, 0, 0, java.awt.Color.WHITE, null);
                graphics.dispose();
                bufferedImage = rgbImage;
            }

            boolean written = ImageIO.write(
                bufferedImage,
                normalizedFormat,
                file
            );

            if (!written) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
                );
                alert.setTitle("Save Error");
                alert.setHeaderText("Unsupported image format");
                alert.setContentText("JoesPaint could not save this image as "
                    + normalizedFormat.toUpperCase() + ".");
                alert.showAndWait();
            }
            return written;
        } catch (IOException ex) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Save Error");
            alert.setHeaderText("Could not save image");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    /**
     * FM verifies the destination filename uses the extension for the selected format.
     * For example, saving as JPG changes "drawing.png" to "drawing.jpg".
     *
     * @param file selected destination file
     * @param format selected image format
     * @return a destination file with the matching extension
     */
    public File ensureFileExtension(File file, String format) {
        if (file == null || format == null) {
            return file;
        }

        String normalizedFormat = format.toLowerCase();
        if (normalizedFormat.equals("jpeg")) {
            normalizedFormat = "jpg";
        }
        if (!normalizedFormat.equals("png")
                && !normalizedFormat.equals("jpg")
                && !normalizedFormat.equals("bmp")) {
            normalizedFormat = "png";
        }

        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }

        return new File(file.getParentFile(), name + "." + normalizedFormat);
    }

    /**
     * FM determines an image format from a selected file.
     *
     * @param file the image file
     * @return the format name, defaulting to png when no supported
     *         extension is found
     */
    public String getFormatFromFile(File file) {
        if (file != null) {
            String name = file.getName().toLowerCase();

            if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
                return "jpg";
            }

            if (name.endsWith(".bmp")) {
                return "bmp";
            }
        }

        return "png";
    }

    /**
     * FM determines the image format represented by a file chooser filter.
     *
     * @param filter the selected file chooser extension filter
     * @return the corresponding format name
     */
    public String getFormatFromFilter(
        FileChooser.ExtensionFilter filter
    ) {
        if (filter != null) {
            String description = filter.getDescription();

            if ("JPG Images".equals(description)) {
                return "jpg";
            }

            if ("BMP Images".equals(description)) {
                return "bmp";
            }
        }

        return "png";
    }
}
