package no.kristiania.Dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionnaireDaoTest {

    QuestionnaireDao dao = new QuestionnaireDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedQuestion() throws SQLException {

        Questionnaire questionnaire = exampleQuestionnaire();
        dao.save(questionnaire);

        assertThat(dao.retrieve(questionnaire.getId()))
                .usingRecursiveComparison()
                .isEqualTo(questionnaire)
        ;

    }

    @Test
    void shouldListQuestionsByTitle() throws SQLException {

        Questionnaire matchingTitle = exampleQuestionnaire();
        matchingTitle.setQuestionTitle("TestTitle");
        dao.save(matchingTitle);

        Questionnaire nonMatchingTitle = exampleQuestionnaire();
        dao.save(nonMatchingTitle);

        assertThat(dao.listAll().contains(matchingTitle.getQuestionTitle()));
        assertThat(!dao.listAll().contains(nonMatchingTitle.getQuestionTitle()));

    }

    @Test
    void shouldListAllQuestions() throws SQLException {
        Questionnaire qre = exampleQuestionnaire();
        dao.save(qre);

        Questionnaire anotherQre = exampleQuestionnaire();
        dao.save(anotherQre);

        assertThat(dao.listAll())
                .extracting(Questionnaire::getId)
                .contains(qre.getId(), anotherQre.getId());
    }

    public static Questionnaire exampleQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionTitle(pickOne("Food", "Car", "Customers", "Education", "Healthcare"));
        questionnaire.setQuestionText(pickOne("What do you like?", "Do you wish?", "What is you job role", "What is your favorite"));
        return questionnaire;
    }

    private static String pickOne(String ... alternatives ) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }


}