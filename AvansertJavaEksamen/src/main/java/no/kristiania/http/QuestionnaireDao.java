package no.kristiania.http;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestionnaireDao {


    private final DataSource dataSource;
    private Questionnaire questionnaire;

    public QuestionnaireDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void save(Questionnaire questionnaire) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (question_title, question_text) values (?, ?)"
            )) {
                statement.setString(1, questionnaire.getQuestionTitle());
                statement.setString(2, questionnaire.getQuestionText());

                statement.executeUpdate();
            }
        }

        this.questionnaire = questionnaire;
    }

    public Questionnaire retrieve(Long id) {
        return questionnaire;
    }
}
