package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;
import java.util.Map;

public class AddQuestionController implements HttpController {
    private final QuestionnaireDao qreDao;

    public AddQuestionController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Questionnaire qre = new Questionnaire();
        qre.setQuestionTitle((queryMap.get("questionTitle")));
        qre.setQuestionText((queryMap.get("questionText")));
        qreDao.save(qre);

        return new HttpMessage("Http/1.1 200 Ok", "it is done");
    }
}
