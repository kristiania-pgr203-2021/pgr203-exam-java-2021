package no.kristiania.http;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

public class EchoQueryController implements HttpController {
    @Override
    public String getPath() {
        return "/hello";
    }

    @Override
    public HttpMessage handle(HttpMessage request) {
        String[] requestLine = request.startLine.split(" ");
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

        String responseText = "";
        String yourName = "world";
        if (fileTarget.equals("/hello")) {
            if (query != null) {
                Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);
                yourName = queryMap.get("lastName") + ", " + queryMap.get("firstName");
            }
        }
        responseText = "<p>Hello " + yourName + "</p>";

        return new HttpMessage("HTTP/1.1 200 Ok", responseText);
    }
}
