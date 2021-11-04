package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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