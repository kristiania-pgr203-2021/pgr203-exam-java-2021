package no.kristiania.http;

import no.kristiania.questionnaire.OptionToQn;
import no.kristiania.questionnaire.OptionToQnDao;

import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class AddOptionController implements HttpController {
    private final OptionToQnDao optionDao;

    public AddOptionController(OptionToQnDao optionDao) {
        this.optionDao = optionDao;
    }

    @Override
    public String getPath() {
        return "/api/alternativeAnswers";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);
        String requestGet = request.startLine.split(" ")[0];

        if (requestGet.equals("GET")){
            String option = "";
            if (query != null) {
                String decodeQuery = AddQuestionController.decodeValue(query);
                Map<String, String> queryMap = HttpMessage.parseRequestParameters(decodeQuery);
                logger.info("query decoding: {}", decodeQuery);

                OptionToQn optionToQn = new OptionToQn();
                optionToQn.setOption(queryMap.get("option"));
                optionToQn.setQuestion_fk(Long.parseLong(queryMap.get("questions")));
                optionDao.save(optionToQn);
                logger.info("option: {}: and id: {} have been added", queryMap.get("option"), queryMap.get("questions"));

                option = (queryMap.get("option")) + ", " + (queryMap.get("questions"));
            }

            String responseText = "<p>" + option + "</p>";

            new HttpMessage("HTTP/1.1 200 Ok", responseText);
        }

        String responseText = "<div>Option to question have been added. <br><a href=/index.html>Return to front page</a><br>" +
                                "Or <a href=/newQuestionnaire.html>Add new question</a></div>";

        return new HttpMessage("HTTP/1.1 200 Ok", responseText);

    }
}
