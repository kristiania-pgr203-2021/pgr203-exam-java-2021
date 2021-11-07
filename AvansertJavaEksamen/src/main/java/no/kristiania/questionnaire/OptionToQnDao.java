package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OptionToQnDao {

    private final DataSource dataSource;

    public OptionToQnDao(DataSource testDataSource) {
        this.dataSource = testDataSource;
    }

    public void save(OptionToQn optionToQn) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into option_to_qn (option_value, question_fk) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, optionToQn.getOption());
                statement.setLong(2, optionToQn.getQuestion_fk());

                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    optionToQn.setId(rs.getLong("id"));
                }
            }
        }
    }


    public OptionToQn retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from option_to_qn where id = ?"
            )) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return mapFromResultSet(rs);
                }
            }
        }
    }

    private OptionToQn mapFromResultSet(ResultSet rs) throws SQLException {
        OptionToQn option = new OptionToQn();
        option.setId(rs.getLong("id"));
        option.setOption(rs.getString("option_value"));
        option.setQuestion_fk(rs.getLong("question_fk"));
        return option;
    }

    public List<OptionToQn> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "select * from option_to_qn"
            )) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<OptionToQn> optionList = new ArrayList<>();
                    while (rs.next()) {
                        optionList.add(mapFromResultSet(rs));
                    }
                    return optionList;
                }
            }
        }
    }
}
