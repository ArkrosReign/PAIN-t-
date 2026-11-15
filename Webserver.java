package com.mycompany.mavenproject1;

//webserver imports
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

//java imports
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Webserver {

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
        server.createContext("/tabs", this::handleTabs);

        server.start();

        System.out.println(
                "JoesPaint web server running at http://localhost:8080/");
    }

    //the webpage! 
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
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1">
                    <title>JoesPaint</title>

                    <style>
                        body {
                            font-family: sans-serif;
                            margin: 0;
                            padding: 24px;
                            text-align: center;
                            background: #f3f3f3;
                        }

                        select {
                            padding: 8px;
                            margin: 10px;
                            font-size: 16px;
                        }

                        img {
                            max-width: 95%;
                            max-height: 80vh;
                            object-fit: contain;
                            background: white;
                            border: 1px solid #ccc;
                        }

                        #message {
                            margin: 24px;
                        }
                    </style>
                </head>

                <body>
                    <h1>JoesPaint</h1>

                    <label for="tabSelect">Choose drawing tab:</label>
                    <select id="tabSelect"></select>

                    <div id="message">Loading drawing...</div>
                    <img id="drawing" alt="Drawing preview" hidden>

                    <script>
                        const drawing =
                            document.getElementById('drawing');
                        const message =
                            document.getElementById('message');
                        const tabSelect =
                            document.getElementById('tabSelect');

                        let currentUrl = null;

                        // Gets the available drawing tabs from JoesPaint
                        async function refreshTabs() {
                            const previous = tabSelect.value;

                            const response = await fetch('/tabs', {
                                cache: 'no-store'
                            });

                            if (!response.ok) {
                                throw new Error('Could not load tabs');
                            }

                            const data = await response.json();

                            tabSelect.replaceChildren();

                            for (const tab of data.tabs) {
                                const option =
                                    document.createElement('option');

                                option.value = tab.index;
                                option.textContent = tab.name;
                                tabSelect.appendChild(option);
                            }

                            // Preserve the browser's selection if possible.
                            if (data.tabs.some(
                                tab => String(tab.index) === previous)) {
                                tabSelect.value = previous;
                            } else if (data.selected >= 0) {
                                tabSelect.value = String(data.selected);
                            }
                        }

                        // Requests a preview of the chosen tab
                        async function refreshDrawing() {
                            try {
                                await refreshTabs();

                                if (tabSelect.value === '') {
                                    drawing.hidden = true;
                                    message.textContent =
                                        'No drawing tab is available.';
                                    return;
                                }

                                const response = await fetch(
                                    '/image?tab=' +
                                    encodeURIComponent(tabSelect.value),
                                    { cache: 'no-store' }
                                );

                                if (!response.ok) {
                                    drawing.hidden = true;
                                    message.textContent =
                                        await response.text();
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
                                    'Zzzzz, wake up JoesPaint';
                            }
                        }

                        // Changing the dropdown immediately refreshes preview
                        tabSelect.addEventListener(
                            'change', refreshDrawing);

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

    //Basically gives us the name of the tabs so they can be displayed
    private void handleTabs(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, 405, "GET requests only.");
            return;
        }

        //this holds the tab info on the JavaFX thread then put into JSON for the brower
        CompletableFuture<String> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                StringBuilder json =
                        new StringBuilder("{\"selected\":");

                int selectedIndex =
                        tabPane.getSelectionModel().getSelectedIndex();

                json.append(selectedIndex);
                json.append(",\"tabs\":[");

                var tabs = tabPane.getTabs();

                for (int i = 0; i < tabs.size(); i++) {
                    if (i > 0) {
                        json.append(",");
                    }

                    String name = tabs.get(i).getText();

                    json.append("{\"index\":")
                        .append(i)
                        .append(",\"name\":\"")
                        .append(jsonEscape(name))
                        .append("\"}");
                }

                json.append("]}");
                future.complete(json.toString());

            } catch (Exception exception) {
                future.completeExceptionally(exception);
            }
        });

        try {
            String json = future.get(3, TimeUnit.SECONDS);
            byte[] response = json.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                    "Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().set("Cache-Control", "no-store");

            exchange.sendResponseHeaders(200, response.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(response);
            }

        } catch (TimeoutException exception) {
            sendText(exchange, 503, "Loading tabs timed out.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            sendText(exchange, 503, "Loading tabs was interrupted.");
        } catch (ExecutionException exception) {
            sendText(exchange, 500, "Could not retrieve drawing tabs.");
        }
    }

    private String jsonEscape(String text) {
        StringBuilder escaped = new StringBuilder();

        for (char character : text.toCharArray()) {
            switch (character) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                default -> {
                    if (character < 0x20) {
                        escaped.append(String.format(
                                "\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
                }
            }
        }

        return escaped.toString();
    }

    //this handles the requests to preview the drawing then send it to the browser
    private void handleImage(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, 405, "GET requests only.");
            return;
        }

        //this gets the tab index requested then forwards it back to the url
        String query = exchange.getRequestURI().getRawQuery();
        Integer requestedIndex = null;

        if (query != null && query.startsWith("tab=")) {
            try {
                requestedIndex =
                        Integer.parseInt(query.substring(4));
            } catch (NumberFormatException exception) {
                sendText(exchange, 400, "Invalid tab index.");
                return;
            }
        }

        final Integer tabIndex = requestedIndex;

        try {
            CompletableFuture<byte[]> future = new CompletableFuture<>();
            Platform.runLater(() -> {
                try {
                    int index;

                    if (tabIndex == null) {
                        index = tabPane.getSelectionModel()
                                .getSelectedIndex();
                    } else {
                        index = tabIndex;
                    }

                    if (index < 0 || index >= tabPane.getTabs().size()) {
                        future.complete(null);
                        return;
                    }

                    Tab selectedTab = tabPane.getTabs().get(index);
                    StackPane drawingArea =
                            tabDrawingAreas.get(selectedTab);

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
                sendText(exchange, 404, "No drawing tab is available.");
                return;
            }

            exchange.getResponseHeaders().set(
                    "Content-Type", "image/png");
            exchange.getResponseHeaders().set("Cache-Control", "no-store");

            exchange.sendResponseHeaders(200, png.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(png);
            }

        } catch (TimeoutException exception) {
            sendText(exchange, 503, "Drawing preview timed out. Try again.");
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            sendText(exchange, 503, "Drawing preview was interrupted.");
        } catch (ExecutionException exception) {
            sendText(exchange, 500, "Could not capture the drawing.");
        }
    }

    //sends a message back to the browser with a status 
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

    //LETS LEAVE *dies*
    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;

            System.out.println("JoesPaint web server stopped.");
        }
    }
}
