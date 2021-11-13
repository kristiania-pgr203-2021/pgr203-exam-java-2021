package no.kristiania.filterController;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class FilterTitleOptionController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterTitleOptionController(QuestionnaireDao qreDao) {

        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/titleOptions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {

        String responseText = "";

        int value = 1;
        for (String qre : qreDao.listAllByTitle()) {
            responseText += "<option value=" + (value++) + ">" + qre + "</option><br>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
