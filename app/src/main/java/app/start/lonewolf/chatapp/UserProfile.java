package app.start.lonewolf.chatapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    private TextView displayName, status, friends;
    private ImageView displayPic;
    private Button send;
    private DatabaseReference databaseReference;
    private DatabaseReference friendRef;
    private FirebaseUser currentUser;
    private int currentState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        displayName = (TextView)findViewById( R.id.txtP_DP);
        status = (TextView)findViewById( R.id.txtP_US);
       friends= (TextView)findViewById(R.id.txt_fiends_Number);

        displayPic = (ImageView)findViewById(R.id.img_Profile);

        send = (Button)findViewById(R.id.btnSendRequest);

        final String User_Id = getIntent().getStringExtra("User_Id");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(User_Id);

        friendRef = FirebaseDatabase.getInstance().getReference().child("friend_Request");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newDisplayName = dataSnapshot.child("display_Name").getValue().toString();
                String newStatus = dataSnapshot.child("status").getValue().toString();
                String newDisPic = dataSnapshot.child("image").getValue().toString();

                displayName.setText(newDisplayName);
                status.setText(newStatus);
                Picasso.with(UserProfile.this).load(newDisPic).into(displayPic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send.setEnabled(false);
               // send.setBackground(R.color.colorPrimary);
                if(currentState==0){

                   friendRef.child(currentUser.getUid()).child(User_Id).child("request_type").setValue("sent")
                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               friendRef.child(User_Id).child(currentUser.getUid()).child("request_type").setValue("recieved")
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                            send.setEnabled(true);
                                           currentState = 1;
                                           send.setText("Cancel Request");

                                           Toast.makeText(UserProfile.this, "Friend Request Successfully sent", Toast.LENGTH_LONG).show();
                                       }
                                   }
                               });
                           }else{
                               Toast.makeText(UserProfile.this, "Friend Request Failed", Toast.LENGTH_LONG).show();

                           }
                       }
                   });

               }

               if(currentState==1){
                   friendRef.child(currentUser.getUid()).child(User_Id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               friendRef.child(User_Id).child(currentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       send.setEnabled(true);
                                       send.setText("Not Friends");
                                       Toast.makeText(UserProfile.this, "Request Revoked", Toast.LENGTH_LONG
                                       ).show();
                                   }
                               });
                           }else {
                               Toast.makeText(UserProfile.this, "Request Not Revoked", Toast.LENGTH_LONG
                               ).show();
                           }
                       }
                   });

               }

            }
        });



    }
}
