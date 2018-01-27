package app.start.lonewolf.chatapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Enter_Knowledge extends Fragment {

    private RecyclerView recyclerView;
    private EditText topic, body, author;
    private Button submit, close;
    private ImageView newImage;
    private DatabaseReference databaseReference, refId, storageId;;
    private StorageReference storageReference;
    private static final int newGallery = 1;
    private String newPicPath = "noPic", newCompPic = "noImage";
    private Integer pictureId =0000;
    private ProgressDialog progressDialog;

    public Frag_Enter_Knowledge() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag__enter__knowledge, container, false);

        topic = (EditText) view.findViewById(R.id.edtEnterTopic);
        body = (EditText)view.findViewById(R.id.edtEnterBody);
        author = (EditText)view.findViewById(R.id.edtEnterAuthor);

        submit = (Button)view.findViewById(R.id.btnKnowSubmit);
        close = (Button)view.findViewById(R.id.btnKnowCancel);
        progressDialog = new ProgressDialog(getActivity());

        newImage = (ImageView)view.findViewById(R.id.knowImageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("knowledge");
        databaseReference.keepSynced(true);
        refId = FirebaseDatabase.getInstance().getReference().child("userIdentifiers").child("knowledgeId").child("number");
        refId.keepSynced(true);
        storageId = FirebaseDatabase.getInstance().getReference().child("userIdentifiers").child("picId").child("number");
        storageId.keepSynced(true);

        storageReference = FirebaseStorage.getInstance().getReference().child("know_pics");
        storageId.keepSynced(true);



        setButtons();

        return view;
    }

    private void setButtons() {

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterData();
            }
        });

        newImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                       // .setAspectRatio(1,1)
                        .setMinCropWindowSize(500,500)
                        .start(getActivity());
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getActivity(), "Hell0", Toast.LENGTH_LONG).show();
        progressDialog.show();
        progressDialog.setMessage("Loading Picture");
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult actvityResult = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                final Uri uri = actvityResult.getUri();


                File actualImage = new File(uri.getPath());
                Bitmap compressedImage = new Compressor(getActivity())
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(actualImage);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumbByte = baos.toByteArray();


                storageId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        //Get pic Id
                        Integer getId = Integer.parseInt(dataSnapshot.getValue().toString())+1;
                        String setId = getId.toString();

                        //Main picture Path
                        StorageReference picPath = storageReference.child(setId).child("question"+setId+"pic.jpeg");
                        //Compressed Pic path
                        StorageReference compressedPath = storageReference.child(setId).child("compressed").child("comp"+setId+"pic.jpeg");

                        pictureId = getId;
                        storageId.setValue(setId);
                        UploadTask uploadTask = compressedPath.putBytes(thumbByte);

                        //Set the compressed Image
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    newCompPic = task.getResult().getDownloadUrl().toString();
                                    Toast.makeText(getActivity(), "Thumb file uploaded", Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getActivity(), "Thumb tile not uploaded", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        //set the main Image
                        picPath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    newPicPath = task.getResult().getDownloadUrl().toString();

                                    Picasso.with(getActivity()).load(newPicPath).into(newImage);
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Picture uploaded", Toast.LENGTH_LONG).show();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Picture not uploaded", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }


        }
    }

    public void enterData(){
        final String newTopic = topic.getText().toString();
        final String newBody = body.getText().toString();
        final String newAuthor = author.getText().toString();

        if(TextUtils.isEmpty(newTopic)){
            Toast.makeText(getActivity(), "Please Enter A Topic", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(newBody)){
            Toast.makeText(getActivity(), "Please Enter A Body", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(newAuthor)){
            Toast.makeText(getActivity(), "Please Enter author name", Toast.LENGTH_LONG).show();
        }else {
            progressDialog.show();
            progressDialog.setMessage("Uploading Article........");
            refId.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                   Integer newNum = Integer.parseInt(dataSnapshot.getValue().toString())+1;
                    String addnum = newNum.toString();

                    //databaseReference.removeValue();
                    databaseReference.child(addnum).child("topic").setValue(newTopic);
                    databaseReference.child(addnum).child("body").setValue(newBody);
                    databaseReference.child(addnum).child("author").setValue(newAuthor);
                    databaseReference.child(addnum).child("knowledgeId").setValue(addnum);
                    databaseReference.child(addnum).child("compPic").setValue(newCompPic);
                    databaseReference.child(addnum).child("mainPic").setValue(newPicPath);
                    databaseReference.child(addnum).child("picId").setValue(pictureId);

                    refId.setValue(addnum);
                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_LONG).show();
                    topic.setText("");
                    body.setText("");
                    author.setText("");
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });
        }
    }
}
