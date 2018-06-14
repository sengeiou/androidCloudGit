package com.cloudtenant.yunmenkeji.cloudtenant.base;

/**
 * Created by feng on 2017/12/13.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.cloudtenant.yunmenkeji.cloudtenant.R;
import com.cloudtenant.yunmenkeji.cloudtenant.util.RxBarTool;
import com.cloudtenant.yunmenkeji.cloudtenant.util.WaitPorgressDialog;
import com.cloudtenant.yunmenkeji.cloudtenant.view.MLoadingDialog;


import com.gyf.barlibrary.ImmersionBar;
import com.yzs.yzsbaseactivitylib.entity.EventCenter;
import com.yzs.yzsbaseactivitylib.loading.LoadingDialog;
import com.yzs.yzsbaseactivitylib.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;



public abstract class YzsBaseActivity extends me.yokeyword.fragmentation.SupportActivity {
    private static final String TAG = "YzsBaseActivity";
    protected View emptyView;
    public boolean useTitle = true;
    public Toolbar mToolbar;
    public TextView title;
    public ImageView back;
    public TextView tv_menu;
    public MLoadingDialog m_cProgressDialog;
    public ImageView iv_menu;
    protected WaitPorgressDialog mWaitPorgressDialog;
    public YzsBaseActivity() {

    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public void setmToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }



    public ImageView getBack() {
        return back;
    }

    public void setBack(ImageView back) {
        this.back = back;
    }

    public TextView getTv_menu() {
        return tv_menu;
    }

    public void setTv_menu(TextView tv_menu) {
        this.tv_menu = tv_menu;
    }

    public ImageView getIv_menu() {
        return iv_menu;
    }

    public void setIv_menu(ImageView iv_menu) {
        this.iv_menu = iv_menu;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


     ImmersionBar.with(this)

                .statusBarColor(R.color.transparent)
                .init();
//        RxBarTool.setNoTitle(this);
//        RxBarTool.setTransparentStatusBar(this);//状态栏透明化
//        RxBarTool.StatusBarLightMode(this);
        Bundle extras = this.getIntent().getExtras();

        if(null != extras) {
            ;
        }

        EventBus.getDefault().register(this);
        this.initContentView(savedInstanceState);
        this.mToolbar = (Toolbar)this.findViewById(R.id.toolbar);
        if(null != this.mToolbar) {
            this.setSupportActionBar(this.mToolbar);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
            this.initTitle();

        }

        this.initView();
        this.initLogic();
/*        AndroidBug5497Workaround.assistActivity(this);*/

    }

    protected abstract void initContentView(Bundle var1);

    protected abstract void initView();

    protected abstract void initLogic();

    protected abstract void getBundleExtras(Bundle var1);

    protected void initTitle() {
        this.title = (TextView)this.findViewById(com.yzs.yzsbaseactivitylib.R.id.toolbar_title);
        this.back = (ImageView)this.findViewById(com.yzs.yzsbaseactivitylib.R.id.toolbar_back);
        this.iv_menu = (ImageView)this.findViewById(com.yzs.yzsbaseactivitylib.R.id.toolbar_iv_menu);
        this.tv_menu = (TextView)this.findViewById(com.yzs.yzsbaseactivitylib.R.id.toolbar_tv_menu);
        if(null != this.back) {
            this.back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   YzsBaseActivity.this.finish();
                }
            });
        }

    }

    public void setTitle(String string) {
        if(null != this.title) {
            this.title.setText(string);
        }

    }
    public void setRightText(String string) {
        if(null != this.title) {
            tv_menu.setText(string);
        }

    }
    public void setTitle(int id) {
        if(null != this.title) {
            this.title.setText(id);
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = 67108864;
        if(on) {
            winParams.flags |= 67108864;
        } else {
            winParams.flags &= -67108865;
        }

        win.setAttributes(winParams);
    }

    public static void setTransparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //透明导航栏
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter center) {
        if(null != center) {
            this.onEventComing(center);
        }

    }

    protected abstract void onEventComing(EventCenter var1);

    protected void showLoadingDialog() {
        if (isFinishing()) {
            return;
        }
        if (m_cProgressDialog == null) {
            m_cProgressDialog = new MLoadingDialog(this);
        }

    }

    protected void showLoadingDialog(int type) {

        LoadingDialog.showLoadingDialog(this, type);
    }

    protected void showLoadingDialog(int type, int drawableId) {
        LoadingDialog.showLoadingDialog(this, type, drawableId);
    }

    protected void showLoadingDialog(String str) {
        LoadingDialog.showLoadingDialog(this, str);
    }

    protected void showLoadingDialog(int type, String str) {
        LoadingDialog.showLoadingDialog(this, type, str);
    }

    protected void showLoadingDialog(int type, String str, int drawable) {
        LoadingDialog.showLoadingDialog(this, type, str, drawable);
    }

    protected void cancelLoadingDialog() {
        if (m_cProgressDialog != null) {
            m_cProgressDialog.dismiss();
        }
    }

    protected void showShortToast(String string) {
        ToastUtils.showShortToast(this, string);
    }

    protected void showShortToast(int stringId) {
        ToastUtils.showShortToast(this, stringId);
    }

    protected void showLongToast(String string) {
        ToastUtils.showShortToast(this, string);
    }

    protected void showLongToast(int stringId) {
        ToastUtils.showShortToast(this, stringId);
    }

    protected void onDestroy() {
        this.emptyView = null;
        EventBus.getDefault().unregister(this);
        ImmersionBar.with(this).destroy();
        super.onDestroy();

    }

    protected void readyGo(Class<?> clazz) {
        this.readyGo(clazz, (Bundle)null);
    }

    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivity(intent);
    }

    protected void readyGoThenKill(Class<?> clazz) {
        this.readyGoThenKill(clazz, (Bundle)null);
    }

    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        this.readyGo(clazz, bundle);
        this.finish();
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        this.startActivityForResult(intent, requestCode);
    }

    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if(null != bundle) {
            intent.putExtras(bundle);
        }

        this.startActivityForResult(intent, requestCode);
    }


}
