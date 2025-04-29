package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.service.AdService;
import org.example.service.TypeService;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The {@code TypeController} class handles HTTP requests related to advertisement types.
 * It supports CORS and handles GET requests to fetch all available advertisement types.
 */
public class TypeController implements HttpHandler {
    private final TypeService typeService = new TypeService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

        try {
            if ("GET".equals(exchange.getRequestMethod())) {

                String response = typeService.getAll();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(401, -1);
            }
        } catch(Exception e) {
            String error = e.getMessage();
            exchange.sendResponseHeaders(500, error.length());
            OutputStream output = exchange.getResponseBody();
            output.write(error.getBytes());
            output.flush();
        } finally {
            exchange.close();
        }
    }
}
