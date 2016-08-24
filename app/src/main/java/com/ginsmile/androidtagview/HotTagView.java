package com.ginsmile.androidtagview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujin on 16/08/24.
 */
public class HotTagView extends ViewGroup {
    private Paint mTextPaint;//字
    private int mTextPaintColor = Color.BLACK;
    private Paint mBackgroudPaint;//背景
    private int mBackgroudPaintColor = Color.LTGRAY;
    private Paint mLinePaint;//外围线条
    private int mLinePaintColor = Color.BLUE;

    private int mTextViewPaddingLeft = 20;
    private int mTextViewPaddingRight = 20;
    private int mTextViewPaddingTop = 10;
    private int mTextViewPaddingBottom = 10;

    private int mTextViewMarginLeft = 10;
    private int mTextViewMarginRight = 10;
    private int mTextViewMarginTop = 10;
    private int mTextViewMarginBottom = 10;

    private List<String> tags = new ArrayList<String>();

    public HotTagView(Context context) {
        super(context,null);
    }

    public HotTagView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public HotTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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



    private void init(){
//        mTextViewPaddingLeft = 10;
//        mTextViewPaddingRight = 10;
//        mTextViewPaddingTop = 10;
//        mTextViewPaddingBottom = 10;
//
//        mTextViewMarginLeft = 10;
//        mTextViewMarginRight = 10;
//        mTextViewMarginTop = 10;
//        mTextViewMarginBottom = 10;
//
//        Log.v("TAG:","DONE....");
    }

    public void setTags(String[] tags){
        for(String tag : tags){
            TagView tv = new TagView(getContext(),tag);
            MarginLayoutParams lp = new MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT
            );
            lp.setMargins(mTextViewMarginLeft, mTextViewMarginRight, mTextViewMarginTop, mTextViewMarginBottom);
            this.addView(tv,lp);

            final CharSequence cs = tag;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), cs, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }






    //小TagView
    private float rx,ry;

    class TagView extends TextView{

        float top;
        float left;
        float right;
        float bottom;

        public TagView(Context context) {
            super(context);
        }

        public TagView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TagView(Context context, CharSequence tag) {
            super(context);
            setPadding(mTextViewPaddingLeft,mTextViewPaddingTop,  mTextViewPaddingRight, mTextViewPaddingBottom);
            setGravity(TEXT_ALIGNMENT_CENTER);
            setText(tag);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            top = 0;
            left = 0;
            right = getWidth();
            bottom = getHeight();
            rx = 30;
            ry = 30;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            mBackgroudPaint  = new Paint();
            mBackgroudPaint.setColor(mBackgroudPaintColor);
            canvas.drawRoundRect(new RectF(left,top,right,bottom),rx,ry,mBackgroudPaint);

            mLinePaint = new Paint();
            mLinePaint.setColor(mLinePaintColor);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(5);
            mLinePaint.setAntiAlias(true);
            //canvas.drawRoundRect(new RectF(left,top,right,bottom),rx,ry,mLinePaint);
            super.onDraw(canvas);
        }



    }
}
