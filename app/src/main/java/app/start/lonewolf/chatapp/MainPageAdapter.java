package app.start.lonewolf.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lonewolf on 8/5/2017.
 */

public class MainPageAdapter extends FragmentPagerAdapter {
    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Eye_Ask_frag eye_ask_frag = new Eye_Ask_frag();
                return  eye_ask_frag;

            case 1:
                Eye_Know_frag eye_know_frag = new Eye_Know_frag();
                return eye_know_frag;
            case 2:
                Eye_Tips_Frag eye_tips_frag = new Eye_Tips_Frag();
                return eye_tips_frag;
            case 3:
                Freebies_Frag freebies_frag = new Freebies_Frag();
                return freebies_frag;
            default:
                return null;

        }





    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Eye Ask";
            case 1:
                return  "Eye Know";
            case 2:
                return "Eye Tips";
            case 3:
                return "Freebies";
            default:
                return null;
        }

       // return super.getPageTitle(position);
    }
}
