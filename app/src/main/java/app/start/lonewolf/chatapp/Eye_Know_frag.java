package app.start.lonewolf.chatapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Eye_Know_frag extends Fragment {

    private Button but2;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private int x=2;
    private SavedPreferences savedPreferences;

    private Button btn1;
    public Eye_Know_frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_eye__know_frag, container, false);
        //linearLayout = (LinearLayout)view.findViewById(R.id.linearKnow);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("knowledge");
        databaseReference.keepSynced(true);
        recyclerView = (RecyclerView)view.findViewById(R.id.knowRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedPreferences = new SavedPreferences(getActivity());


        //but2  = (Button)view.findViewById(R.id.button2);
        /*but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=4;
            }
        });*/
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<knoowledgeModel, usersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<knoowledgeModel, usersViewHolder>(

                knoowledgeModel.class,
                R.layout.layout_knowledge,
                usersViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(usersViewHolder viewHolder, knoowledgeModel model, final int position) {
                viewHolder.setTopic(model.getTopic());
                viewHolder.setPic( getContext(),model.getCompPic());


                usersViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Article.class);
                        String artId = getRef(position).getKey();
                        savedPreferences.setArticleId(artId);
                        startActivity(intent);
                    }
                });
            }
        };
     recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private static class usersViewHolder extends RecyclerView.ViewHolder{
        static View view;
        public usersViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        private  void setTopic(String topic){
            TextView topics = (TextView)view.findViewById(R.id.txtKnowTopic);
            topics.setText(topic);
        }

        private void setPic(final Context context, String compImage){
            ImageView newImage = (ImageView)view.findViewById(R.id.getKnowImage);
            Picasso.with(context).load(compImage).into(newImage);
        }
    }
}
