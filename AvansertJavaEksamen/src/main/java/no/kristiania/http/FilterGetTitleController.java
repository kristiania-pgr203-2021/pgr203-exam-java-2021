package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

public class FilterGetTitleController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterGetTitleController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/catchQuryTitle";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);
        String requestGet = request.startLine.split(" ")[0];

        if (requestGet.equals("GET")){
            if (query != null) {
                Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);

                Questionnaire qre = new Questionnaire();
                String title = queryMap.get("title-name");
                System.out.println(title);
            }
        }

        String responseText = "<div>List of title have been sorted: "+"<h3>" + "<h3>" + "<br><a href=/questionnaireFilter.html>Return back to see</a></div>";

        return new HttpMessage("HTTP/1.1 200 OK", responseText);

    }
}
