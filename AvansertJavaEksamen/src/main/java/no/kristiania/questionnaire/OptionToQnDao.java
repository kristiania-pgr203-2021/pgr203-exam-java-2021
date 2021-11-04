package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.*;

public class OptionToQnDao {

    private final DataSource dataSource;

    public OptionToQnDao(DataSource testDataSource) {
        this.dataSource = testDataSource;
    }

    public void save(OptionToQn optionToQn) throws SQLException {

    }


    public OptionToQn retrieve(long id) throws SQLException {
        return null;
    }


}
