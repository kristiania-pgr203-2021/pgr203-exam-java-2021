package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpPostClient {
    private final HttpMessage httpMessage;
    private final int statusCode;

    public HttpPostClient(String host, int port, String requestTarget, String contentBody, String contentType) throws IOException {
        Socket socket = new Socket(host, port);

        String request = "POST " + requestTarget + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Content-Length: " + contentBody.length() + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                contentBody;
        socket.getOutputStream().write(request.getBytes(StandardCharsets.ISO_8859_1.toString()));

        httpMessage = new HttpMessage(socket);
        String[] statusLine = httpMessage.startLine.split(" ");
        this.statusCode = Integer.parseInt(statusLine[1]);
    }

    public int getStatusCode(){
        return statusCode;
    }
}
