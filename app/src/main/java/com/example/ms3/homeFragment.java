package com.example.ms3;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class homeFragment extends Fragment {
    private Button b1;
    private LatLng userLocation;

    Location mLastLocation;


    public homeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);
        b1= view.findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
//                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Location");
//                GeoFire geofire =new GeoFire(ref);
//                geofire.setLocation(userId,new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
//
//                userLocation=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    public void sendLocationSMS(String phoneNumber, Location currentLocation) {
        SmsManager smsManager = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append("http://maps.google.com?q=");
        smsBody.append(currentLocation.getLatitude());
        smsBody.append(",");
        smsBody.append(currentLocation.getLongitude());
        smsManager.sendTextMessage("9861286961", null, smsBody.toString(), null, null);
    }
}