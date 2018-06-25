package com.gmail.akshitgupta1695.textextractor.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gmail.akshitgupta1695.textextractor.R;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openRelevantActivity(View view) {
        int id=view.getId();
        switch (id){
            case R.id.AadharCardView:
                Intent aadhaarCardIntent=new Intent(this,AadhaarActivity.class);
                startActivity(aadhaarCardIntent);
                break;
            case R.id.GenericIdCardView:
                Intent genericIdIntent=new Intent(this,GenericIdActivity.class);
                startActivity(genericIdIntent);
                break;
            case R.id.PanCardView:
                Intent panCardIntent=new Intent(this,PanCardActivity.class);
                startActivity(panCardIntent);
                break;
            default:break;
        }
    }
}
