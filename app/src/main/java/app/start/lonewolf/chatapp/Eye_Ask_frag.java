package app.start.lonewolf.chatapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_Ask_frag extends Fragment {
    private TextView logState, welcome;
    private TextView askQuestion, startChat;
    private SavedPreferences savedPreferences;
    private DatabaseReference databaseReference;
   // private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private String annon;


    public Eye_Ask_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View askView = inflater.inflate(R.layout.fragment_eye__ask_frag, container, false);

        askQuestion = (TextView)askView.findViewById(R.id.txtAskQuestion);
        startChat = (TextView)askView.findViewById(R.id.txtstartChat);
        auth= FirebaseAuth.getInstance();


        savedPreferences = new SavedPreferences(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        logState = (TextView)askView.findViewById(R.id.txtMainLogIn);
        welcome = (TextView)askView.findViewById(R.id.txtMainName);
        annon = savedPreferences.getIsAnnonymous();



if(annon.equals("no")){
    String fireUser = auth.getCurrentUser().getUid();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(fireUser).child("display_Name");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            welcome.setText("WELCOME, "+dataSnapshot.getValue().toString().toUpperCase()+", TO EYE CENTER");
            logState.setText("LOGOUT");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}




            logState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(annon.equals("yes")) {
                       //If logged out go to loggin page

                        logState.setText("LOGIN");
                        savedPreferences.setPrevPage("mainPage");
                        savedPreferences.setPerson_Id("logToMain");
                        Intent intent = new Intent(getActivity(), LoginPage.class);
                        startActivity(intent);


                    }else if(annon.equals("no")){
                        //If logged in, logout

                        String fireUser = auth.getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(fireUser).child("display_Name");

                        progressDialog.show();
                        progressDialog.setMessage("Please wait whilst we safely log you out");
                        auth.signOut();
                        Toast.makeText(getActivity(), "Successfully logged out", Toast.LENGTH_LONG).show();
                        logState.setText("LOGIN");
                        welcome.setText("Welcome to Eye Center");
                        savedPreferences.setIsAnnonymous("yes");
                        annon="yes";
                        progressDialog.dismiss();
                    }
                }
            });


        setaskQuest();
        setstartChat();

    return askView;
    }


    public void setaskQuest(){
        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent askIntent = new Intent(getActivity(), LoginPage.class);
                //askIntent.putExtra("setState", "questions");
                savedPreferences.setPerson_Id("questions");
                savedPreferences.setPrevPage("mainPage");
                startActivity(askIntent);
            }
        });
    }

    public void setstartChat(){
        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(getActivity(), LoginPage.class);
                //chatIntent.putExtra("setState", "chat");
                savedPreferences.setPerson_Id("chat");
                savedPreferences.setPrevPage("mainPage");
                startActivity(chatIntent);
            }
        });
    }

}
