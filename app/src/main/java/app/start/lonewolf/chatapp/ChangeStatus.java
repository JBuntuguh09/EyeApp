package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeStatus extends AppCompatActivity {

    private Toolbar sToolbar;
    private EditText status;
    private Button butUpdate;
    private DatabaseReference sref;
    private ProgressDialog progressDialog;
    //private FirebaseAuth userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);

        sToolbar= (Toolbar)findViewById(R.id.changeStaTab);
        setSupportActionBar(sToolbar);
        getSupportActionBar().setTitle("User Status");
        getSupportActionBar().setHomeButtonEnabled(true);

        status = (EditText)findViewById(R.id.edtStatus);
        butUpdate = (Button)findViewById(R.id.butStatus);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        sref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        sref.keepSynced(true);

        progressDialog = new ProgressDialog(this);

        sref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String fre=dataSnapshot.child("status").getValue().toString();
                status.setText(fre);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });


    }

    private void updateStatus(){
        String newStatus = status.getText().toString();
        progressDialog.setMessage("Updating Status");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        sref.child("status").setValue(newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ChangeStatus.this, "Successfully changed Status", Toast.LENGTH_LONG).show();

                }else
                {
                    progressDialog.dismiss();;
                    Toast.makeText(ChangeStatus.this, "Failed to changed Status", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}
