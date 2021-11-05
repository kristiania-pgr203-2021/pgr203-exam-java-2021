package no.kristiania.http;

import no.kristiania.questionnaire.OptionToQnDao;

import java.sql.SQLException;
import java.util.Map;

public class AddOptionController implements HttpController {
    private final OptionToQnDao optionDao;

    public AddOptionController(OptionToQnDao optionDao) {
        this.optionDao = optionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);

        String option = "";
        if (query != null) {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);
            option = queryMap.get("option") + ", " + queryMap.get("questions");
        }
        String responseText = "<p>" + option + "</p>";

        return new HttpMessage("HTTP/1.1 200 Ok", responseText);
    }
}
