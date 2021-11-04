package no.kristiania.http;

import javax.sql.DataSource;

public class QuestionnaireDao {


    private final DataSource dataSource;

    public QuestionnaireDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void save(Questionnaire questionnaire) {

    }

    public Questionnaire retrieve(long id) {
        return null;
    }
}
