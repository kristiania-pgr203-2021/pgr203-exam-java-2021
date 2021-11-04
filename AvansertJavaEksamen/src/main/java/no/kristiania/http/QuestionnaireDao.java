package no.kristiania.http;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionnaireDao {


    private final DataSource dataSource;

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
    }

    public Questionnaire retrieve(Long id) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    Questionnaire questionnaire = new Questionnaire();
                    questionnaire.setQuestionTitle(rs.getString("question_title"));
                    questionnaire.setQuestionText(rs.getString("question_text"));
                    return questionnaire;
                }
            }
        }
    }
}
