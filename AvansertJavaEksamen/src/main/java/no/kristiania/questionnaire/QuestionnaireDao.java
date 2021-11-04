package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDao {

    private final DataSource dataSource;

    public QuestionnaireDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void save(Questionnaire questionnaire) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (question_title, question_text) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, questionnaire.getQuestionTitle());
                statement.setString(2, questionnaire.getQuestionText());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    questionnaire.setId(rs.getLong("id"));
                }
            }
        }
    }

    public Questionnaire retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    public List<Questionnaire> listByTitle(String title) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions where question_title = ?"
            )) {
                statement.setString(1, title);

                try (ResultSet rs = statement.executeQuery()) {

                    ArrayList<Questionnaire> questionnaires = new ArrayList<>();

                    while (rs.next()) {
                        questionnaires.add(mapFromResultSet(rs));
                    }
                    return questionnaires;
                }
            }
        }
    }

    public List<Questionnaire> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions"
            )) {

                try (ResultSet rs = statement.executeQuery()) {

                    ArrayList<Questionnaire> questionnaires = new ArrayList<>();

                    while (rs.next()) {
                        questionnaires.add(mapFromResultSet(rs));
                    }
                    return questionnaires;
                }
            }
        }
    }

    private Questionnaire mapFromResultSet(ResultSet rs) throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(rs.getLong("id"));
        questionnaire.setQuestionTitle(rs.getString("question_title"));
        questionnaire.setQuestionText(rs.getString("question_text"));
        return questionnaire;
    }

}
