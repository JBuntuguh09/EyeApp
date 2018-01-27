package app.start.lonewolf.chatapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatMainPage extends AppCompatActivity {
    private FirebaseAuth chatAuth;
    private FirebaseAuth.AuthStateListener chatAuthListener;
    private Toolbar ctoolbar;
    private ViewPager chatViewPager;
    private SessionsPagerAdapter sessionsPagerAdapter;
    private TabLayout cTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main_page);

        chatAuth= FirebaseAuth.getInstance();
        //chatAuth.addAuthStateListener(chatAuthListener);
        ctoolbar = (Toolbar)findViewById(R.id.chart_lay);
        setSupportActionBar(ctoolbar);
        getSupportActionBar().setTitle("EYE HQ");

        chatViewPager = (ViewPager)findViewById(R.id.mainchatVP);
        sessionsPagerAdapter = new SessionsPagerAdapter(getSupportFragmentManager());
        chatViewPager.setAdapter(sessionsPagerAdapter);

        cTabLayout = (TabLayout)findViewById(R.id.ChatTabLay);
        cTabLayout.setupWithViewPager(chatViewPager);


        checkState();

    }
    private void checkState(){
        chatAuth= FirebaseAuth.getInstance();
        chatAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             if(chatAuth.getCurrentUser()==null){
                 startActivity(new Intent(ChatMainPage.this, LoginPage.class));
                 finish();
             }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = chatAuth.getCurrentUser();
        if(currentuser==null){
            startActivity(new Intent(ChatMainPage.this, ChooseLogin.class));
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout){
            logOut();
        }

        if(item.getItemId()==R.id.settings){
            Intent setIntent = new Intent(ChatMainPage.this, SettingsPage.class);
            startActivity(setIntent);
            //finish();
        }

        if(item.getItemId()==R.id.allUsersPage){
            Intent setIntent = new Intent(ChatMainPage.this, AllUsers.class);
            startActivity(setIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        chatAuth.signOut();
        Intent logoutIntent = new Intent(ChatMainPage.this, LoginPage.class);
        startActivity(logoutIntent);
        finish();
    }
}
