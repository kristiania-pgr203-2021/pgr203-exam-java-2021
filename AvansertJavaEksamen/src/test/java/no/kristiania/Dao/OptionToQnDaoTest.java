package no.kristiania.Dao;

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

    @Test
    void shouldListAllOptions() throws SQLException {
        OptionToQn optionQn = exampleOptionToQn();
        dao.save(optionQn);

        OptionToQn anotherOption = exampleOptionToQn();
        dao.save(anotherOption);

        assertThat(dao.listAll())
                .extracting(OptionToQn::getId)
                .contains(optionQn.getId(), anotherOption.getId());
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
