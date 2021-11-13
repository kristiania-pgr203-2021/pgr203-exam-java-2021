package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class UpdateQuestionTitleController implements HttpController {
    private final QuestionnaireDao qreDao;

    public UpdateQuestionTitleController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/updateTitle";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {

            Map<String, String> queryMap = HttpMessage.parseRequestParameters(request.messageBody);
            Long getTitleId = Long.valueOf(queryMap.get("title-id"));
            String getUpdateTitle = AddQuestionController.decodeValue(queryMap.get("question-title"));

            qreDao.updateTitle(getTitleId, getUpdateTitle);
            logger.info("Updated title of specific row in questions table: {}", getUpdateTitle);

            return new HttpMessage("Http/1.1 303 See other", "", "/index.html");
        }
    }

