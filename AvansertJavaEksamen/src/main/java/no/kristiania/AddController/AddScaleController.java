package no.kristiania.AddController;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.questionnaire.*;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import static no.kristiania.http.QuestionnaireServer.logger;

public class AddScaleController implements HttpController {
    private final ScaleDao scaleDao;

    public AddScaleController(ScaleDao scaleDao) {
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

            for (Scale checking:
                    scaleDao.ForChecking()) {
                if (checking.getQuestionScaleFk() == getQuestionId && checking.getScaleValue().equals(getScale)){
                    String response = "<div style=color:red>You have allerede added \""+getScale+ "\""  + " to question</div><br>" +
                            "<a href=/index.html>Return to front page</a>" +
                            " Or <a href=/ScaleToQuestion.html>Add new scale</a>";
                    return new HttpMessage("Http/1.1 400 Bad Reqeust", response);
                }
            }

            scaleDao.save(scale);
            logger.info("option: {}: and id: {} have been added ", queryMap.get("questions"), queryMap.get("skalaOption"));
        }

        return new HttpMessage("HTTP/1.1 303 See other","" ,"/listAll.html");
    }
}
