package no.kristiania.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class DefaultController {
    public static void defualt(HttpServer httpServer, Socket clientSocket, String requestTarget, String fileTarget) throws IOException {
        InputStream fileResource = httpServer.getClass().getResourceAsStream(fileTarget);

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

            httpServer.writeOkResponse(clientSocket, responseText, contentType);
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
