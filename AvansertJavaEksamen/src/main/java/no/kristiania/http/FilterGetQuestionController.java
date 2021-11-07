package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FilterGetQuestionController implements HttpController {
    private final QuestionnaireDao qreDao;

    public FilterGetQuestionController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/catchQuryQuestion";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);
        String requestGet = request.startLine.split(" ")[0];

        String response = "";
        if (requestGet.equals("GET")){
            if (query != null) {
                Map<String, String> queryMap = HttpMessage.parseRequestParameters(query);

                for (Questionnaire qre:
                        qreDao.listAllByTitleID(Long.valueOf(queryMap.get("question-name")))) {
                    response += "<div><h4> Title: "+qre.getQuestionTitle() +
                            " Text: " + qre.getQuestionText() + " Option: " + "</h4></div><br>";

                    for (Questionnaire qreList:
                        qreDao.listByText(qre.getQuestionText())) {
                        response += "<div><h4> Title: "+qre.getQuestionTitle() +
                                " Text: " + qre.getQuestionText() + " Option: " + qreList.getOptionForQuestion()
                                + "</h4></div><br>";
                    }
                }
            }
        }

        String responseText = "<div><h3>List of question have been sorted: " + "</h3>" + response + "<br><a href=/questionnaireFilter.html>Return back to see</a></div>";

        return new HttpMessage("HTTP/1.1 200 OK", responseText);

    }
}
