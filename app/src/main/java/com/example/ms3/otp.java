package com.example.ms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    EditText mPhone,mCode;
    Button mBtn;
    ProgressBar mProgressBar;
    TextView mState;
    CountryCodePicker codePicker;
    String mVerification_id;
    PhoneAuthProvider.ForceResendingToken mToken;
    Boolean verificationProgress= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth= FirebaseAuth.getInstance();

        fStore=FirebaseFirestore.getInstance();
        mPhone= findViewById(R.id.phone);
        mCode= findViewById(R.id.codeEnter);
        mBtn= findViewById(R.id.nextBtn);
        mState= findViewById(R.id.state);
        codePicker= findViewById(R.id.ccp);
        mProgressBar=findViewById(R.id.progressBar);

//When "Next" button is clicked this code will run

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!verificationProgress) {
                    if(!mPhone.getText().toString().isEmpty()&& mPhone.getText().toString().length()==10){

                        String phoneNum="+" + codePicker.getSelectedCountryCode()  +  mPhone.getText().toString();
                        Log.d("mPhone", "Phone No.: " + phoneNum);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mState.setText("Sending OTP...");
                        mState.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);

                    }else{
                        mPhone.setError("Phone number is not valid");
                    }
                }
                else{
                    String userOTP= mCode.getText().toString();
                    if (!userOTP.isEmpty() && userOTP.length()==6){
                        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerification_id,userOTP);
                        verifyAuth(credential);

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null){
            mProgressBar.setVisibility(View.VISIBLE);
            mState.setText("Checking");
            checkUserProfile();
        }
    }



    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                checkUserProfile();

            }else {
                Toast.makeText(otp.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void checkUserProfile() {
        Toast.makeText(otp.this,"Authentication Success",Toast.LENGTH_SHORT).show();
        DocumentReference docRef = fStore.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    finish();

                }else{
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    finish();
                }
            }
        });


    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(String.valueOf(phoneNum), 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//When User doesnt get OTP and we force server to send the OTP code,Force Resending token is used
            //"s" contains the verification code id
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mProgressBar.setVisibility(View.GONE);
                mState.setVisibility(View.GONE);
                mCode.setVisibility(View.VISIBLE);
                mVerification_id=s;
                mToken=forceResendingToken;
                mBtn.setText("Verify");
                verificationProgress=true;


            }
//When OTP is not entered in the given time frame

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(otp.this,"Cannot Create Account"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}