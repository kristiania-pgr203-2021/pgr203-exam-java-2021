package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScaleDao {

    private final DataSource dataSource;

    public ScaleDao(DataSource testDataSource) {
        this.dataSource = testDataSource;
    }

    public void save(Scale scale) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into scale_options (scale_value, question_fk) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, scale.getScaleValue());
                statement.setLong(2, scale.getQuestionScaleFk());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    scale.setId(rs.getLong("id"));
                }
            }
        }
    }



    public Scale retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from scale_options where id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    private Scale mapFromResultSet(ResultSet rs) throws SQLException {
        Scale scale = new Scale();
        scale.setId(rs.getLong("id"));
        scale.setScaleValue(rs.getString("scale_value"));
        scale.setQuestionScaleFk(rs.getLong("question_fk"));
        return scale;
    }

    public List<Scale> listAllScale() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from scale_options"
            )) {

                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Scale> scales = new ArrayList<>();
                    while (rs.next()) {
                        mapFromResultSet(rs);
                    }
                    return scales;
                }
            }
        }
    }

    public List<String> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from scale_options"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<String> scales = new ArrayList<>();
                    while (rs.next()) {
                        scales.add(rs.getString("scale_value"));

                    }
                    return scales;
                }
            }
        }
    }

}
