package app.start.lonewolf.chatapp;

/**
 * Created by lonewolf on 8/13/2017.
 */

public class knoowledgeModel {

    private String topic, compPic, body, date;

    public knoowledgeModel(){

    }


    public knoowledgeModel(String topic, String compPic, String body, String date) {
        this.topic = topic;
        this.body = body;
        this.date = date;
        this.compPic = compPic;

    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompPic() {
        return compPic;
    }

    public void setCompPic(String compPic) {
        this.compPic = compPic;
    }
}
