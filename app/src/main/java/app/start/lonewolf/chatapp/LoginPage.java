package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.functions.Cancellable;

public class LoginPage extends AppCompatActivity {
    private Button loginBut, signUpBut;
    private EditText emailLog, passLog;
    private DatabaseReference logDataRef;
    private FirebaseAuth loginAuth;
    private ProgressDialog logProgress;
    private Toolbar logTool;
    private SavedPreferences savedPreferences;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //getting variables
        emailLog= (EditText)findViewById(R.id.logEmail);
        passLog= (EditText)findViewById(R.id.logPassword);
        loginBut = (Button)findViewById(R.id.butLogin);
        signUpBut = (Button)findViewById(R.id.btnSignUp);
        logTool= (Toolbar)findViewById(R.id.login_bar);
        setSupportActionBar(logTool);
        getSupportActionBar().setTitle("LOGIN");
        savedPreferences=new SavedPreferences(this);

        logDataRef= FirebaseDatabase.getInstance().getReference();
        loginAuth= FirebaseAuth.getInstance();
        logProgress = new ProgressDialog(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null && savedPreferences.getIsAnnonymous().equals("no")){
            authenticate();
        }else {

        }





        String state = getIntent().getStringExtra("setState");
       // Toast.makeText(LoginPage.this, "here : "+ state,Toast.LENGTH_LONG).show();
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginToApp();
            }
        });

        signUpBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPreferences.setPrevPage("mainPage");
                savedPreferences.setPerson_Id("logToMain");
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });


    }


    private void loginToApp(){
        String lEmail=emailLog.getText().toString();
        String lPass = passLog.getText().toString();

        if(TextUtils.isEmpty(lEmail)){
            Toast.makeText(LoginPage.this, "Enter valid Email", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(lPass)){
            Toast.makeText(LoginPage.this, "Enter valid Password", Toast.LENGTH_LONG).show();
        }else {
            logProgress.setCanceledOnTouchOutside(false);
            logProgress.setMessage("Logging In ...........");
            logProgress.show();

            loginAuth.signInWithEmailAndPassword(lEmail, lPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        savedPreferences.setIsAnnonymous("no");
                        authenticate();
                        Toast.makeText(LoginPage.this, "Successfully Logged in", Toast.LENGTH_LONG).show();
                    }else {

                        Toast.makeText(LoginPage.this, "Error. Unable to Login", Toast.LENGTH_LONG).show();
                        logProgress.dismiss();
                        ;


                    }
                }

            });

        }
    }

    private void authenticate(){


        String role = loginAuth.getCurrentUser().getUid();
        logDataRef= FirebaseDatabase.getInstance().getReference().child("users").child(role).child("userType");
        logDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userRole = dataSnapshot.getValue().toString();

                //Toast.makeText(LoginPage.this, userRole, Toast.LENGTH_LONG).show();
                savedPreferences.setUserType(userRole);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Toast.makeText(LoginPage.this, "Error. Role not stored", Toast.LENGTH_LONG).show();
            }
        });

        logProgress.dismiss();
        String state = savedPreferences.getChecker();
        String prevPage = savedPreferences.getpPrevPage();
        String userRole = savedPreferences.getUserType();
        // Toast.makeText(LoginPage.this, "here : "+ state,Toast.LENGTH_LONG).show();
        if(state.equals("questions") && prevPage.equals("mainPage")){
            Intent logIntent = new Intent(LoginPage.this, AskQuestion.class);
            // logIntent.setFlags()
            startActivity(logIntent);
            finish();
        }
        if(state.equals("chat") && prevPage.equals("mainPage")){
            Intent logIntent = new Intent(LoginPage.this, ChatMainPage.class);
            // logIntent.setFlags()
            startActivity(logIntent);
            finish();
        }

        if(state.equals("logToMain") && prevPage.equals("mainPage")){
            Intent logIntent = new Intent(LoginPage.this, MainActivity.class);
            // logIntent.setFlags()
            startActivity(logIntent);
            finish();
        }

        if(userRole.equals("admin") && prevPage.equals("tabMainPage")){
            Intent logIntent = new Intent(LoginPage.this, AdminPage.class);
            // logIntent.setFlags()
            startActivity(logIntent);
            finish();
        }

        if(userRole.equals("standard") && prevPage.equals("tabMainPage")){
            Intent logIntent = new Intent(LoginPage.this, UserPage.class);
            // logIntent.setFlags()
            startActivity(logIntent);
            finish();
        }

        // Toast.makeText(LoginPage.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
    }
}
