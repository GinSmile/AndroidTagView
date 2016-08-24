package com.ginsmile.androidtagview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private HotTagView mHotTagView;
    private HotTagView mHotTagView2;
    private HotTagView mHotTagView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHotTagView = (HotTagView)findViewById(R.id.htv);
        mHotTagView.setTags(getData());

        mHotTagView2 = (HotTagView)findViewById(R.id.htv2);
        mHotTagView2.setTags(getData());

        mHotTagView3 = (HotTagView)findViewById(R.id.htv3);
        mHotTagView3.setTags(getData());
    }

    public ArrayList<String> getData(){
        String[] labels = new String[]{
                "good","apple","yeah","iphone","ill",
                "arigato","obrigato","whats up","sky","tokyo","sky tower",
        };
        ArrayList<String> tags = new ArrayList<String>();
        for(int i = 0; i < labels.length; i++){
            tags.add(labels[i]);
        }

        return tags;
    }


}
