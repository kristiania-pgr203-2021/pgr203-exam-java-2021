package no.kristiania.Dao;

public class OptionToQn {
    private long id;
    private String option;
    private long question_fk;

    public long getQuestion_fk() {
        return question_fk;
    }

    public void setQuestion_fk(long question_fk) {
        this.question_fk = question_fk;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
