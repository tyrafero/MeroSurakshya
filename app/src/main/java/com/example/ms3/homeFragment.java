package com.example.ms3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class homeFragment extends Fragment {
    private Button b1, button1, b6, b4;
    private EditText e6;
    String number, userid,em,en,eLocation;
    private LatLng userLocation;
    Location lastLocation;
    LocationRequest locationRequest;
    private GoogleApiClient mLocationClient;
    Location currentLocation;


    FirebaseDatabase rootNode;
    DatabaseReference reference;


    private FusedLocationProviderClient mFusedLocationClient;


    Location mLastLocation;


    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton bmain = view.findViewById(R.id.button);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        b4 = view.findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WomenLaws.class);
                startActivity(intent);
            }
        });

        button1 = view.findViewById(R.id.button5);
        Button tipsBut= view.findViewById(R.id.button2);
        Button threat=view.findViewById(R.id.button3);
        threat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), threat.class);
                startActivity(intent);

            }
        });
        tipsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), women_safetytips.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Button1.class);
                startActivity(intent);
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        bmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getCurrentLocation();

                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                }
                else {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.SEND_SMS}, 0);
                }

            }
        });

        e6 = view.findViewById(R.id.edittext);
        b6 = view.findViewById(R.id.b6);


        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = e6.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                reference.child(userid).child("E_Number").setValue(number);

            }
        });

        DatabaseReference refNum = FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("E_Number");
        refNum.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    en = snapshot.getValue().toString();
                    e6.setText(en);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference refLoc = FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("C_Location");
        refLoc.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    eLocation = snapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address>addresses= geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );

                        String rLocation= addresses.get(0).getAddressLine(0);

                        reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.child(userid).child("C_Location").setValue(rLocation);

                        Toast.makeText(getContext(),"Emergency message sending...",Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }

    }

    private void MyMessage() {

        em = "I am in danger. Please help me. Location : " + eLocation ;

        String e_Num = en;
        String e_Msg = em;

        if (!e_Num.equals("") || !e_Msg.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(e_Num, null, e_Msg, null, null);

            Toast.makeText(getContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(), "Message Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 0 :

                if (grantResults.length>=0  && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

                    MyMessage();
                }
                else {
                    Toast.makeText(getContext(), "Permission Required", Toast.LENGTH_LONG).show();
                }

                break;

        }

    }
}