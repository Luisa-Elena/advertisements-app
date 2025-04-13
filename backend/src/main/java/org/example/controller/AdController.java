package org.example.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.service.AdService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

/**
 * The {@code AdController} class handles HTTP requests related to advertisements.
 * It supports CORS, and handles GET requests to fetch all ads or a specific ad by ID,
 * as well as POST requests to create new ads.
 */
public class AdController implements HttpHandler {
    private final AdService adService = new AdService();

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
                String path = exchange.getRequestURI().getPath();
                String[] pathParts = path.split("/");
                String response;
                if (pathParts.length == 4) {
                    int id = Integer.parseInt(pathParts[3]);
                    response = adService.getById(id);
                } else {
                    response = adService.getAll();
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.flush();

            } else if ("POST".equals(exchange.getRequestMethod())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder body = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                String json = body.toString();
                Gson gson = new Gson();

                Map<String, Object> fieldValueMap = gson.fromJson(json, Map.class);
                adService.save(fieldValueMap);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(json.getBytes());
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
