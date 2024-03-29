package no.kristiania.listController;

import no.kristiania.http.HttpController;
import no.kristiania.http.HttpMessage;
import no.kristiania.Dao.Questionnaire;
import no.kristiania.Dao.QuestionnaireDao;

import java.sql.SQLException;

public class ListQuestionsController implements HttpController {
    private final QuestionnaireDao qreDao;
    public ListQuestionsController(QuestionnaireDao qreDao) {
        this.qreDao = qreDao;
    }

    @Override
    public String getPath() {
        return "/api/questions";
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String responseText = "";
        for (Questionnaire qre : qreDao.listAll()) {
            responseText += "<div>Title: " + qre.getQuestionTitle() +
                    " & Text: " + qre.getQuestionText() +
                    "</div>";
            for (Questionnaire qre2 : qreDao.listAllQuestionAndOptions()) {
                if (qre2.getQuestionTitle().equals(qre.getQuestionTitle())) {
                    if (qre2.getOptionForQuestion() == null) {
                        responseText += "";
                    }
                    else {
                        responseText += "<ul style=list-style-type:square>" +
                                "<li>" + " Option: " + qre2.getOptionForQuestion() + "</li>" +
                                "</ul>";
                    }
                }
            }
        }

        if (responseText.isEmpty()){
            responseText = "<h3>List is empty, add new question</h3>";
        }
        return new HttpMessage("HTTP/1.1 200 Ok", responseText);
    }

}
