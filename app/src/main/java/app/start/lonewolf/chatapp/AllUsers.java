package app.start.lonewolf.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AllUsers extends AppCompatActivity {

    private Toolbar utoolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private StorageReference allStorage;
    private ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        utoolbar = (Toolbar)findViewById(R.id.user_app_bar);
        setSupportActionBar(utoolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllUsers.this));
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.keepSynced(true);
        allStorage = FirebaseStorage.getInstance().getReference().child("profile pic");






    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<usersModel, usersViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<usersModel, usersViewHolder>(
                usersModel.class,
                R.layout.users_single_layout,
                usersViewHolder.class,
                databaseReference.orderByChild("display_Name")

        ) {
            @Override
            protected void populateViewHolder(usersViewHolder viewHolder, usersModel model, int position) {
                viewHolder.setdisplay_Name(model.getDisplay_Name());
                viewHolder.setstatus(model.getStatus());
                viewHolder.setPic(getApplicationContext(), model.getThumbNail_Image());

                final String UserId= getRef(position).getKey();
                usersViewHolder.uview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profileItent = new Intent(AllUsers.this, UserProfile.class);
                        profileItent.putExtra("User_Id", UserId);
                        startActivity(profileItent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    public static class usersViewHolder extends RecyclerView.ViewHolder{
        static View uview;
        public usersViewHolder(View itemView) {
            super(itemView);
                    

            uview=itemView;
        }

        private void setdisplay_Name(String display_Name ){
            TextView newName = (TextView)uview .findViewById(R.id.singleDisplayName);
            newName.setText(display_Name);
        }

        private void setstatus(String status ){
            TextView newStatus = (TextView)uview .findViewById(R.id.singleDStatus);
            newStatus.setText(status);
        }

        private void  setPic(final Context ctx, final String thumnail ){


            ImageView newpic = (ImageView) uview .findViewById(R.id.circleImageView);
            Picasso.with(ctx).load(thumnail).into(newpic);
        }
    }

}
