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

    public List<Questionnaire> listAllByTitleID(Long title) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions LEFT join option_to_qn on questions.id = question_fk where questions.id = ?"
            )) {
                statement.setLong(1, title);

                try (ResultSet rs = statement.executeQuery()) {

                    ArrayList<Questionnaire> qreList = new ArrayList<>();

                    while (rs.next()) {
                        mapFromResultSetWihOption(rs, qreList);
                    }
                    return qreList;
                }
            }
        }
    }

    public List<String> listAllByTitle() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<String> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(rs.getString("question_title"));
                    }
                    return result;
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
                    ArrayList<Questionnaire> qre = new ArrayList<>();
                    while (rs.next()) {
                        qre.add(mapFromResultSet(rs));
                    }
                    return qre;
                }
            }
        }
    }


    public List<Questionnaire> search(String search) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM questions left JOIN option_to_qn on questions.id = question_fk where questions.question_text like (?)"

            )) {
                statement.setString(1, "%" + search + "%");
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Questionnaire> qre = new ArrayList<>();
                    while (rs.next()) {
                        mapFromResultSetWihOption(rs, qre);
                    }
                    return qre;
                }
            }
        }
    }

    public List<Questionnaire> listAllQuestionAndOptions() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM questions LEFT join option_to_qn on questions.id = question_fk"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Questionnaire> result = new ArrayList<>();
                    while (rs.next()){
                        mapFromResultSetWihOption(rs, result);
                    }
                    return result;
                }
            }
        }
    }


    public List<Questionnaire> listAllQuestionAndScale() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select distinct  question_title, question_text, option_value, scale_value from questions left join option_to_qn on questions.id = option_to_qn.question_fk left join scale_options on questions.id = scale_options.question_fk"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Questionnaire> result = new ArrayList<>();
                    while (rs.next()){
                        Questionnaire qre = new Questionnaire();
                        qre.setQuestionTitle(rs.getString("question_title"));
                        qre.setQuestionText(rs.getString("question_text"));
                        qre.setOptionForQuestion(rs.getString("option_value"));
                        qre.setScaleForQuestion(rs.getString("scale_value"));

                        result.add(qre);
                    }
                    return result;
                }
            }
        }
    }

    private void mapFromResultSetWihOption(ResultSet rs, List<Questionnaire> result) throws SQLException {
        Questionnaire qre = new Questionnaire();
        qre.setQuestionText(rs.getString("question_text"));
        qre.setOptionForQuestion(rs.getString("option_value"));
        qre.setQuestionTitle(rs.getString("question_title"));
        result.add(qre);
    }

    private Questionnaire mapFromResultSet(ResultSet rs) throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setId(rs.getLong("id"));
        questionnaire.setQuestionTitle(rs.getString("question_title"));
        questionnaire.setQuestionText(rs.getString("question_text"));
        return questionnaire;
    }

}
