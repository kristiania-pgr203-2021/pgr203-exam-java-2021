package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionnaireDaoTest {

    QuestionnaireDao dao = new QuestionnaireDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedQuestion(){


        Questionnaire questionnaire = exampleQuestionnaire();
        dao.save(questionnaire);

        assertThat(dao.retrieve(questionnaire.getId()))
                .usingRecursiveComparison()
                .isEqualTo(questionnaire)
        ;

    }

    private Questionnaire exampleQuestionnaire() {
        return new Questionnaire();
    }


}