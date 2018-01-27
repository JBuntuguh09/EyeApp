package app.start.lonewolf.chatapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //private Button but ;
    private FirebaseAuth auth;
    private Toolbar jtoolbar;
    private ViewPager MPviewPager;
    private TabLayout MPtabLayout;
    private MainPageAdapter MPmainPageAdapter;
    private SavedPreferences savedPreferences;
   // private FirebaseUser firebaseUser;
    //private String setState;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jtoolbar = (Toolbar)findViewById(R.id.MainPageBarLay);
        setSupportActionBar(jtoolbar);
        getSupportActionBar().setTitle("Main Page");

        MPviewPager = (ViewPager)findViewById(R.id.MainPageViewPager);
        MPmainPageAdapter = new MainPageAdapter(getSupportFragmentManager());
        MPviewPager.setAdapter(MPmainPageAdapter);
        //auth = FirebaseAuth.getInstance();
        MPtabLayout = (TabLayout)findViewById(R.id.MainPageTabLayout);
        MPtabLayout.setupWithViewPager(MPviewPager);
        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //String fireUser = firebaseUser.getUid();
        savedPreferences = new SavedPreferences(this);

/*
        if(firebaseUser!=null ){

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        }
*/

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.start_menu,menu);

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.myPage){
            Intent allUsers = new Intent(MainActivity.this, LoginPage.class );
            savedPreferences.setPrevPage("tabMainPage");
            startActivity(allUsers);
        }

        if((item.getItemId()==R.id.mainSignUp)){
            savedPreferences.setPrevPage("mainPage");
            savedPreferences.setPerson_Id("logToMain");
            Intent intent = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(intent);
            //Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_LONG).show();

        }



        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        auth.signOut();
    }
}
