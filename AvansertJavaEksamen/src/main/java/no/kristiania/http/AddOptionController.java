package no.kristiania.http;

import no.kristiania.questionnaire.OptionToQnDao;

import java.sql.SQLException;
import java.util.Map;

public class AddOptionController implements HttpController {
    public AddOptionController(OptionToQnDao optionDao) {
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {


        return new HttpMessage("HTTP/1.1 200 Ok", "responseText");
    }
}
