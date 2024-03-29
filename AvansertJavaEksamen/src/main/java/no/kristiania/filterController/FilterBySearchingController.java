package no.kristiania.filterController;

import no.kristiania.addController.AddQuestionController;
import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.Dao.Questionnaire;
import no.kristiania.Dao.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

public class FilterBySearchingController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterBySearchingController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/searchOption";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        String questionText = AddQuestionController.decodeValue(queryMap.get("question-name"));

        String responseText = "";
            for (Questionnaire qre :
                    qreDao.search(questionText)) {
                responseText = "<div>Title: " + qre.getQuestionTitle() +
                        " & Text: " + qre.getQuestionText() +
                        "</div>";
                for (Questionnaire qre2 : qreDao.listAllQuestionAndOptions()) {
                    if (qre2.getQuestionText().equals(qre.getQuestionText())) {
                        if (qre2.getOptionForQuestion() == null) {
                            responseText += "";
                        }else {
                            responseText += "<ul style=list-style-type:square>" +
                                    "<li>" + " Option: " + qre2.getOptionForQuestion() + "</li>" +
                                    "</ul>";
                        }
                    }
                }
            }
        return new HttpMessage("HTTP/1.1 200 OK", "<p>Filtering only text with wildcard</p>" +
                responseText + "<br><a href=/questionnaireFilter.html>Return back Or " +
                "<a href=/index.html>Return to home page</a></div>");
    }
}
