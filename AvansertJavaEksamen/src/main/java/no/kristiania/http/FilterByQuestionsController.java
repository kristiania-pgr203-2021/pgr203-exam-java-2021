package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;


public class FilterByQuestionsController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterByQuestionsController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/questionNameOptions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        String responseText = "";

        int value = 1;
        for (Questionnaire qre : qreDao.listAll()) {
            responseText += "<option value=" + (value++) + ">" + qre.getQuestionText() + "</option><br>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
