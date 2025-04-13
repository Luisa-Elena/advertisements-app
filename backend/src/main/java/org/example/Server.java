package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.controller.AdController;
import org.example.controller.TypeController;
import org.example.model.Ad;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code Server} class sets up and runs an HTTP server to handle requests.
 * The server listens on port 8080 and defines endpoints for advertisements and ad types.
 */
public class Server {

    public static void main(String[] args) throws IOException {

        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/api/ads", new AdController());
        server.createContext("/api/types", new TypeController());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + serverPort);
    }
}