package app.start.lonewolf.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lonewolf on 7/30/2017.
 */

class SessionsPagerAdapter extends FragmentPagerAdapter {
    public SessionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Frag_Request frag_request = new Frag_Request();
                return frag_request;
            case 1:
                Frag_Chat frag_chat = new Frag_Chat();
                return frag_chat;
            case 2:
                Frag_Friends frag_friends = new Frag_Friends();
                return frag_friends;
            default:
                return null;

        }
    }
/*
    public CharSequence getTitles(int position){
        switch (position){
            case 0:
                return "Requests";
            case 1 :
                return  "Chat";
            case 2 :
                return  "Friends";
            default:
                return null;
        }

    } */

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Requests";
            case 1:
                return "Chat";
            case 2:
                return "Friends";
            default:
                return null;
            //return super.getPageTitle(position);
        }
    }
}