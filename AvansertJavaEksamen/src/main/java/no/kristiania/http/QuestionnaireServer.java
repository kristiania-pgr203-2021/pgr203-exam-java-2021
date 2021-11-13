package no.kristiania.http;

import no.kristiania.AddController.AddOptionController;
import no.kristiania.AddController.AddQuestionController;
import no.kristiania.AddController.AddScaleController;
import no.kristiania.AddController.RoleOptionsController;
import no.kristiania.UpdateController.UpdateQuestionTextController;
import no.kristiania.UpdateController.UpdateQuestionTitleController;
import no.kristiania.defaultController.EchoQueryController;
import no.kristiania.filterController.FilterBySearchingController;
import no.kristiania.filterController.FilterByTitleController;
import no.kristiania.filterController.FilterTitleOptionController;
import no.kristiania.listController.ListAllWithScaleAndOption;
import no.kristiania.listController.ListQuestionsController;
import no.kristiania.questionnaire.OptionToQnDao;
import no.kristiania.questionnaire.QuestionnaireDao;
import no.kristiania.questionnaire.ScaleDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class QuestionnaireServer {
   public static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        DataSource dataSource = createDataSource();
        QuestionnaireDao qreDao = new QuestionnaireDao(dataSource);
        OptionToQnDao option = new OptionToQnDao(dataSource);
        ScaleDao scaleDao = new ScaleDao(dataSource);

        HttpServer httpServer = new HttpServer(8000);

        httpServerControllers(qreDao, option, scaleDao, httpServer);

        logger.info("Starting http://localhost:{}/index.html", + httpServer.getPort());
    }

    private static void httpServerControllers(QuestionnaireDao qreDao, OptionToQnDao option, ScaleDao scaleDao, HttpServer httpServer) {
        httpServer.addController(new EchoQueryController());
        httpServer.addController(new AddOptionController(option, scaleDao));
        httpServer.addController(new AddScaleController(scaleDao));
        httpServer.addController(new RoleOptionsController(qreDao, scaleDao));
        httpServer.getControllers().put("/api/questionOptionsSkala", new RoleOptionsController(qreDao, scaleDao));
        httpServer.getControllers().put("/api/textOptions", new RoleOptionsController(qreDao));

        httpServer.addController(new UpdateQuestionTitleController(qreDao));
        httpServer.addController(new UpdateQuestionTextController(qreDao));

        httpServer.addController(new AddQuestionController(qreDao));
        httpServer.addController(new ListQuestionsController(qreDao));
        httpServer.addController(new FilterTitleOptionController(qreDao));
        httpServer.addController(new FilterByTitleController(qreDao));
        httpServer.addController(new FilterBySearchingController(qreDao));
        httpServer.addController(new ListAllWithScaleAndOption(qreDao));
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
