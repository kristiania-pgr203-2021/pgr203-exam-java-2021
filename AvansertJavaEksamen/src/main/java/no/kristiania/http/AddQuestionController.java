package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class AddQuestionController implements HttpController {
    private final QuestionnaireDao qreDao;

    public AddQuestionController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Questionnaire qre = new Questionnaire();
        qre.setQuestionText((queryMap.get("questionText")));
        qre.setQuestionTitle((queryMap.get("questionTitle")));
        qreDao.save(qre);
        logger.info("Title: {} and Text: {} have been added", qre.getQuestionTitle(), qre.getQuestionText());

        return new HttpMessage("Http/1.1 200 Ok", "it is done");
    }
}
