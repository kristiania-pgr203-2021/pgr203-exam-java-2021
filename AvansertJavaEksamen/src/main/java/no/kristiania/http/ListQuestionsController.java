package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;

public class ListQuestionsController implements HttpController {
    public ListQuestionsController(QuestionnaireDao qreDao) {
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        return new HttpMessage("HTTP/1.1 200 Ok", "ok");
    }
}
