package com.ginsmile.androidtagview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujin on 16/08/24.
 */
public class HotTagView extends ViewGroup {
    public HotTagView(Context context) {
        super(context,null);
    }

    public HotTagView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public HotTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content
        int width = 0;//整个大view的宽度，就是所有lineWidth的最大值
        int height = 0;

        //每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //内部元素个数
        int cCount = getChildCount();
        for(int i = 0 ; i < cCount; i++){
            View child = getChildAt(i);
            //测量子view的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();//这个是Linearlayout的

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //如果这一行不够了，换行
            if(lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()){
                //得到最大宽度
                width = Math.max(width, lineWidth);
                //lineWidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            }else{//未换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }


            //到达最后一个控件
            if(i == cCount - 1){
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth:width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight:height + getPaddingTop() + getPaddingBottom()
        );
    }

    /**
     * 存储所有的View,一行一行的存
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 存储每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int cCount = getChildCount();
        Log.v("MYTAG:",cCount+"");
        for(int i = 0; i < cCount; i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //重新开了一行
            if(childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()){
                //记录lineheight
                mLineHeight.add(lineHeight);

                //当前行的Views
                mAllViews.add(lineViews);

                //重置行宽，高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                lineViews = new ArrayList<View>();
            }


            lineWidth += childWidth + lp.rightMargin + lp.leftMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);

        }


        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);


        //设置子View的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();

        for(int i = 0; i < lineNum; i++){
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //遍历行
            for(int j = 0 ; j < lineViews.size(); j++){
                View child = lineViews.get(j);
                if(child.getVisibility() == View.GONE){
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            //重置left
            left = getPaddingLeft();
            top += lineHeight;
        }



    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public void setTags(String[] tags){
        for(String tag : tags){
            TextView tv = new TextView(getContext());
            MarginLayoutParams lp = new MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT
            );
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            lp.bottomMargin = 10;
            tv.setText(tag);
            tv.setPadding(10,10,10,10);
            tv.setBackgroundColor(Color.LTGRAY);
            this.addView(tv,lp);
        }
    }
}
