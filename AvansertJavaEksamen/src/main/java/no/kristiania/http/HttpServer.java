package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

import static no.kristiania.http.QuestionnaireServer.logger;

public class HttpServer {
    private final ServerSocket serverSocket;
    private final HashMap<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        new Thread(() -> {
            while (true){
                try(Socket clientSocket = serverSocket.accept()){
                    handleClient(clientSocket);
                }catch (IOException e){
                    logger.warn("invalid input or invalid output has occurred" + e.getMessage());
                }catch (SQLException e){
                    logger.warn("Something went wrong with database" + e.getMessage());
                }
            }
        }).start();
    }

    private void handleClient(Socket clientSocket) throws IOException, SQLException {
        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        if (questionPos != -1) {
            fileTarget = requestTarget.substring(0, questionPos);
        } else {
            fileTarget = requestTarget;
        }

        if (controllers.containsKey(fileTarget)){
            HttpMessage response = controllers.get(fileTarget).handle(httpMessage);
            response.write(clientSocket);
        }
        else {
            DefaultController.defualt(this, clientSocket, requestTarget, fileTarget);
        }
    }

    public void writeOkResponse(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseText.getBytes().length + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void addController(HttpController controller) {
        controllers.put(controller.getPath(), controller);
    }

    public HashMap<String, HttpController> getControllers() {
        return controllers;
    }
}
