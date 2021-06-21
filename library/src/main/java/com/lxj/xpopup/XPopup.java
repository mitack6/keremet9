package com.lxj.xpopup;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lxj.xpopup.enums.PopupStatus;
import com.lxj.xpopup.enums.PopupType;
import com.lxj.xpopup.impl.BasePopupView;
import com.lxj.xpopup.impl.CenterPopupView;

/**
 * PopupView的控制类，控制生命周期：显示，隐藏，添加，删除。
 */
public class XPopup implements LifecycleObserver {
    private static XPopup instance = null;
    private Context context;
    private PopupInfo popupInfo = null;
    private PopupInterface popupInterface;
    private Handler handler = new Handler();
    private ViewGroup activityView = null;
    private PopupStatus popupStatus = PopupStatus.Dismiss;
    private XPopup(Context context) {
        this.context = context;
    }

    public static XPopup get(Context context) {
        if (instance == null) {
            instance = new XPopup(context);
        }
        return instance;
    }

    /**
     * 显示，本质是就将View添加到Window上，并执行动画
     */
    public void show() {
        if(popupStatus!=PopupStatus.Dismiss)return;
        if (context == null) {
            throw new IllegalArgumentException("context can not be null!");
        }
        if (!(context instanceof FragmentActivity)) {
            throw new IllegalArgumentException("context must be an instance of FragmentActivity");
        }
        FragmentActivity activity = (FragmentActivity) context;
        activityView = (ViewGroup) activity.getWindow().getDecorView();
        Log.e("tag", activityView.getClass().getSimpleName());

        //1. 根据PopupInfo生成PopupView
        popupInterface = genPopupImpl();
        if (popupInterface.getView() == null) {
            throw new RuntimeException("PopupInterface getView() method can not return null!");
        }
        activityView.addView(popupInterface.getView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        activityView.bringChildToFront(popupInterface.getView());

        // 监听KeyEvent
        popupInterface.getView().setFocusableInTouchMode(true);
        popupInterface.getView().requestFocus();
        popupInterface.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });

        // 监听点击
        popupInterface.getBackgroundView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //2. 执行开始动画
        popupInterface.getView().post(new Runnable() {
            @Override
            public void run() {
                popupStatus = PopupStatus.Showing;
                popupInterface.startAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupStatus = PopupStatus.Show;
                    }
                }, popupInterface.getAnimationDuration()+10);
            }
        });

    }

    /**
     * 根据PopupInfo生成对应
     *
     * @return
     */
    private PopupInterface genPopupImpl() {
        checkPopupInfo();
        Log.e("XPopup", popupInfo.toString());
        BasePopupView popupView = null;
        switch (popupInfo.popupType) {
            case Center:
                popupView = new CenterPopupView(context);
                break;
            case Bottom:

                break;
            case Custom:

                break;
        }
        popupView.setPopupInfo(popupInfo);
        return popupView;
    }

    /**
     * 消失
     */
    public void dismiss() {
        if(popupStatus!=PopupStatus.Show)return;
        //1. 执行结束动画
        popupStatus = PopupStatus.Dismissing;
        popupInterface.endAnimation();

        //2. 将PopupView从window中移除
        handler.removeCallbacks(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(popupInterface.getView().isAttachedToWindow() && activityView!=null){
                    activityView.removeView(popupInterface.getView());
                    activityView = null;
                    popupStatus = PopupStatus.Dismiss;
                }
            }
        }, popupInterface.getAnimationDuration() + 10);
    }

    public XPopup position(PopupType popupType) {
        checkPopupInfo();
        popupInfo.popupType = popupType;
        return this;
    }

    public XPopup dismissOnBackPressed(boolean isDismissOnBackPressed) {
        checkPopupInfo();
        popupInfo.isDismissOnBackPressed = isDismissOnBackPressed;
        return this;
    }

    public XPopup dismissOnTouchOutside(boolean isDismissOnTouchOutside) {
        checkPopupInfo();
        popupInfo.isDismissOnTouchOutside = isDismissOnTouchOutside;
        return this;
    }

    public XPopup atView(View view) {
        checkPopupInfo();
        popupInfo.setAtView(view);
        return this;
    }

    public XPopup hasShadowBg(boolean hasShadowBg) {
        checkPopupInfo();
        popupInfo.hasShadowBg = hasShadowBg;
        return this;
    }

    private void checkPopupInfo() {
        if (popupInfo == null) {
            popupInfo = new PopupInfo();
        }
    }
}
