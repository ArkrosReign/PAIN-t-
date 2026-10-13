package com.mycompany.mavenproject1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Webserver { //wsp professor, you get to see me lose my sanity, ignore the crashing out comments

  //private declarations are for connecting stuff
    private HttpServer server;
    private final TabPane tabPane;
    private final Map<Tab, StackPane> tabDrawingAreas;

    public Webserver(
            TabPane tabPane,
            Map<Tab, StackPane> tabDrawingAreas) {
        this.tabPane = tabPane;
        this.tabDrawingAreas = tabDrawingAreas;
    }

    public void start() throws IOException {
        if (server != null) {
            return;
        }

        server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", 8080), 0);

        server.createContext("/", this::handleHome);
        server.createContext("/image", this::handleImage);
        server.start();

        System.out.println(
                "JoesPaint web server running at http://localhost:8080/");
    }

    private void handleHome(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, 405, "GET requests only.");
            return;
        }

        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>JoesPaint</title>
                    <style>
                        body {
                            font-family: sans-serif;
                            margin: 0;
                            padding: 24px;
                            text-align: center;
                            background: #f3f3f3;
                        }
                        img {
                            max-width: 95%;
                            max-height: 80vh;
                            object-fit: contain;
                            background: white;
                            border: 1px solid #ccc;
                        }
                        #message { margin: 24px; }
                    </style>
                </head>
                <body>
                    <h1>JoesPaint</h1>
                    <p>Live preview of the selected drawing tab</p>
                    <div id="message">Loading drawing...</div>
                    <img id="drawing" alt="Selected drawing" hidden>

                    <script>
                        const drawing = document.getElementById('drawing');
                        const message = document.getElementById('message');
                        let currentUrl = null;

                        async function refreshDrawing() {
                            try {
                                const response = await fetch('/image', {
                                    cache: 'no-store'
                                });

                                if (!response.ok) {
                                    const text = await response.text();
                                    drawing.hidden = true;
                                    message.textContent = text;
                                    return;
                                }

                                const blob = await response.blob();
                                const newUrl = URL.createObjectURL(blob);

                                drawing.onload = () => {
                                    if (currentUrl) {
                                        URL.revokeObjectURL(currentUrl);
                                    }
                                    currentUrl = newUrl;
                                };

                                drawing.src = newUrl;
                                drawing.hidden = false;
                                message.textContent = '';
                            } catch (error) {
                                drawing.hidden = true;
                                message.textContent =
                                    'Cannot connect to JoesPaint. Is the app running?';
                            }
                        }

                        refreshDrawing();
                        setInterval(refreshDrawing, 1500);
                    </script>
                </body>
                </html>
                """;

        byte[] response = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(
                "Content-Type", "text/html; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }

    private void handleImage(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, 405, "GET requests only.");
            return;
        }

        try {
            CompletableFuture<byte[]> future = new CompletableFuture<>();

            Platform.runLater(() -> {
                try {
                    Tab selectedTab = tabPane.getSelectionModel()
                            .getSelectedItem();

                    if (selectedTab == null) {
                        future.complete(null);
                        return;
                    }

                    StackPane drawingArea = tabDrawingAreas.get(selectedTab);

                    if (drawingArea == null) {
                        future.complete(null);
                        return;
                    }

                    WritableImage snapshot = drawingArea.snapshot(
                            new SnapshotParameters(), null);

                    try (ByteArrayOutputStream output =
                                 new ByteArrayOutputStream()) {
                        ImageIO.write(
                                SwingFXUtils.fromFXImage(snapshot, null),
                                "png",
                                output);
                        future.complete(output.toByteArray());
                    }
                } catch (Exception exception) {
                    future.completeExceptionally(exception);
                }
            });

            byte[] png = future.get(3, TimeUnit.SECONDS);

            if (png == null) {
                sendText(exchange, 404, "No drawing tab is selected.");
                return;
            }

            exchange.getResponseHeaders().set(
                    "Content-Type", "image/png");
            exchange.getResponseHeaders().set(
                    "Cache-Control", "no-store");
            exchange.sendResponseHeaders(200, png.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(png);
            }
        } catch (java.util.concurrent.TimeoutException exception) {
            sendText(exchange, 503, "Drawing preview timed out. Try again.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            sendText(exchange, 503, "Drawing preview was interrupted.");
        } catch (java.util.concurrent.ExecutionException exception) {
            sendText(exchange, 500, "Could not capture the drawing.");
        }
    }

    private void sendText(
            HttpExchange exchange, int status, String message)
            throws IOException {
        byte[] response = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=UTF-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            System.out.println("JoesPaint web server stopped.");
        }
    }
}
