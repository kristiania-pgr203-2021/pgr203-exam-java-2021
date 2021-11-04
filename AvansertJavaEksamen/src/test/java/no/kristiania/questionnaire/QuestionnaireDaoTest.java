package no.kristiania.questionnaire;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionnaireDaoTest {

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

        Questionnaire anotherMatchingTitle = exampleQuestionnaire();
        anotherMatchingTitle.setQuestionTitle(matchingTitle.getQuestionTitle());
        dao.save(anotherMatchingTitle);

        Questionnaire nonMatchingTitle = exampleQuestionnaire();
        dao.save(nonMatchingTitle);

        assertThat(dao.listByTitle(matchingTitle.getQuestionTitle()))
                .extracting(Questionnaire::getId)
                .contains(matchingTitle.getId(), anotherMatchingTitle.getId())
                .doesNotContain(nonMatchingTitle.getId());

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

    private Questionnaire exampleQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setQuestionTitle(pickOne("Food", "Car", "Customers", "Education", "Healthcare"));
        questionnaire.setQuestionText(pickOne("What do you like?", "Do you wish?", "What is you job role", "What is your favorite"));
        return questionnaire;
    }

    private static String pickOne(String ... alternatives ) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }


}