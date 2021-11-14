package no.kristiania.questionnaire;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static no.kristiania.http.QuestionnaireServer.logger;

public abstract class AbstractForDao<T> {

    protected final DataSource dataSource;

    public AbstractForDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected T retrieve(long id, String commands) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    commands
            )) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()){
                        return mapFromAbsResultSet(rs);
                    }else {
                        logger.info("har en feil ved retrieve dersom id ikke finnes");
                        return null;
                    }
                }
            }
        }
    }

    protected List<T> retrieveOfLists(long id, String commands) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    commands
            )) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(mapFromAbsResultSet(rs));
                    }
                    return list;
                }
            }
        }
    }

    protected void updateQuestion(long id, String commands) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    commands
            )) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }
        }
    }

    protected abstract T mapFromAbsResultSet(ResultSet rs) throws SQLException;
}
