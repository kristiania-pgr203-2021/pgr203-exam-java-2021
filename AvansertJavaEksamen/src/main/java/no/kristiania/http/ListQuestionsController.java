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
        String responseText = "";

        for (Questionnaire qre : qreDao.listAll()) {
            responseText += "<div>" + qre.getQuestionTitle() + ", " + qre.getQuestionText() + "</div>";
        }

        if (responseText.isEmpty()){
            responseText = "<h3>List is empty, add new question to initialize</h3>";
        }


        return new HttpMessage("HTTP/1.1 200 Ok", responseText);
    }
}
