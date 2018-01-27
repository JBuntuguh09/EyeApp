package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    private EditText displayName, email, password, confirmPass;
    private Button register, cancel;
    private DatabaseReference refDatabase;
    private FirebaseDatabase datab;
    private FirebaseAuth authReg;
    private ProgressDialog progressDialog;
    private Toolbar rToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //Edit Text
        displayName= (EditText) findViewById(R.id.regDisplayName);
        email = (EditText)findViewById(R.id.regEmail);
        password = (EditText)findViewById(R.id.regPassword);
        confirmPass = (EditText)findViewById(R.id.regConfirmPass);

        //Buttons
        register = (Button)findViewById(R.id.btnCreateAccount);
        cancel= (Button)findViewById(R.id.btnRegCancel);

        rToolbar = (Toolbar)findViewById(R.id.regAppBar);
        setSupportActionBar(rToolbar);
        getSupportActionBar().setTitle("Create Account");

        //Progress
        progressDialog = new ProgressDialog(this);

        //Firebase

        authReg = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });

    }

    private void createNewUser(){


        //getting String values of input
        final String newDisplayName = displayName.getText().toString();
        final String newEmail= email.getText().toString();
        final String newPass = password.getText().toString();
        final String newConfirm = confirmPass.getText().toString();

        if(TextUtils.isEmpty(newEmail)){
            Toast.makeText(RegisterPage.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(newPass)) {
            Toast.makeText(RegisterPage.this, "Please Enter a Password", Toast.LENGTH_LONG).show();
        }else if(!TextUtils.equals(newConfirm, newPass)){
            Toast.makeText(RegisterPage.this, "Password should be the same as the Confirm password", Toast.LENGTH_LONG).show();
        }else if(TextUtils.getTrimmedLength(newPass)<6){
            Toast.makeText(RegisterPage.this, "Password should be 6 or more charcters", Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Creating Account ..........");
            progressDialog.show();

            authReg.createUserWithEmailAndPassword(newEmail, newPass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId= currentUser.getUid();

                    refDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("display_Name", newDisplayName);
                    hashmap.put("status", "Hi, I'm Usin Eye HQ");
                    hashmap.put("image", "Default");
                    hashmap.put("thumbNail_Image", "Default");
                    hashmap.put("userType", "standard");
                    refDatabase.setValue(hashmap);



                    Intent regIntent = new Intent(RegisterPage.this, MainActivity.class);
                    startActivity(regIntent);
                    finish();

                    Toast.makeText(RegisterPage.this, "User Acount Create. Username : " + newEmail
                            + " Password : " + newPass, Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterPage.this, "Error. New Account not Created. Please Try again  ", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
