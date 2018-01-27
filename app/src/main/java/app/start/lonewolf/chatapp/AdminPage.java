package app.start.lonewolf.chatapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class AdminPage extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private QuestionPageAdapter questionPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        toolbar = (Toolbar)findViewById(R.id.lay_admin_ques);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Main");

        viewPager = (ViewPager)findViewById(R.id.vpQues);
        questionPageAdapter = new QuestionPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(questionPageAdapter);

        tabLayout = (TabLayout)findViewById(R.id.TabQuestions);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }


    }
}
