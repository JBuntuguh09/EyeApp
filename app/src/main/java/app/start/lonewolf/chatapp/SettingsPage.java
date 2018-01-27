package app.start.lonewolf.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class SettingsPage extends AppCompatActivity {

    private static final int MAX_LENGTH = 30 ;
    private FirebaseAuth setAuth;
    private DatabaseReference setDatabase;
    private TextView displayName, status;
    private ImageView profilePic;
    private Button changePic, changeStatus;
    private static final int galler_Int=1;
    private StorageReference setStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        displayName = (TextView)findViewById(R.id.txtDisName);
        status = (TextView)findViewById(R.id.txtDisStatus);
        changePic = (Button)findViewById(R.id.butChangeImage);
        changeStatus = (Button)findViewById(R.id.butChangeStatus);
        profilePic = (ImageView) findViewById(R.id.disPic);


        setAuth = FirebaseAuth.getInstance();
        FirebaseUser currUser = setAuth.getCurrentUser();
        String UserId = currUser.getUid();
        setDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(UserId);
        setDatabase.keepSynced(true);
        setStorage = FirebaseStorage.getInstance().getReference().child("profile pic");

        setDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newName = dataSnapshot.child("display_Name").getValue().toString();
               String newstatus = dataSnapshot.child("status").getValue().toString();
                String newPic = dataSnapshot.child("image").getValue().toString();

                displayName.setText(newName);
                status.setText(newstatus);
                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);


                if(!newPic.equals("Default")) {
                    Picasso.with(SettingsPage.this).load(newPic).into(profilePic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsPage.this, ChangeStatus.class));

            }
        });

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), galler_Int); */

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .setMinCropWindowSize(500,500)
                        .start(SettingsPage.this);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();


                File actualImageFile= new File(resultUri.getPath());
                Bitmap compressedImageBitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75).
                        compressToBitmap(actualImageFile);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = setStorage.child(user+".jpeg");
                StorageReference thumbPath = setStorage.child("Thumbfiles").child(user+".jpeg");

                UploadTask uploadTask = thumbPath.putBytes(thumb_byte);

               // upload Thumb file
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            String thumb_link = task.getResult().getDownloadUrl().toString();
                            setDatabase.child("thumbNail_Image").setValue(thumb_link).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SettingsPage.this, "Thumb file uploaded", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else
                        {
                            Toast.makeText(SettingsPage.this, "Thumb file not uploaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Upload Main file
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            String downloadLink = task.getResult().getDownloadUrl().toString();

                            setDatabase.child("image").setValue(downloadLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SettingsPage.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                                }
                            });

                        }else {
                            Toast.makeText(SettingsPage.this, "UnSuccessfully uploaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
