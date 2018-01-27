package app.start.lonewolf.chatapp;

/**
 * Created by lonewolf on 8/13/2017.
 */

public class QuestionsModel {
    private String username, details, questionId,title, email;
    public QuestionsModel(){

    }


    public QuestionsModel(String userName, String details, String questionId, String title, String email) {
        this.username = userName;
        this.details = details;
        this.questionId = questionId;
        this.title = title;
        this.email = email;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
