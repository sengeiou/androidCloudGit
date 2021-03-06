package com.cloudtenant.yunmenkeji.cloudtenant.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.cloudtenant.yunmenkeji.cloudtenant.R;


/**
 * All rights Reserved, Designed By GeofferySun 
 * @Title: 	SelectPicPopupWindow.java
 * @Package sun.geoffery.uploadpic
 * @Description:从底部弹出或滑出选择菜单或窗口
 * @author:	GeofferySun
 * @date:	2015年1月15日 上午1:21:01
 * @version	V1.0
 */
public class CharPicPopupWindow extends PopupWindow {

	private Button takePhotoBtn, pickPhotoBtn, cancelBtn,btnpickP;
	private ImageView imageView;
	private View mMenuView;

	public interface OnClickListen{
	void	onClick(View view ,int pos);
	}
	@SuppressLint("InflateParams")
	public CharPicPopupWindow(Context context,  final OnClickListen onClickListen, int type) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.layout_dialog_chat, null);
		takePhotoBtn = (Button) mMenuView.findViewById(R.id.takePhotoBtn);
		pickPhotoBtn = (Button) mMenuView.findViewById(R.id.pickPhotoBtn);
		cancelBtn = (Button) mMenuView.findViewById(R.id.cancelBtn);
		btnpickP=(Button)mMenuView.findViewById(R.id.pickP);
		imageView= (ImageView) mMenuView.findViewById(R.id.image);
		// 设置按钮监听
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListen.onClick(view,3);
			}
		});
		pickPhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListen.onClick(view,1);
			}
		});
		takePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListen.onClick(view,0);
			}
		});
		btnpickP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickListen.onClick(view,2);
			}
		});


		if (type==1) {
			pickPhotoBtn.setText("撤销退房申请");
		}else  if(type==2){
			pickPhotoBtn.setText("半年报表");
		}

		else {
			pickPhotoBtn.setText("申请退房");
		}
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		//this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x20000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
