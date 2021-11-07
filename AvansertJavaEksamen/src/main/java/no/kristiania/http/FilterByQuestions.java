package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class FilterByQuestions implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterByQuestions(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/questionNameOptions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String responseText = "";

        int value = 1;
        for (Questionnaire qre : qreDao.listAll()) {
            responseText += "<option value=" + (value++) + ">" + qre.getQuestionText() + "</option><br>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
