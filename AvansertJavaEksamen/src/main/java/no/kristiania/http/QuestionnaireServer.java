package no.kristiania.http;

import no.kristiania.questionnaire.QuestionnaireDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class QuestionnaireServer {
    static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();

        QuestionnaireDao qreDao = new QuestionnaireDao(dataSource);
        HttpServer httpServer = new HttpServer(8000);
        httpServer.addController("/api/newQuestions", new AddQuestionController(qreDao));

        logger.info("Starting http://localhost:{}/index.html", + httpServer.getPort());
    }

    private static DataSource createDataSource() throws IOException {

        Properties prop = new Properties();
        try (FileReader reader = new FileReader("AvansertJavaEksamen/pgr203.properties")) {
            prop.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(prop.getProperty("dataSource.url"));
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
