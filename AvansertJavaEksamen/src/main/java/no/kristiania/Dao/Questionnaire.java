package no.kristiania.Dao;

public class Questionnaire {
    private long id;
    private String questionTitle;
    private String questionText;
    private String optionForQuestion;

    private String scaleForQuestion;

    public String getScaleForQuestion() {
        return scaleForQuestion;
    }

    public void setScaleForQuestion(String scaleForQuestion) {
        this.scaleForQuestion = scaleForQuestion;
    }

    public String getOptionForQuestion() {
        return optionForQuestion;
    }

    public void setOptionForQuestion(String optionForQuestion) {
        this.optionForQuestion = optionForQuestion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "questionTitle='" + questionTitle + '\'' +
                ", questionText='" + questionText + '\'' +
                ", optionForQuestion='" + optionForQuestion + '\'' +
                ", scaleForQuestion='" + scaleForQuestion + '\'' +
                '}';
    }
}
