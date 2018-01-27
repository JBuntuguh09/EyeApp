package app.start.lonewolf.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class AnsQuestion extends AppCompatActivity {
    private Button submit, cancel;
    private TextView name, title, question;
    private EditText answer;
    private DatabaseReference databaseReference, senDatabaseReference, refNumber;
    private SavedPreferences savedPreferences;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ans_question);

        toolbar = (Toolbar)findViewById(R.id.ansBarLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ans Question");


        name = (TextView)findViewById(R.id.aName);
        title = (TextView)findViewById(R.id.aTitle);
        question = (TextView)findViewById(R.id.aQuestion);
        answer = (EditText)findViewById(R.id.edtAnsQuestion);
        submit = (Button)findViewById(R.id.btnAnsSubmit);
        cancel = (Button)findViewById(R.id.btnAnsCancel);

        savedPreferences = new SavedPreferences(this);
        String UserId = savedPreferences.getUserId();

        refNumber =  FirebaseDatabase.getInstance().getReference().child("userIdentifiers")
                .child("answerId").child("number");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("questions").child(UserId);
        senDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Answers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String newName = dataSnapshot.child("username").getValue().toString();
                final String newTitle = dataSnapshot.child("title").getValue().toString();
                final String newQuestion = dataSnapshot.child("details").getValue().toString();
                final String newUid = dataSnapshot.child("uniqueId").getValue().toString();

                name.setText(newName);
                title.setText(newTitle);
                question.setText(newQuestion);

                refNumber.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String newAns = answer.getText().toString();
                                if (TextUtils.isEmpty(newAns)) {
                                    Toast.makeText(AnsQuestion.this, "Please Enter an Ans to the Question", Toast.LENGTH_LONG).show();

                                } else {
                                    String ansNum = dataSnapshot.getValue().toString();
                                    Integer newNum = (Integer.parseInt(ansNum)) + 1;
                                    refNumber.setValue("Ans " + newNum);

                                    senDatabaseReference.child(newUid).child(newNum.toString()).child("name").setValue(newName);
                                    senDatabaseReference.child(newUid).child(newNum.toString()).child("question_title").setValue(newTitle);
                                    senDatabaseReference.child(newUid).child(newNum.toString()).child("question_details").setValue(newQuestion);
                                    senDatabaseReference.child(newUid).child(newNum.toString()).child("Answer").setValue(newAns);
                                    senDatabaseReference.child(newUid).child(newNum.toString()).child("AnswerId").setValue(newNum);

                                    refNumber.setValue(newNum.toString());

                                    answer.setText("");
                                    Toast.makeText(AnsQuestion.this, "Question Answered", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(AnsQuestion.this, AdminPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AnsQuestion.this, AdminPage.class);
                                startActivity(intent);
                                finish();
                            }
                        });



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
