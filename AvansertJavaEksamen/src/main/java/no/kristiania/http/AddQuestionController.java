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
    public String getPath() {
        return "/api/newQuestions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
        Questionnaire qre = new Questionnaire();
        String questionTitle = decodeValue(queryMap.get("questionTitle"));
        qre.setQuestionTitle(questionTitle);
        String questionText = decodeValue(queryMap.get("questionText"));
        qre.setQuestionText(questionText);

        for (Questionnaire checking:
             qreDao.listAll()) {
            if (checking.getQuestionTitle().equals(questionTitle) && checking.getQuestionText().equals(questionText)){
                String response = "<div style=color:red>You have allerede added \""+questionTitle+ "\"" + " and " + "\""+questionText+"\" " + " to questionnaire</div><br><a href=/index.html>Return to front page</a>" +
                        " Or <a href=/newQuestionnaire.html>Add new question</a>";
                return new HttpMessage("Http/1.1 400 Bad Reqeust", response);
            }
        }

        qreDao.save(qre);
        logger.info("Title: {} and Text: {} have been added", qre.getQuestionTitle(), qre.getQuestionText());


        return new HttpMessage("Http/1.1 303 See other", "", "/index.html");
    }

    public static String decodeValue(String responseText) {
            return URLDecoder.decode(responseText, StandardCharsets.UTF_8);
    }
}
