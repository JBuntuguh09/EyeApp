package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AskQuestion extends AppCompatActivity {
    private EditText title, details;
    private Button submit, cancel;
    private Toolbar askToolbar;
    private DatabaseReference askDatabase, askDatabaseId, userStuff;
    private FirebaseAuth askAuth,askAuth2;
    private FirebaseUser askUser;
    private ProgressDialog askProgress;
    private Integer newNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        askToolbar = (Toolbar)findViewById(R.id.askQuesLayout);
        setSupportActionBar(askToolbar);
        getSupportActionBar().setTitle("Ask A Question");

        title = (EditText)findViewById(R.id.edtQuestionTopic);
        details = (EditText)findViewById(R.id.edtSubject);
        submit = (Button)findViewById(R.id.btnQuesSubmit);
        cancel = (Button)findViewById(R.id.btnQuesCancel);
        askProgress = new ProgressDialog(this);

        askDatabaseId = FirebaseDatabase.getInstance().getReference().child("userIdentifiers")
                .child("anonymousId").child("number");
        askDatabaseId.keepSynced(true);
        askDatabase= FirebaseDatabase.getInstance().getReference().child("questions");
        askDatabase.keepSynced(true);
        askAuth= FirebaseAuth.getInstance();
        String askId = askAuth.getCurrentUser().getUid();

        userStuff = FirebaseDatabase.getInstance().getReference().child("users")
                .child(askId);
        userStuff.keepSynced(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuention();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskQuestion.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void sendQuention(){
        askAuth2 = FirebaseAuth.getInstance();
        final String uId = askAuth2.getCurrentUser().getUid();
        final String newTitle = title.getText().toString();
        final String newDetail = details.getText().toString();
        askProgress.show();
        askProgress.setMessage("Please wait whilst we send your message");

        userStuff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                final String userName = dataSnapshot.child("display_Name").getValue().toString();
                //final String email = dataSnapshot.child("")

                askDatabaseId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        String newId = dataSnapshot2.getValue().toString();
                        Integer number = (Integer.parseInt(newId))+1;
                        askDatabaseId.setValue(("Question "+number.toString()).toString());
                        //newNumber = (Integer.parseInt(askDatabaseId.toString()))+1;
                        //askUser = FirebaseAuth.getInstance().getCurrentUser();
                        //String UserId = askUser.getUid();


                        askDatabase.child(number.toString()).child("title").setValue(newTitle);
                        askDatabase.child(number.toString()).child("details").setValue(newDetail);
                        askDatabase.child(number.toString()).child("questionId").setValue(number.toString());
                        askDatabase.child(number.toString()).child("email").setValue("email100");
                        askDatabase.child(number.toString()).child("username").setValue(userName);
                        askDatabase.child(number.toString()).child("uniqueId").setValue(uId);


                        askDatabaseId.setValue(number.toString());
                        title.setText("");
                        details.setText("");
                        Toast.makeText(AskQuestion.this, "Comment Successfully Sent ", Toast.LENGTH_LONG).show();

                        askProgress.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AskQuestion.this, databaseError.toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






       // askDatabase.child("title").setValue(newTitle);
       // askDatabase.child("details").setValue(newDetail);


    }
}
