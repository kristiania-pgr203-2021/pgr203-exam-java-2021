package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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


        Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);

        String qestionText = queryMap.get("question-name");

        for (Questionnaire list:
                qreDao.search(qestionText)) {
            System.out.println("Title" + list.getQuestionTitle() + " Text: " + list.getQuestionText() + " Option:" + list.getOptionForQuestion());
        }

        return new HttpMessage("HTTP/1.1 200 OK", qestionText);



















       /* String[] requestLine = request.startLine.split(" ");
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
                    for (Questionnaire qreList:
                        qreDao.listByText(qre.getQuestionText())) {
                        if (qre.getOptionForQuestion().isEmpty() || qre.getOptionForQuestion() == null){
                            System.out.println("en feil er");
                        }
                        response += "<div><h4> Title: " + qre.getQuestionTitle() +
                                " Text: " + qre.getQuestionText() + " Option: " + qreList.getOptionForQuestion()
                                + "</h4></div><br>";
                    }
                }
            }
        }

        String responseText = "<div><h3>Filtering list of all samme text: " + "</h3>" + response + "<br><a href=/questionnaireFilter.html>Return back to see</a></div>";

        return new HttpMessage("HTTP/1.1 200 OK", responseText);*/
    }
}
