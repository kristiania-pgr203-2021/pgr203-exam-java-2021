package no.kristiania.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServer {


    static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final ServerSocket serverSocket;
    private List<String> roles = new ArrayList<>();


    public HttpServer(int serverPort) throws IOException {

        serverSocket = new ServerSocket(serverPort);

        new Thread(this::handleClients).start();
    }

    private void handleClients () {
        try {
            while (true){
                handleClient();
            }

        } catch (IOException e) {
            logger.warn("invalid input or invalid output has occurred" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient() throws IOException {
        Socket clientSocket = serverSocket.accept();

        HttpMessage httpMessage = new HttpMessage(clientSocket);
        String[] requestLine = httpMessage.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String fileTarget;
        String query = null;
        if (questionPos != -1) {
            fileTarget = requestTarget.substring(0, questionPos);
            query = requestTarget.substring(questionPos+1);
        } else {
            fileTarget = requestTarget;
        }

        if (requestTarget.equals("/api/questions")){

            String responseText = null;
            if (responseText == null){
                responseText = "<h3>List is empty</h3>";
            }

            writeOkResponse(clientSocket, responseText, "text/html");
        }

        if (requestTarget.equals("/hello")){

            String responseText = "Hello world";
            writeOkResponse(clientSocket, responseText, "text/html");
        }

        else if (requestTarget.equals("/shouldReturnContent")){

            String responseText = "<p>Hello world</p>";
            writeOkResponse(clientSocket, responseText, "text/html");
        }
        else {

            InputStream fileResource = getClass().getResourceAsStream(fileTarget);

            if (fileResource != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                fileResource.transferTo(buffer);
                String responseText = buffer.toString();

                String contentType = "text/plain";
                if (requestTarget.endsWith(".html")){
                    contentType = "text/html";
                }
                else if (requestTarget.endsWith(".css")){
                    contentType = "text/css";
                }

                writeOkResponse(clientSocket, responseText, contentType);
                return;
            }

            String responseText = "File not found: " + requestTarget;

            String response = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseText.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseText;
            clientSocket.getOutputStream().write(response.getBytes());
        }
    }

    private Map<String, String> parseRequestParameters(String query) {
        Map<String, String> queryMap = new HashMap<>();
        for (String queryParameter : query.split("&")) {
            int equalsPos = queryParameter.indexOf('=');
            String parameterName = queryParameter.substring(0, equalsPos);
            String parameterValue = queryParameter.substring(equalsPos+1);
            queryMap.put(parameterName, parameterValue);
        }
        return queryMap;
    }

    private void writeOkResponse(Socket clientSocket, String responseText, String contentType) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseText.getBytes().length + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseText;
        clientSocket.getOutputStream().write(response.getBytes());
    }


    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8000);
        logger.info("Starting http://localhost:{}/index.html", + httpServer.getPort());

    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
