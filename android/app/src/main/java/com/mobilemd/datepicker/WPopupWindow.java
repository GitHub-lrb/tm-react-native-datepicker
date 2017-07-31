package com.mobilemd.datepicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class WPopupWindow extends PopupWindow {

    private Context context;
    private boolean isBgAlpha=true;
    private float alpha=0.5f;

    public WPopupWindow(View contentView) {
        this(contentView, ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public WPopupWindow(Context context) {
        this(context,null);
    }

    public WPopupWindow(int width, int height) {
        this(null,width, height);
    }

    public WPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public WPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }
    public WPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
        init();
    }

    public WPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        this.context=contentView.getContext();
        init();
    }

    public void setBgAlpha(boolean isAlpha,float alpha){
        this.isBgAlpha=isAlpha;
        this.alpha=alpha;
    }

    @Override
    public void showAsDropDown(View anchor) {
        this.showAsDropDown(anchor,0,0);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        this.showAsDropDown(anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        setWindowFilter(isBgAlpha, alpha);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setWindowFilter(isBgAlpha, alpha);
        super.showAtLocation(parent, gravity, x, y);
    }

    public void init(){
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowFilter(isBgAlpha, 1f);
            }
        });
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setOutTouchCancel(true);
    }

    /**
     * @param isCancel  点击对话框外时，是否取消对话框
     */
    public void setOutTouchCancel(boolean isCancel){
        if(isCancel){
            setBackgroundDrawable(new BitmapDrawable());
            setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }else{
            setBackgroundDrawable(null);
            setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    public void setWindowFilter(boolean isBgAlpha,float alpha) {
        if (isBgAlpha) {
            WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
            lp.alpha = alpha;
            //保证华为honor颜色变暗
            lp.dimAmount=alpha;
            ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            ////////////////////////////////////////////////////
            ((Activity) context).getWindow().setAttributes(lp);
        }
    }


}