package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class UpdateQuestionController implements HttpController {
    private final QuestionnaireDao qreDao;

    public UpdateQuestionController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/updateTitle";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);

        if (request.equals("/api/updateText")){
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Long getTextId = Long.valueOf(queryMap.get("text-id"));
            String getUpdateText = AddQuestionController.decodeValue(queryMap.get("question-text"));


            logger.info("Updated text of specific row in questions table: {}", getUpdateText);
            return new HttpMessage("HTTP/1.1 200 OK", "it is done");
        }
        else {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Long getTitleId = Long.valueOf(queryMap.get("title-id"));
            String getUpdateTitle = AddQuestionController.decodeValue(queryMap.get("question-title"));

            qreDao.updateTitle(getTitleId, getUpdateTitle);
            logger.info("Updated title of specific row in questions table: {}", getUpdateTitle);


            return new HttpMessage("HTTP/1.1 200 OK", "Else");
        }

    }
}
