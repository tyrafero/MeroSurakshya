package com.example.ms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore mfs;
    EditText name,email,contact;
    Button save_btn;
    String user_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mfs=FirebaseFirestore.getInstance();
        user_Id= mFirebaseAuth.getCurrentUser().getUid();

        final DocumentReference docRef = mfs.collection("users").document(user_Id);




        name= findViewById(R.id.User_Name);
        email= findViewById(R.id.User_Email);
        contact=findViewById(R.id.User_Contact);
        save_btn=findViewById(R.id.save_button);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty()&& !email.getText().toString().isEmpty()&& !contact.getText().toString().isEmpty() ){
                    String  Name= name.getText().toString();
                    String  Email= email.getText().toString();
                    String  Contact= contact.getText().toString();

                    Map<String,Object> user=new HashMap<>();
                    user.put("name",Name);
                    user.put("email",Email);
                    user.put("contact",Contact);

                    docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), Settings.class));
                                finish();
                            }else{
                                Toast.makeText(Register.this,"Data is not inserted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this,"All Fields are Required.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}