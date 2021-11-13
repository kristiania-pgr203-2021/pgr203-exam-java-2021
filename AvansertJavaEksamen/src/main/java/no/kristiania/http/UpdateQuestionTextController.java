package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class UpdateQuestionTextController implements HttpController {
    private final QuestionnaireDao qreDao;

    public UpdateQuestionTextController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/updateText";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Long getTextId = Long.valueOf(queryMap.get("text-id"));
            String getUpdateText = AddQuestionController.decodeValue(queryMap.get("question-text"));

            qreDao.updateText(getTextId, getUpdateText);
            logger.info("Updated text of specific row in questions table: {}", getUpdateText);
            return new HttpMessage("Http/1.1 303 See other", "", "/index.html");
        }
    }
