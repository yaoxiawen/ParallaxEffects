package com.yxw.parallaxeffects;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        final MyListView mListView = findViewById(R.id.lv);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // 加Header
        final View mHeaderView = View.inflate(MainActivity.this, R.layout.view_header, null);
        final ImageView mImage = mHeaderView.findViewById(R.id.iv);
        mListView.addHeaderView(mHeaderView);
        // 填充数据
        mListView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
        //设置监听，获取视图树，onLayout执行完之后会调用此方法，
        // 这个时候getHeight()就能得到图片显示的高度了
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mListView.setParallaxImage(mImage);
                //监听一次就好
                mHeaderView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }
}
