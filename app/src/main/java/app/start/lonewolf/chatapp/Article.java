package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Article extends AppCompatActivity {
    private Toolbar toolbar;
    private Button close;
    private TextView topic, article, author;
    private ImageView articleImage;
    private DatabaseReference databaseReference;
    private SavedPreferences savePreferences;
    private String artId;
    private ProgressDialog progressdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        toolbar = (Toolbar)findViewById(R.id.articleAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Know your Eye");
        topic = (TextView)findViewById(R.id.articleTopic);
        article = (TextView)findViewById(R.id.articleText);
        author = (TextView)findViewById(R.id.articleAuthor);
        articleImage = (ImageView)findViewById(R.id.articleImage);
        savePreferences = new SavedPreferences(this);
        progressdialog = new ProgressDialog(this);
         artId = savePreferences.getArticleId();
      //  int newId = Integer.parseInt(artid);
       databaseReference = FirebaseDatabase.getInstance().getReference().child("knowledge");
        databaseReference.keepSynced(true);

        setdata();
    }

    private void setdata() {
        progressdialog.show();
        progressdialog.setMessage("Retrieving Article");
        progressdialog.setCanceledOnTouchOutside(false);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    topic.setText(dataSnapshot.child(artId).child("topic").getValue().toString());
                    article.setText(dataSnapshot.child(artId).child("body").getValue().toString());
                    author.setText(dataSnapshot.child(artId).child("author").getValue().toString());
                    String newImage = dataSnapshot.child(artId).child("mainPic").getValue().toString();
                    if(!newImage.equals("noPic")){
                        Picasso.with(Article.this).load(newImage).into(articleImage);
                    }
                    progressdialog.dismiss();
                    //Toast.makeText(Article.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    progressdialog.dismiss();
                    e.printStackTrace();
                    //Toast.makeText(Article.this, "Failed to Sync", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    progressdialog.dismiss();
            }
        });
    }


}
