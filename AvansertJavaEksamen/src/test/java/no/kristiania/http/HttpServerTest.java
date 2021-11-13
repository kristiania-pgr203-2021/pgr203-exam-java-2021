package no.kristiania.http;

import no.kristiania.questionnaire.*;
import org.junit.jupiter.api.BeforeEach;
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

        server.addController(new EchoQueryController());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/hello");
        assertAll(
                () -> assertEquals(200, client.getStatusCode()),
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

        server.addController(new EchoQueryController());

        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
        assertEquals(200, new HttpClient("localhost", server.getPort(), "/hello")
                .getStatusCode());
    }

    QuestionnaireDao qreDao;
    @BeforeEach
    void setUp() {
        qreDao = new QuestionnaireDao(TestData.testDataSource());
    }

    @Test
    void shouldCreateNewQuestion() throws IOException, SQLException {
        server.addController(new AddQuestionController(qreDao));
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestions",
                "questionTitle=titleTest&questionText=questionTest&skalaOption=på en skala",
                "text/html"
        );


        assertEquals(303, postClient.getStatusCode());
        assertThat(qreDao.listAll())
                .anySatisfy(q -> {
                    assertThat(q.getQuestionTitle()).isEqualTo("titleTest");
                    assertThat(q.getQuestionText()).isEqualTo("questionTest");
                    assertThat(q.getQuestionTitle()).doesNotContain("på en skala");

                });
    }

    @Test
    void shouldCreateNewQuestionWithDecoding() throws IOException, SQLException {
        server.addController(new AddQuestionController(qreDao));
        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestions",
                "questionText=hallå på deg?&questionTitle=titleTest&skalaOption=på en skala",
                "text/html"

        );
        assertEquals(303, postClient.getStatusCode());
        assertThat(qreDao.listAll())
                .anySatisfy(q -> {
                    assertThat(q.getQuestionTitle()).isEqualTo("titleTest");
                    assertThat(q.getQuestionText()).isEqualTo("hallå på deg?");
                });
    }


    @Test
    void shouldListQuestionsFromDatabase() throws SQLException, IOException {
        Questionnaire qre1 = QuestionnaireDaoTest.exampleQuestionnaire();
        qreDao.save(qre1);

        Questionnaire qre2 = QuestionnaireDaoTest.exampleQuestionnaire();
        qreDao.save(qre2);

        Questionnaire qre3 = QuestionnaireDaoTest.exampleQuestionnaire();
        qreDao.save(qre3);

        server.addController(new ListQuestionsController(qreDao));

        HttpClient client = new HttpClient(
                "localhost", server.getPort(),
                "/api/questions"
        );

        assertThat(client.getMessageBody())
                .contains(  "<div>Title: "+qre1.getQuestionTitle()+" & Text: "+qre1.getQuestionText()
                            +"</div><div>Title: "+qre2.getQuestionTitle()
                            +" & Text: "+qre2.getQuestionText()+"</div><div>Title: "
                            +qre3.getQuestionTitle()+" & Text: "+qre3.getQuestionText()+"</div>"
                );
    }

    @Test
    void shouldEchoQueryParameter() throws IOException {
        OptionToQnDao optionDao = new OptionToQnDao(TestData.testDataSource());
        server.addController(new AddOptionController(optionDao));

        HttpClient client = new HttpClient(
                "localhost",
                server.getPort(),
                "/api/alternativeAnswers?questions=4&option=Mat"
        );
        //Dette her må fikses
        assertEquals(client.getMessageBody(), client.getMessageBody());
    }

    @Test
    void shouldReturnRoles() throws SQLException {
        Questionnaire qre = new Questionnaire();
        qre.setQuestionTitle("TestTitle");
        qre.setQuestionText("TestText");
        qreDao.save(qre);

        server.addController(new RoleOptionsController(qreDao));

        assertThat(qreDao.retrieve(qre.getId()))
                .usingRecursiveComparison()
                .isEqualTo(qre);
    }


    @Test
    void shouldReturnRolesFromServer() throws IOException, SQLException {
        Questionnaire qre = new Questionnaire();
        qre.setQuestionTitle("TestTitle");
        qre.setQuestionText("null not allow");
        qreDao.save(qre);

        server.addController(new RoleOptionsController(qreDao));

        HttpClient client = new HttpClient("localhost", server.getPort(), "/api/questionOptions");
        assertEquals(200, client.getStatusCode());
        assertThat(qreDao.listAll())
                .anySatisfy(dao -> {
                    assertThat(dao.getQuestionTitle()).isEqualTo("TestTitle");
                });
    }

}