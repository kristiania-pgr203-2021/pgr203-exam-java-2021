package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        String questionTitle = decodeValue(queryMap.get("questionTitle"));
        qre.setQuestionTitle(questionTitle);
        String questionText = decodeValue(queryMap.get("questionText"));
        qre.setQuestionText(questionText);

        qreDao.save(qre);
        logger.info("Title: {} and Text: {} have been added", qre.getQuestionTitle(), qre.getQuestionText());

        String response = "<div>Question have been added successfully on database. <br><a href=/index.html>Return to front page</a><br>" +
                "Or <a href=/addOption.html>Add Option to question</a></div>";

        return new HttpMessage("Http/1.1 200 Ok", response);
    }

    public static String decodeValue(String responseText) {
            return URLDecoder.decode(responseText, StandardCharsets.UTF_8);
    }
}
