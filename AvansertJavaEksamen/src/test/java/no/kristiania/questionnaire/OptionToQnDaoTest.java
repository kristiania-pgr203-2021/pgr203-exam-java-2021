package no.kristiania.questionnaire;


import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionToQnDaoTest {

    private OptionToQnDao dao = new OptionToQnDao(TestData.testDataSource());

    @Test
    void shouldRetrieveOptionToQn() throws SQLException {
        OptionToQn optionToQn = exampleOptionToQn();
        dao.save(optionToQn);

        OptionToQn anotherOptionToQn = exampleOptionToQn();
        dao.save(anotherOptionToQn);

        assertThat(dao.retrieve(optionToQn.getId()))
                .usingRecursiveComparison()
                .isEqualTo(optionToQn)
        ;
    }


    private OptionToQn exampleOptionToQn() {
        OptionToQn option = new OptionToQn();
        option.setOption(pickOne("Food", "Car", "Customers", "Education", "Healthcare"));
        return option;
    }

    private static String pickOne(String ... alternatives ) {
        return alternatives[new Random().nextInt(alternatives.length)];
    }


}
