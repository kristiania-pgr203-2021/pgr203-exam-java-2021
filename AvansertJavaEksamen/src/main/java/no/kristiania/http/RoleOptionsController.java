package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.sql.SQLException;


public class RoleOptionsController implements HttpController {
    private final QuestionnaireDao qreDao;

    public RoleOptionsController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/questionOptions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {

        String responseText = "";

        int value = 0;
        for (String qre : qreDao.listAllByTitle()) {
            responseText += "<option value=" + (value++) + ">" + qre + "</option><br>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
