package no.kristiania.http;

import no.kristiania.questionnaire.Questionnaire;
import no.kristiania.questionnaire.QuestionnaireDao;
import no.kristiania.questionnaire.QuestionnaireDaoTest;
import no.kristiania.questionnaire.TestData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

    HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknowRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldResponseWithRequestTargetIn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals("File not found: /non-existing", client.getMessageBody());
    }

    @Test
    void shouldRespondWith200ForKnownRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/hello");
        assertAll(
                () -> assertEquals(200, client.getStatusCode()),
                () -> assertEquals("Hello world", client.getMessageBody())
        );
    }
    @Test
    void shouldReturnContentType200() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/shouldReturnContent");
        assertAll(
                () -> assertEquals(200, client.getStatusCode()),
                () -> assertEquals("text/html", client.getHeader("Content-Type")),
                () -> assertEquals("<p>Hello world</p>", client.getMessageBody())
        );
    }

    @Test
    void shouldServeFiles() throws IOException {

        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");

        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }


    @Test
    void shouldUseFileExtensionForContentType() throws IOException {

        String fileContent = "<p>Hello world</p>";
        Files.write(Paths.get("target/test-classes/example-file.html"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.html");
        assertEquals("text/html", client.getHeader("Content-Type"));

    }

    @Test
    void shouldHandleMoreThanOneRequest() throws IOException {
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
    }


    @Test
    void shouldCreateNewQuestion() throws IOException, SQLException {
        QuestionnaireDao qreDao = new QuestionnaireDao(TestData.testDataSource());
        server.addController("/api/newQuestions", new AddQuestionController(qreDao));
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestions",
                "questionText=questionTest&questionTitle=titleTest",
                "text/html"
        );
        assertEquals(200, postClient.getStatusCode());
        assertThat(qreDao.listAll())
                .anySatisfy(q -> {
                    assertThat(q.getQuestionTitle()).isEqualTo("titleTest");
                    assertThat(q.getQuestionText()).isEqualTo("questionTest");
                });
    }

    @Test
    void shouldCreateNewQuestionWithDecoding() throws IOException, SQLException {
        QuestionnaireDao qreDao = new QuestionnaireDao(TestData.testDataSource());
        server.addController("/api/newQuestions", new AddQuestionController(qreDao));
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestions",
                "questionText=hallå på deg?&questionTitle=titleTest",
                "text/html"

        );
        assertEquals(200, postClient.getStatusCode());
        assertThat(qreDao.listAll())
                .anySatisfy(q -> {
                    assertThat(q.getQuestionTitle()).isEqualTo("titleTest");
                    assertThat(q.getQuestionText()).isEqualTo("hallå på deg?");
                });
    }

    @Test
    void shouldListQuestionsFromDatabase() throws SQLException, IOException {
        QuestionnaireDao qreDao = new QuestionnaireDao(TestData.testDataSource());

        Questionnaire qre1 = QuestionnaireDaoTest.exampleQuestionnaire();
        qreDao.save(qre1);

        Questionnaire qre2 = QuestionnaireDaoTest.exampleQuestionnaire();
        qreDao.save(qre2);

        server.addController("/api/questions", new ListQuestionsController(qreDao));

        HttpClient client = new HttpClient(
                "localhost", server.getPort(),
                "/api/questions"
        );

        assertThat(client.getMessageBody())
                .contains(qre1.getQuestionTitle() + ", " + qre1.getQuestionText())
                .contains(qre2.getQuestionTitle() + ", " + qre2.getQuestionText())
                ;
    }

}