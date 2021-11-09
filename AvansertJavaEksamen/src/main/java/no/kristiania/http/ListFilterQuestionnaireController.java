package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;

public class ListFilterQuestionnaireController implements HttpController {
    private final QuestionnaireDao qreDao;

    public ListFilterQuestionnaireController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/questionnaire";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {



        return new HttpMessage("HTTP/1.1 200 Ok", "et problem her");
    }
}
