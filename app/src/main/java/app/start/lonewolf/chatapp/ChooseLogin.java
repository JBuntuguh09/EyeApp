package app.start.lonewolf.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseLogin extends AppCompatActivity {

    private Button createAccount, loginAccount;
    private FirebaseAuth chooseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        createAccount= (Button)findViewById(R.id.btntoCreatePage);
        loginAccount = (Button)findViewById(R.id.btntoLoginPage);
        chooseAuth= FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regintent = new Intent(ChooseLogin.this, RegisterPage.class);
                startActivity(regintent);
            }
        });

        loginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chooseAuth.getCurrentUser()!=null){
                        Intent intent = new Intent(new Intent(ChooseLogin.this, ChatMainPage.class));
                        startActivity(intent);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                }else {

                    Intent logIntent = new Intent(ChooseLogin.this, LoginPage.class);
                    startActivity(logIntent);
                }
            }
        });



    }
}
