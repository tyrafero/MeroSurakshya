package com.example.ms3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth mAuth;
    EditText mPhone,mCode;
    Button mBtn;
    ProgressBar mProgressBar;
    TextView mState;
    CountryCodePicker codePicker;
    String mVerification_id;
    PhoneAuthProvider.ForceResendingToken mToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth= FirebaseAuth.getInstance();
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
                if(!mPhone.getText().toString().isEmpty()&& mPhone.getText().toString().length()==10){
                    String phoneNum= "+" + codePicker.getSelectedCountryCode()+mPhone.getText().toString();
                    Log.d("mPhone", "Phone No.: " + phoneNum);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState.setText("Sending OTP...");
                    mState.setVisibility(View.VISIBLE);
                    requestOTP(mPhone);
                }else{
                    mPhone.setError("Phone number is not valid");
                }
            }
        });
    }

    private void requestOTP(EditText mPhone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(String.valueOf(mPhone), 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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