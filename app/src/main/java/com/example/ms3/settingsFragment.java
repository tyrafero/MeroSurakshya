package com.example.ms3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class settingsFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    TextView pName,pEmail,pPhone;
    String mName,mEmail,mPhone;

    public settingsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_settings,container,true);
        pName= view.findViewById(R.id.profileName);
        pEmail =view.findViewById(R.id.profileEmail);
        pPhone =view.findViewById(R.id.profilePhone);
        mAuth = FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();

        DocumentReference docRef = mStore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mName = documentSnapshot.getString("name");
                    mEmail=documentSnapshot.getString("email");
                    mPhone= mAuth.getCurrentUser().getPhoneNumber();

                    pName.setText(mName);
                    pEmail.setText(mEmail);
                    pPhone.setText(mPhone);
                }
            }
        });

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}