package com.ginsmile.androidtagview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private  String[] tags = new String[]{
            "good","apple","yeah","iphone","calculate",
            "Hello","jobs","good","iphone","I love you",
            "arigato","obrigato","whats up","sky","tokyo tower"
    };
    private HotTagView mHotTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHotTagView = (HotTagView)findViewById(R.id.htv);
        mHotTagView.setTags(tags);
    }


}
