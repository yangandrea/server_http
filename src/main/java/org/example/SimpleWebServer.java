package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

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
                    } else if ("/classe.json".equals(requestTokens[1])) {
                        // Se la richiesta è per il percorso /json, restituisci un oggetto JSON
                        serveJson(out);
                        return;
                    } else {
                        requestedFilePath = ROOT_DIRECTORY + "\\classe.json";
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
    
                String contentType;
                if (filePath.endsWith(".html")) {
                    contentType = "text/html; charset=UTF-8";
                } else if (filePath.endsWith(".css")) {
                    contentType = "text/css; charset=UTF-8";
                } else {
                    contentType = "application/octet-stream";
                }
    
                out.writeBytes("HTTP/1.1 200 OK\r\n");
                out.writeBytes("Status: OK\r\n");
                out.writeBytes("Date: " + LocalDateTime.now().toString() + "\r\n");
                out.writeBytes("Content-Type: " + contentType + "\r\n");
                out.writeBytes("Content-Length: " + fileContent.length + "\r\n");
                out.writeBytes("Connection: keep-alive\r\n");
                out.writeBytes("Server: SimpleWebServer\r\n");
                out.writeBytes("\r\n");
                out.write(fileContent);
            } else {
                // File not found, return 404 Not Found
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
private static void serveJson(DataOutputStream out) {
    try {
        Alunno a = new Alunno("Mario", "Rossi", createDate(2000, 1, 1));
        Alunno a1 = new Alunno("d", "a", createDate(1974, 3, 29));
        Alunno a2 = new Alunno("a", "s", createDate(2000, 1, 1));
        Alunno a3 = new Alunno("f", "d", createDate(2000, 1, 1));

        Classe cl = new Classe(5, "BIA", "04-TC", new ArrayList<Alunno>());
                cl.addAlunno(a);
                cl.addAlunno(a1);
                cl.addAlunno(a2);
                cl.addAlunno(a3);

        // Serializza l'oggetto come JSON
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(cl);
        mapper.writeValue(new File("htdocs/classe.json"), json);

        // Imposta gli header della risposta
        out.writeBytes("HTTP/1.1 200 OK\r\n");
        out.writeBytes("Status: OK\r\n");
        out.writeBytes("Date: " + LocalDateTime.now().toString() + "\r\n");
        out.writeBytes("Content-Type: application/json; charset=UTF-8\r\n");
        out.writeBytes("Content-Length: " + json.length() + "\r\n");
        out.writeBytes("Connection: keep-alive\r\n");
        out.writeBytes("Server: SimpleWebServer\r\n");
        out.writeBytes("\r\n");

        // Scrivi la risposta JSON
        out.writeBytes(json);
        out.flush();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
        private static Date createDate(int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, day);
            return calendar.getTime();
        }
}