package no.kristiania.filterController;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

public class FilterByTitleController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterByTitleController(QuestionnaireDao qreDao) {
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
        String query = requestTarget.substring(questionPos + 1);
        String requestGet = request.startLine.split(" ")[0];

        String responseText = "";
        if (requestGet.equals("GET")) {
            if (query != null) {
                Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);

                for (Questionnaire qre :
                        qreDao.listAllByTitleID(Long.valueOf(queryMap.get("title-name")))) {
                    responseText = "<div>Title: " + qre.getQuestionTitle() +
                            " & Text: " + qre.getQuestionText() +
                            "</div>";
                    for (Questionnaire qre2 : qreDao.listAllQuestionAndOptions()) {
                        if (qre2.getQuestionTitle().equals(qre.getQuestionTitle())) {
                            if (qre2.getOptionForQuestion() == null) {
                                responseText += "";
                            } else {
                                responseText += "<ul style=list-style-type:square>" +
                                        "<li>" + " Option: " + qre2.getOptionForQuestion() + "</li>" +
                                        "</ul>";
                            }
                        }
                    }
                }
            }
        }
        return new HttpMessage("HTTP/1.1 200 OK", "<p>Filter by Title: </p>"
                + responseText + "<br><a href=/questionnaireFilter.html>Return back " +
                "Or <a href=/index.html>Return to home page</a></div>");
    }
}
