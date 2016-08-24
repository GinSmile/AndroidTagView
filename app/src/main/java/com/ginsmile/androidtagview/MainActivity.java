package com.ginsmile.androidtagview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private HotTagView mHotTagView;
    private HotTagView mHotTagView2;
    private HotTagView mHotTagView3;
    private HotTagView mHotTagView4;

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

        mHotTagView4 = (HotTagView)findViewById(R.id.htv4);
        mHotTagView4.setTags(getData());



        //让不同的TagView有不同颜色
        int[] myColors = {Color.rgb(0x49, 0xC1, 0x20), Color.rgb(0xF0, 0x80, 0x80),
                Color.rgb(0xCD, 0xAA, 0x7D)};
        Random rand = new Random();
        ArrayList<HotTagView.TagView> tagViews = (ArrayList<HotTagView.TagView>)mHotTagView4.getTagViews();
        for(HotTagView.TagView tagView: tagViews){
            int curColor = myColors[rand.nextInt(myColors.length)];
            tagView.setTextColor(curColor);
            tagView.setmLinePaintColor(curColor);
        }


    }

    public ArrayList<String> getData(){
        String[] labels = new String[]{
                "good","apple","yeah","iphone","ill",
                "sky","tokyo","sky tower"
        };
        ArrayList<String> tags = new ArrayList<String>();
        for(int i = 0; i < labels.length; i++){
            tags.add(labels[i]);
        }

        return tags;
    }


}
