package com.cloudtenant.yunmenkeji.cloudtenant.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cloudtenant.yunmenkeji.cloudtenant.R;
import com.cloudtenant.yunmenkeji.cloudtenant.activity.CityPickerActivity;
import com.cloudtenant.yunmenkeji.cloudtenant.activity.DisMapActivity;
import com.cloudtenant.yunmenkeji.cloudtenant.activity.HouseDetilActivity;
import com.cloudtenant.yunmenkeji.cloudtenant.activity.TnementAcitivity_;
import com.cloudtenant.yunmenkeji.cloudtenant.activity.WebVActivity;
import com.cloudtenant.yunmenkeji.cloudtenant.adapter.HouseAdapter;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.BrokenUp;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.Qr;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.RoomInfo;
import com.cloudtenant.yunmenkeji.cloudtenant.http.HttpMethods;
import com.cloudtenant.yunmenkeji.cloudtenant.model.BaseBean;
import com.cloudtenant.yunmenkeji.cloudtenant.model.HouseDetil;
import com.cloudtenant.yunmenkeji.cloudtenant.model.HouseDetil1;
import com.cloudtenant.yunmenkeji.cloudtenant.model.ListBean;
import com.cloudtenant.yunmenkeji.cloudtenant.model.LoginBean;
import com.cloudtenant.yunmenkeji.cloudtenant.util.AppUtils;
import com.cloudtenant.yunmenkeji.cloudtenant.util.BannerPicassoImageLoader;
import com.cloudtenant.yunmenkeji.cloudtenant.util.BaseObserver;
import com.cloudtenant.yunmenkeji.cloudtenant.util.JSONUtil;
import com.cloudtenant.yunmenkeji.cloudtenant.util.PreferencesUtils;
import com.cloudtenant.yunmenkeji.cloudtenant.util.SpacesItemDecoration;
import com.cloudtenant.yunmenkeji.cloudtenant.util.UserLocalData;
import com.daimajia.slider.library.SliderLayout;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.squareup.picasso.Picasso;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.Circle;
import com.tencent.mapsdk.raster.model.CircleOptions;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzs.yzsbaseactivitylib.entity.EventCenter;
import com.yzs.yzsbaseactivitylib.fragment.YzsBaseListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewHomeFragment extends BaseFragment implements TencentLocationListener, RecyclerArrayAdapter.OnMoreListener,SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    private static final int REQUEST_CODE_SCAN=77;
    private TextView tv_title,tv_location;
    public static final int GETCITY=9527;
    public Context mContext;
    private     List<String> images=new ArrayList<>();
    private     List<HouseDetil.BannerDataBean> bannerDataBeans=new ArrayList<>();
    private Banner banner;
    private HouseAdapter adapter;
    private List<HouseDetil.ViewDataBean> viewDataBean;
    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter.ItemView headerViewButten;
    private RecyclerArrayAdapter.ItemView headerViewBanner;
    private RecyclerArrayAdapter.ItemView headerViewMap;
    int page=1;
    int row=20;
    boolean isFirst=true;
    MapView mapview=null;
    TencentMap tencentMap;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private  List<Marker> markers =new ArrayList<>();
    public static boolean isMapMode=false;
    private LinearLayout ll_tor_bar;
    private LinearLayout.LayoutParams linearParams;
    private int scrolledDistance = 0;
    private Button tv_common, tv_map;
    private String phone;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_op1: {
                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE,Permission.READ_PHONE_STATE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(Object data) {
                                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                                /*ZxingConfig是配置类
                                 *可以设置是否显示底部布局，闪光灯，相册，
                                 * 是否播放提示音  震动
                                 * 设置扫描框颜色等
                                 * 也可以不传这个参数
                                 * */
                                ZxingConfig config = new ZxingConfig();
                                config.setPlayBeep(true);//是否播放扫描声音 默认为true
                                config.setShake(true);//是否震动  默认为true
                                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                                config.setReactColor(R.color.red1);//设置扫描框四个角的颜色 默认为淡蓝色
                                //config.setFrameLineColor(R.color.white);//设置扫描框边框颜色 默认无色
                                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                                config.setShowAlbum(true);
                                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                                startActivityForResult(intent, REQUEST_CODE_SCAN);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(Object data) {
                                Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(getContext(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();
                /*Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);*/
            }break;
            case R.id.btn_op2:{
                startActivityForResult(new Intent(getActivity(),CityPickerActivity.class),GETCITY);
            }
            break;

            case R.id.tv_map:{
                Log.d("onclick","点击了地图");
                if (!isMapMode) {
                    tv_map.setBackgroundResource(R.drawable.butten_background_green_solid_r);
                    tv_map.setTextColor(getResources().getColor(R.color.white));
                    tv_common.setBackgroundResource(R.drawable.butten_background_green_l);
                    tv_common.setTextColor(getResources().getColor(R.color.orange_cut_clorr));
                    initMyMap();
                    HideList();
                }
            }break;
            case R.id.tv_common:{
                Log.d("onclick","点击了点击了列表");
                if (isMapMode) {
                    tv_common.setBackgroundResource(R.drawable.butten_background_green_solid);
                    tv_common.setTextColor(getResources().getColor(R.color.white));
                    tv_map.setBackgroundResource(R.drawable.butten_background_green);
                    tv_map.setTextColor(getResources().getColor(R.color.orange_cut_clorr));
                    ShowList();
                }
            }break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","requestCode="+requestCode+">>>>resultCode="+resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GETCITY:
                    String city=data.getExtras().getString("city");
                    if(city!= null) {
                        System.out.println("ccccccctttttt" + city);
                        tv_location.setText(city);
                    }
                    break;
            }
        }
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                final String join="1";
                final String roominfo="2";
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                if (content.contains("qrType")) {
                    Qr qr= JSONUtil.fromJson(content,Qr.class);
                    Log.e("getData","解析成功qrtype="+qr.getQrType());
                    switch (qr.getQrType()) {
                        case join:{
                            Log.e("getData","解析成功qrtype=1>>>开始执行joinFamily方法");
                            //joinFamily(qr.getFamilyID());
                            normalDialog(qr.getFamilyID());
                        }break;
                        case roominfo:{
                            openActivity(qr.getCellRoomID());
                        }break;
                    }
                }else {
                    Toast.makeText(getContext(), "二维码解析失败！！！请重试" + content, Toast.LENGTH_LONG).show();
                }
                Log.e("getData","扫描结果为="+content);
            }
        }
    }
    String name,id;
    //一般的Dialog
    public void normalDialog(final String s){
        final View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.view_dialog,null);
        AlertDialog.Builder bulider =new AlertDialog.Builder(mContext);
        bulider.setTitle("请填写您的基础资料").setView(dialogView);


        bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                EditText et_name = dialogView.findViewById(R.id.edit_text_name);
                EditText et_id =  dialogView.findViewById(R.id.edit_text_id);
                name=et_name.getText().toString();
                id=et_id.getText().toString();
                joinFamily(s,id,name);
                dialog.dismiss();
            }
        });
        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        bulider.create().show();
    }
    private void openActivity(String roomid) {
        Bundle bundle =new Bundle();
        bundle.putBoolean("isMap",true);
        bundle.putString("roomId",roomid);
        readyGo(TnementAcitivity_.class,bundle);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return  null;
    }

    @Override
    protected View initContentView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        PreferencesUtils.putBoolean(getActivity(),"isShow",true);
        View view=inflater.inflate(R.layout.fragment_house,container,false);
        mContext=getActivity();
        if (!isFirst) {
            phone= Objects.requireNonNull(UserLocalData.getUser(mContext)).getUserPhone();
        }
        tv_location =view. findViewById(R.id.tv_location);
        ll_tor_bar =view. findViewById(R.id.ll_top_bar);
        linearParams= (LinearLayout.LayoutParams) ll_tor_bar.getLayoutParams();
        view.findViewById(R.id.btn_op1).setOnClickListener(this);
        tv_title=view.findViewById(R.id.title);
        tv_title .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        recyclerView=view.findViewById(R.id.recycler_view);

        recyclerView.setScrollBarStyle(RecyclerView.SCROLLBAR_POSITION_DEFAULT);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e("getData","onScrollStateChanged》》scrolledDistance="+scrolledDistance);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledDistance=scrolledDistance+dy;
                int search_layout_max_margin= ll_tor_bar.getMeasuredHeight();
                int newTopMargin = search_layout_max_margin - dy;
                // Log.e("getData","onScrolled》》dy="+dy+">>>search_layout_max_margin="+search_layout_max_margin);
                if (scrolledDistance<180) {
                    if (newTopMargin > 80 && newTopMargin < 180) {
                        linearParams.height = newTopMargin;
                        ll_tor_bar.setLayoutParams(linearParams);
                    }
                }
                //Log.e("getData","onScrolled》》dy="+dy+">>>newTopMargin="+newTopMargin);
            }
        });
        headerViewButten=new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view=inflater.inflate(R.layout.house_header,parent,false);
                tv_common=view.findViewById(R.id.tv_common);
                tv_map=view.findViewById(R.id.tv_map);
                tv_common.setOnClickListener(NewHomeFragment.this);
                tv_map.setOnClickListener(NewHomeFragment.this);

                return view;
            }
            @Override
            public void onBindView(View headerView) {
            }
        };
        headerViewMap=new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view=inflater.inflate(R.layout.house_header2,parent,false);
                mapview=view.findViewById(R.id.map);
                //获取TencentMap实例

                tencentMap = mapview.getMap();
                //设置实时路况开启
                tencentMap.setTrafficEnabled(true);
                tencentMap.setZoom(13);
                //移动地图
                return view;
            }
            @Override
            public void onBindView(View headerView) {
                //headerView.setVisibility(View.VISIBLE);
                locationManager = TencentLocationManager.getInstance(getActivity());
                locationRequest = TencentLocationRequest.create();
                locationRequest.setInterval(600000);//每分钟重新定位一次
                bindListener();
            }
        };
        headerViewBanner=new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view=inflater.inflate(R.layout.house_header1,parent,false);
                banner = view. findViewById(R.id.banner);
                return view;
            }
            @Override
            public void onBindView(View headerView) {
            }
        };
        //view.findViewById(R.id.btn_op2).setOnClickListener(this);
        adapter = new HouseAdapter(getActivity());
        adapter.addHeader(headerViewBanner);
        adapter.addHeader(headerViewButten);
        adapter.addHeader(headerViewMap);
        Log.e("getData","进入initContentView");
        return view;
    }

    private void ShowList() {
        PreferencesUtils.putBoolean(getActivity(),"isShow",true);
        isMapMode=false;
        mapview.setVisibility(View.GONE);
        banner.setVisibility(View.VISIBLE);

        adapter.clear();
        adapter.removeAll();
        adapter.addAll(viewDataBean);
        recyclerView.scrollToPosition(0);

        //adapter.setMore(R.layout.view_more, this);
    }

    private void HideList() {
        PreferencesUtils.putBoolean(getActivity(),"isShow",false);
        isMapMode=true;
        mapview.setVisibility(View.VISIBLE);
        banner.setVisibility(View.GONE);
        adapter.clear();
        adapter.removeAll();
        adapter.add(this);
        adapter.stopMore();
        //adapter.setMore(null, (RecyclerArrayAdapter.OnMoreListener) null);
        //recyclerView.scrollToPosition(3);
    }


    private void initMyMap() {
        Log.d("initMyMap","initMyMap！！");
        List<HouseDetil.ViewDataBean>  list= viewDataBean;

        tencentMap.setInfoWindowAdapter(new TencentMap.InfoWindowAdapter() {
            //infoWindow关闭后调用，用户回收View
            @Override
            public void onInfoWindowDettached(Marker arg0, View arg1) {
                // TODO Auto-generated method stub
                Log.d("WindowAdapter","onInfoWindowDettached！！");
                arg0.hideInfoWindow();
            }
            //infoWindow弹出前调用，返回的view将作为弹出的infoWindow
            @Override
            public View getInfoWindow(final Marker arg0) {
                Log.d("WindowAdapter","getInfoWindow！！");
                // TODO Auto-generated method stub
                View view=     LayoutInflater.from(mContext).inflate(R.layout.item_map_info,null);
                ImageView iv_pic= view.findViewById(R.id.iv_pic);
                TextView tv_shengxia= view.findViewById(R.id.tv_shengxia);

                TextView tv_name= view.findViewById(R.id.tv_name);
                TextView tv_pay= view.findViewById(R.id.tv_pay);
                TextView tv_desc= view.findViewById(R.id.tv_desc);
                HouseDetil.ViewDataBean bean = (HouseDetil.ViewDataBean) arg0.getTag();
                tv_name.setText(bean.getCellName());
                tv_pay.setText(bean.getCellCost());
                tv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("onclick","点击了地图上的弹框");
                        Bundle bundle =new Bundle();
                        bundle.putSerializable("bean",(HouseDetil.ViewDataBean)arg0.getTag());
                        readyGo(HouseDetilActivity.class,bundle);
                    }
                });
                tv_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("onclick","点击了地图上的弹框");
                        Bundle bundle =new Bundle();
                        bundle.putSerializable("bean",(HouseDetil.ViewDataBean)arg0.getTag());
                        readyGo(HouseDetilActivity.class,bundle);
                    }
                });
                Picasso.with(mContext).load(HttpMethods.BASE_URL+bean.getCellImage()).fit().into(iv_pic);
                tv_shengxia.setText("剩:"+bean.getCellRemain()+"间");

                return view;
            }
        });
        if (list!=null) {


            for (int i = 0; i < list.size(); i++) {

                HouseDetil.ViewDataBean bean=list.get(i);
                if (!bean.getCellLatitude().equals("0.000000")) {
                    String lat=bean.getCellLatitude();
                    String lon=bean.getCellLongitude();
                    LatLng latLng = new LatLng(Double.valueOf(bean.getCellLatitude()),Double.valueOf(bean.getCellLongitude()));
                    Log.d("initMyMap","lat="+Double.valueOf(bean.getCellLatitude())+">>>long="+Double.valueOf(bean.getCellLongitude()));
                    Log.d("initMyMap","lat="+bean.getCellLatitude()+">>>long="+bean.getCellLongitude());
                    //Log.d("initMyMap","getCellName="+bean.getCellName()+">>>getCellAddress="+bean.getCellAddress());
                    ImageView imageView=new ImageView(getActivity());
                    imageView.setImageResource(R.drawable.image_message_warning);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                                Log.d("initMyMap","lat=imageView");
                        }
                    });
                    final Marker marker = tencentMap.addMarker(new MarkerOptions().
                            position(latLng).
                            title(bean.getCellName()).anchor(1f, 1f)
                            .markerView(imageView)
                            .snippet(bean.getCellAddress()));

                    marker.setTag(bean);
                    markers.add(marker);
                }

            }
           /* for(HouseDetil.ViewDataBean bean :list){
                LatLng latLng = new LatLng(Double.valueOf(bean.getCellLatitude()),Double.valueOf(bean.getCellLongitude()));
                Log.d("initMyMap","lat="+bean.getCellLongitude()+">>>long="+bean.getCellLongitude());
                Log.d("initMyMap","getCellName="+bean.getCellName()+">>>getCellAddress="+bean.getCellAddress());
                final Marker marker = tencentMap.addMarker(new MarkerOptions().
                        position(latLng).
                        title(bean.getCellName()).
                        snippet(bean.getCellAddress()));

                marker.setTag(bean);
                markers.add(marker);
            }*/
        }else {
            Log.d("initMyMap","list为空！！");
        }

        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("onclick","点击了地图上的小标点setOnMarkerClickListener");
                arg0.hideInfoWindow();
                arg0.showInfoWindow();
                /*Bundle bundle =new Bundle();
                bundle.putSerializable("bean",(HouseDetil.ViewDataBean)arg0.getTag());
                readyGo(HouseDetilActivity.class,bundle);*/
                return false;
            }
        });
    }

    protected void bindListener() {

        // TODO Auto-generated method stub
        int error = locationManager.requestLocationUpdates(
                locationRequest,this);
        switch (error) {
            case 0:
                Log.e("location", "成功注册监听器");
                break;
            case 1:
                Log.e("location", "设备缺少使用腾讯定位服务需要的基本条件");
                break;
            case 2:
                Log.e("location", "manifest 中配置的 key 不正确");
                break;
            case 3:
                Log.e("location", "自动加载libtencentloc.so失败");
                break;

            default:
                break;
        }


    }
    @Override
    protected void initView(View view) {
        Log.e("getData","进入initView");
        getData();
    }

    @Override
    public void init() {
        Log.e("getData","进入init");

    }
    public void getData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter.addHeader(headerView);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                adapter.resumeMore();
            }

            @Override
            public void onNoMoreClick() {
                recyclerView.scrollToPosition(0);
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadMore();
                    }
                }, 3000);
            }
        });
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle =new Bundle();
                bundle.putSerializable("bean",viewDataBean.get(position));
                readyGo(HouseDetilActivity.class,bundle);
            }
        });
        isFirst=true;
        requestData();
    }

    private void requestData() {
        HttpMethods.getInstance().homeData(new BaseObserver<HouseDetil>() {
            @Override
            protected void onSuccees(BaseBean t) throws Exception {
                HouseDetil houseDetil= (HouseDetil) t;
                List<String> stringList=new ArrayList<>();
                if (isFirst) {
                    adapter.clear();
                    bannerDataBeans=houseDetil.getBannerData();
                    for (int i = 0; i < bannerDataBeans.size(); i++) {
                        images.add("http://123.207.91.208"+bannerDataBeans.get(i).getBannerImage());
                        stringList.add(bannerDataBeans.get(i).getBannerTitle());
                    }
                    banner.setBannerTitles(stringList);
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent=new Intent(getActivity(), WebVActivity.class);
                            intent.putExtra("url",bannerDataBeans.get(position).getBannerURL());
                            startActivity(intent);
                        }
                    });
                    banner.setImages(images).setImageLoader(new BannerPicassoImageLoader()).start();
                    isFirst=false;
                }
                if (page==1) {
                    adapter.removeAll();
                    adapter.clear();
                    viewDataBean=houseDetil.getViewDataX();
                }else {
                    for (int i = 0; i < houseDetil.getViewDataX().size(); i++) {
                        viewDataBean.add(houseDetil.getViewDataX().get(i));
                    }
                }
                System.out.println(houseDetil.getViewData());
                //Log.e("requestData",houseDetil.getViewDataX().get(0).toString());
                Log.e("requestData",houseDetil.getViewDataX().size()+"条信息");
                if (!isMapMode) {
                    Log.e("requestData","添加一次数据信息>>>>adapter="+adapter.getCount()+"条》》》》》》viewDataBean="+viewDataBean.size());
                    if (houseDetil.getViewDataX().size()>0) {
                        adapter.addAll(houseDetil.getViewDataX());
                    }
                } else {
                    adapter.stopMore();
                }
            }
            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
            }
        },page+"",row+"",longitude+"",latitude+"","","","","","","","","","","","true","","","","","","","");
    }

    private void joinFamily(String roomId,String idCard,String name) {
        //TODO:
        HttpMethods.getInstance().joinFamily(new BaseObserver<BrokenUp>() {
            @Override
            protected void onSuccees(BaseBean t) throws Exception {
                BrokenUp houseDetil= (BrokenUp) t;
                Log.e("getData","执行joinFamily方法返回"+houseDetil.getMessage());
                System.out.println(t.getMessage()+"");
                Toast.makeText(getActivity(), houseDetil.getMessage(), Toast.LENGTH_SHORT).show();
                //adapter.addAll(viewDataBeanList);
            }
            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        },roomId,phone,idCard,name,phone,"","","","","","","");
    }



    private  boolean isFist =true;
    private Marker myLocation;
    private Circle accuracy;
    private double longitude=113.27324,latitude=23.15792;
    @Override
    public void onLocationChanged(TencentLocation arg0, int arg1, String s) {
        if (longitude!=arg0.getLongitude()||latitude!=arg0.getLatitude()){
            longitude=arg0.getLongitude();
            latitude=arg0.getLatitude();
            PreferencesUtils.putString(mContext,"longitude",longitude+"");
            PreferencesUtils.putString(mContext,"latitude",latitude+"");
        }
        Log.e("onLocationChanged","longitude="+longitude);
        Log.e("onLocationChanged","latitude="+latitude);
        //adapter.addAll(viewDataBean);
        if (arg1 == TencentLocation.ERROR_OK) {
            LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
            if (myLocation == null) {
                myLocation = tencentMap.addMarker(new MarkerOptions().
                        position(latLng).
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.navigation)).
                        anchor(0.5f, 0.5f));
            }
            if(isFist){
                tencentMap.setCenter(latLng);
                isFist=false;
            }

            /*if (accuracy == null) {
                accuracy = tencentMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius((double)arg0.getAccuracy()).
                        fillColor(0x440000ff).
                        strokeWidth(0f));
            }
            myLocation.setPosition(latLng);
            myLocation.setRotation(arg0.getBearing()); //仅当定位来源于gps有效，或者使用方向传感器
            accuracy.setCenter(latLng);
            accuracy.setRadius(arg0.getAccuracy());*/

        } else {
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    private void LoadMore() {
        page++;
        requestData();
    }
    private Handler handler = new Handler();


    @Override
    public void onRefresh() {
        final boolean aa=isMapMode;
        page = 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //刷新
                if (!AppUtils.isNetworkAvailable(mContext)) {
                    adapter.pauseMore();
                    return;
                }
                requestData();
                isMapMode=aa;
                //page=1;
            }
        }, 2000);
    }

    @Override
    protected void initLogic() {
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {

    }


    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapview!=null) {
            mapview.onResume();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        mapview.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onMoreShow() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadMore();
            }
        }, 2000);
    }

    @Override
    public void onMoreClick() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadMore();
            }
        }, 2000);
    }
}
