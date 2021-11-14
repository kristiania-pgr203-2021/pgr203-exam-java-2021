package no.kristiania.Dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDao extends AbstractForDao<Questionnaire> {

    public QuestionnaireDao(DataSource dataSource) {
        super(dataSource);
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

    public void updateTitle(long id, String title) throws SQLException {
        super.updateQuestion(id, "update questions set question_title='" + title + "' where id= ?");
    }

    public void updateText(long id, String text) throws SQLException {
        super.updateQuestion(id, "update questions set question_text='" + text + "' where id= ?");
    }

    public Questionnaire retrieve(long id) throws SQLException {
        return super.retrieve(id, "select * from questions where id = ?");
    }

    public List<Questionnaire> listAllByTitleID(long title) throws SQLException {
        return super.retrieveLists(title, "select * from questions where id = ?");
    }

    public List<String> listAllByTitle() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from questions order by id asc"
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
                    "select * from questions order by id asc"
            )) {

                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Questionnaire> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapFromAbsResultSet(rs));
                    }
                    return result;
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
                    ArrayList<Questionnaire> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapFromAbsResultSet(rs));
                    }
                    return result;
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
                        result.add(mapFromAbsResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    public List<Questionnaire> listAllQuestionAndScale() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select distinct question_title, scale_value from questions left join scale_options on questions.id = scale_options.question_fk"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Questionnaire> result = new ArrayList<>();
                    while (rs.next()){
                        Questionnaire qre = new Questionnaire();
                        qre.setQuestionTitle(rs.getString("question_title"));
                        qre.setScaleForQuestion(rs.getString("scale_value"));
                        result.add(qre);
                    }
                    return result;
                }
            }
        }
    }

    @Override
    protected Questionnaire mapFromAbsResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();

        Questionnaire qre = new Questionnaire();
        qre.setId(rs.getLong("id"));
        qre.setQuestionTitle(rs.getString("question_title"));
        qre.setQuestionText(rs.getString("question_text"));

        for (int i = 1; i < numberOfColumns + 1; i++) {
            String columnName = rsMetaData.getColumnName(i);
            if ("option_value".equals(columnName)) {
                qre.setOptionForQuestion(rs.getString("option_value"));
            }
        }
        return qre;
    }

}
