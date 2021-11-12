package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class AbstractForDao<T> {

    protected final DataSource dataSource;

    public AbstractForDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected T retrieve(long id, String Commands) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    Commands
            )) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return mapFromAbsResultSet(rs);
                }
            }
        }
    }

    protected abstract T mapFromAbsResultSet(ResultSet rs) throws SQLException;

}
