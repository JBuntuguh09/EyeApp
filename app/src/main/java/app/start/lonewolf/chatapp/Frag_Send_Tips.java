package app.start.lonewolf.chatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Send_Tips extends Fragment {
    private DatabaseReference databaseReference, refId;
    private Button submit, close;
    private EditText tip;

    public Frag_Send_Tips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_frag__send__tips, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tips");
        submit = (Button)view.findViewById(R.id.btnTipSubmit);
        close = (Button)view.findViewById(R.id.btnTipCancel);
        tip = (EditText)view.findViewById(R.id.edtTip);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTip();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        return view;


    }

    private void setTip(){
        String newTip = tip.getText().toString();
        databaseReference.setValue(newTip);

        Toast.makeText(getActivity(), "Tip Uploaded", Toast.LENGTH_LONG).show();
        tip.setText("");

    }
}
