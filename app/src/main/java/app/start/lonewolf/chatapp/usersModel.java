package app.start.lonewolf.chatapp;

/**
 * Created by lonewolf on 8/4/2017.
 */

public class usersModel {

    public String display_Name;
    public String status;
    public String image;
    public String thumbNail_Image;

    public usersModel(){

    }

    public usersModel(String display_Name, String status, String image, String thumbNail_Image) {
        this.display_Name = display_Name;
        this.status = status;
        this.image = image;
        this.thumbNail_Image = thumbNail_Image;
    }

    public String getDisplay_Name() {
        return display_Name;
    }

    public void setDisplay_Name(String display_Name) {
        this.display_Name = display_Name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbNail_Image() {
        return thumbNail_Image;
    }

    public void setThumbNail_Image(String thumbNail_Image) {
        this.thumbNail_Image = thumbNail_Image;
    }
}
