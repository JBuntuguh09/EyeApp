package app.start.lonewolf.chatapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_Tips_Frag extends Fragment {
    private TextView tip;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    public Eye_Tips_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eye__tips_, container, false);
        tip = (TextView)view.findViewById(R.id.txtTipsTip);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tips");
        databaseReference.keepSynced(true);
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.show();
        progressDialog.setMessage("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tip.setText(dataSnapshot.getValue().toString());

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


}
