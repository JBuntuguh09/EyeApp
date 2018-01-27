package app.start.lonewolf.chatapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag_Ques_Admin extends Fragment {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private SavedPreferences savedPreferences;
    public Frag_Ques_Admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view= inflater.inflate(R.layout.fragment_frag__ques__admin, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("questions");
        databaseReference.keepSynced(true);
        recyclerView=(RecyclerView)view.findViewById(R.id.questionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog = new ProgressDialog(getActivity());
        savedPreferences = new SavedPreferences(getActivity());


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        progressDialog.show();
        progressDialog.setMessage("Please wait whilst we retrieve your Data");
        FirebaseRecyclerAdapter<QuestionsModel,QuesViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<QuestionsModel, QuesViewHolder>(

                QuestionsModel.class,
                R.layout.layout_admin_auestions,
                QuesViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(QuesViewHolder viewHolder, QuestionsModel model, int position) {
                viewHolder.setUseName(model.getUserName());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDetails(model.getDetails());
                viewHolder.setEmail(model.getEmail());


                final String userId = getRef(position).getKey();
                QuesViewHolder.qView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedPreferences.setUserId(userId);
                        Intent intent = new Intent(getActivity(), AnsQuestion.class);
                        startActivity(intent);
                    }
                });



            }
        };
        recyclerView.setAdapter(recyclerAdapter);

        progressDialog.dismiss();
    }


    public static class QuesViewHolder extends RecyclerView.ViewHolder{

        static View qView;

        public QuesViewHolder(View itemView) {
            super(itemView);

            qView=itemView;
        }
        private void setEmail(String email){
            TextView mail = (TextView)qView.findViewById(R.id.qEmail);
            mail.setText(email);

        }

        private void setTitle(String title){
            TextView Title = (TextView)qView.findViewById(R.id.qTitle);
            Title.setText(title);

        }

        private void setDetails(String details){
            TextView Details = (TextView)qView.findViewById(R.id.qQuestion);
            Details.setText(details);

        }



        private void setUseName(String userName){
            TextView name = (TextView)qView.findViewById(R.id.qName);
            name.setText(userName);

        }
    }
}
