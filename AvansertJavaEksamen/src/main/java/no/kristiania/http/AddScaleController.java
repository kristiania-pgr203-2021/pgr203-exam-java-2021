package no.kristiania.http;

import no.kristiania.questionnaire.OptionToQn;
import no.kristiania.questionnaire.QuestionnaireDao;
import no.kristiania.questionnaire.Scale;
import no.kristiania.questionnaire.ScaleDao;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class AddScaleController implements HttpController {
    private final QuestionnaireDao qreDao;
    private final ScaleDao scaleDao;

    public AddScaleController(QuestionnaireDao qreDao, ScaleDao scaleDao) {
        this.qreDao = qreDao;
        this.scaleDao = scaleDao;
    }

    @Override
    public String getPath() {
        return "/api/scaleAlternative";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException, UnsupportedEncodingException {
        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];

        int questionPos = requestTarget.indexOf('?');
        String query = requestTarget.substring(questionPos+1);
        String requestGet = request.startLine.split(" ")[0];

        String responseText = "";
        String getScale = "";
        if (query != null) {
            String decodeQuery = AddQuestionController.decodeValue(query);
            Map<String, String> queryMap = HttpMessage.parseRequestParameters(decodeQuery);
            logger.info("query decoding: {}", decodeQuery);

            Scale scale = new Scale();

            long getQuestionId = Long.parseLong(queryMap.get("questions"));
            scale.setQuestionScaleFk(getQuestionId);
            getScale = queryMap.get("skalaOption");
            scale.setScaleValue(getScale);

            scaleDao.save(scale);
            logger.info("option: {}: and id: {} have been added ", queryMap.get("questions"), queryMap.get("skalaOption"));
        }

        return new HttpMessage("HTTP/1.1 303 See other","" ,"/listAll.html");
    }
}
