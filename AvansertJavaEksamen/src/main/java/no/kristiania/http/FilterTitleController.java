package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterTitleController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterTitleController(QuestionnaireDao qreDao) {

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
        for (String qre : qreDao.listAllByTitle().stream().distinct().collect(Collectors.toList())) {
            responseText += "<option value=" + (value++) + ">" + qre + "</option><br>";
        }
        return new HttpMessage("HTTP/1.1 200 OK", responseText);
    }
}
