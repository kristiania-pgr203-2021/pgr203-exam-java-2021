package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;

public class ListQuestionsController implements HttpController {
    private final QuestionnaireDao qreDao;

    public ListQuestionsController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String response = "";

        for (Questionnaire qre : qreDao.listAll()) {
            response += "<div>" + qre.getQuestionTitle() + ", " + qre.getQuestionText() + "</div>";
        }


        return new HttpMessage("HTTP/1.1 200 Ok", response);
    }
}
