package com.cookandroid.weatherdressing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class longP extends AppCompatActivity {
    int[] imageid = {R.drawable.lp1, R.drawable.lp2, R.drawable.lp3, R.drawable.lp4, R.drawable.lp5, R.drawable.lp6, R.drawable.lp7};
    ImageView img;
    Button bPicL, bPicR;
    int i=0, length = imageid.length-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothes);

        img = (ImageView)findViewById(R.id.imageView);
        bPicL = (Button)findViewById(R.id.btnPicleft);
        bPicR = (Button)findViewById(R.id.btnPicrignt);

        img.setImageResource(R.drawable.lp1);

        bPicL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==0) i=length;
                else i-=1;
                img.setImageResource(imageid[i]);
            }
        });
        bPicR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i==length) i=0;
                else i+=1;
                img.setImageResource(imageid[i]);
            }
        });

    }
}