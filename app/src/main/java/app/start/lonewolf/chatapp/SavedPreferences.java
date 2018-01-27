package app.start.lonewolf.chatapp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.SharedPreferences.*;

/**
 * Created by lonewolf on 8/10/2017.
 */

public class SavedPreferences {

    private final String PREF_NAME = "saved_preference";
    private final String pchecker = "check";
    private SharedPreferences sharedPreferences;
    private final String pUserId = "user_id";
    private final String pUserType = "user_type";
    private final String pPrevPage = "previous_Page";
    private final String pArticle_Id = "articleId";
    private final String pIsAnnonymous = "annonymous";


    public SavedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
    }

    public Editor getEditor() {
        Editor editor = sharedPreferences.edit();
        return editor;
    }

    public SharedPreferences getPref() {
        return sharedPreferences;
    }

    public void setPerson_Id(String checker){
        Editor editor = getEditor();
        editor.putString(pchecker, checker);
        editor.commit();

    }

    public String getChecker(){
    return sharedPreferences.getString(pchecker, "");
}


    public void setUserId(String uSerId){
        Editor editor = getEditor();
        editor.putString(pUserId, uSerId);
        editor.commit();

    }

    public String getUserId(){
        return sharedPreferences.getString(pUserId, "");
    }

    public void setUserType(String userType){
        Editor editor = getEditor();
        editor.putString(pUserType, userType);
        editor.commit();

    }

    public String getUserType(){
        return sharedPreferences.getString(pUserType,"");
    }

    public void setPrevPage(String prevPage){
        Editor editor = getEditor();
        editor.putString(pPrevPage, prevPage);
        editor.commit();
    }

    public String getpPrevPage(){
        return sharedPreferences.getString(pPrevPage, "");
    }


    public String getArticleId(){
        return sharedPreferences.getString(pArticle_Id, "");
    }

    public void setArticleId(String artId) {
        Editor editor = getEditor();
        editor.putString(pArticle_Id, artId);
        editor.commit();
    }


    public String getIsAnnonymous(){
        return sharedPreferences.getString(pIsAnnonymous, "");
    }

    public void setIsAnnonymous(String annonymous){
        Editor editor = getEditor();
        editor.putString(pIsAnnonymous, annonymous);
        editor.commit();

    }
}


