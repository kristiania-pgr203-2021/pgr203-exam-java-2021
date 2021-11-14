package no.kristiania.Dao;

public class Scale {
    private long id;
    private String scaleValue;
    private long questionScaleFk;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getScaleValue() {
        return scaleValue;
    }

    public void setScaleValue(String scaleValue) {
        this.scaleValue = scaleValue;
    }

    public long getQuestionScaleFk() {
        return questionScaleFk;
    }

    public void setQuestionScaleFk(long questionScaleFk) {
        this.questionScaleFk = questionScaleFk;
    }

    @Override
    public String toString() {
        return "Scale{" +
                "scaleValue='" + scaleValue + '\'' +
                '}';
    }
}
