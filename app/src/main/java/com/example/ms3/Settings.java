package com.example.ms3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Settings extends AppCompatActivity {
    private ChipNavigationBar ChipBar;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ChipBar=findViewById(R.id.chipBar);
        ChipBar.setItemSelected(R.id.home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new homeFragment()).commit();

        ChipBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.home:
                        fragment=new homeFragment();
                        break;
                    case R.id.police:
                        fragment=new locationFragment();
                        break;
                    case R.id.period:
                        fragment=new periodFragment();
                        break;
                    case R.id.settings:
                        fragment=new settingsFragment();
                        break;

                }
                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                }
            }
        });
    }
}