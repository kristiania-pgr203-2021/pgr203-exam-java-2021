package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;
import no.kristiania.questionnaire.ScaleDao;

import java.sql.SQLException;

public class RoleOptionsController implements HttpController {
    private  QuestionnaireDao qreDao;
    private  ScaleDao scaleDao;

    public RoleOptionsController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    public RoleOptionsController(QuestionnaireDao qreDao, ScaleDao scaleDao) {

        this.qreDao = qreDao;
        this.scaleDao = scaleDao;
    }

    @Override
    public String getPath() {
        return "/api/questionOptions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        //Implementing dropdown menu for update and filter
        if (requestTarget.equals("/api/questionOptions")){
            String responseText = "";

            int value = 1;
            for (String qre : qreDao.listAllByTitle()) {
                responseText += "<option value=" + (value++) + ">" + qre + "</option><br>";
            }
            return new HttpMessage("HTTP/1.1 200 OK", responseText);
        }

        //Implementing dropdown menu for update
        if (requestTarget.equals("/api/textOptions")){
            String responseText = "";

            int value = 1;
            for (Questionnaire qre : qreDao.listAll()) {
                responseText += "<option value=" + (value++) + ">" + qre.getQuestionTitle() + ": " + qre.getQuestionText() + "</option><br>";
            }
            return new HttpMessage("HTTP/1.1 200 OK", responseText);
        }

        ////Implementing dropdown menu for Scale
        else {
            String responseText = "";

            int value = 1;
            for (String scale : scaleDao.listAllValues()) {
                responseText += "<option value=" + (value++) + ">" + scale + "</option><br>";
            }
            return new HttpMessage("HTTP/1.1 200 OK", responseText);
        }
    }
}
