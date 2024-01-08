package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class SimpleWebServer {
    private static final int PORT = 8080;

    private static final String ROOT_DIRECTORY = "htdocs";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server in ascolto sulla porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClientRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())
        ) {
            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("Richiesta ricevuta: " + requestLine);

                String[] requestTokens = requestLine.split("\\s+");
                if (requestTokens.length == 3 && requestTokens[2].contains("HTTP")) {
                    String requestedFilePath;
                    if ("/".equals(requestTokens[1])) {
                        // Se la richiesta è per la radice (/), restituisci il contenuto di htdocs/index.html
                        requestedFilePath = ROOT_DIRECTORY + "\\index.html";
                    } else {
                        requestedFilePath = ROOT_DIRECTORY + requestTokens[1];
                    }
                    serveFile(requestedFilePath, out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void serveFile(String filePath, DataOutputStream out) {
        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path) && !Files.isDirectory(path)) {
                byte[] fileContent = Files.readAllBytes(path);

                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("Status: OK\r\n");
                out.writeBytes("Date: " + LocalDateTime.now().toString() + "\r\n");
                out.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
                out.writeBytes("Content-Length: " + fileContent.length + "\r\n");
                out.writeBytes("Connection: keep-alive\r\n");
                out.writeBytes("Server: SimpleWebServer\r\n");
                out.writeBytes("\r\n");
                out.write(fileContent);
            } else {
                // File non trovato, restituisci 404 Not Found
                send404NotFound(out);
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send404NotFound(DataOutputStream out) throws IOException {
    String filePath = ROOT_DIRECTORY + "/404.html";
    Path path = Paths.get(filePath);

    if (Files.exists(path) && !Files.isDirectory(path)) {
        byte[] fileContent = Files.readAllBytes(path);

        out.writeBytes("HTTP/1.1 404 Not Found\r\n");
        out.writeBytes("Status: Not Found\r\n");
        out.writeBytes("Date: " + LocalDateTime.now().toString() + "\r\n");
        out.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
        out.writeBytes("Content-Length: " + fileContent.length + "\r\n");
        out.writeBytes("Connection: keep-alive\r\n");
        out.writeBytes("Server: SimpleWebServer\r\n");
        out.writeBytes("\r\n");
        out.write(fileContent);
    }
    out.flush();
    }
}