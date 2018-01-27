package app.start.lonewolf.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lonewolf on 8/13/2017.
 */

public class QuestionPageAdapter extends FragmentPagerAdapter {
    public QuestionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Frag_Ques_Admin frag_ques_admin = new Frag_Ques_Admin();
                return frag_ques_admin;
            case 1:
                Frag_Enter_Knowledge frag_enter_knowledge = new Frag_Enter_Knowledge();
                return frag_enter_knowledge;
            case 2:
                Frag_Send_Tips frag_send_tips = new Frag_Send_Tips();
                return frag_send_tips;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Question";
            case 1:
                return "Post Info";
            case 2:
                return "Post Tips";
            default:
                return null;
        }
        //return super.getPageTitle(position);
    }
}
