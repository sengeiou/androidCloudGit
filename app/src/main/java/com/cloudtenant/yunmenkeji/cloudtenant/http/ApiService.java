package com.cloudtenant.yunmenkeji.cloudtenant.http;


import com.cloudtenant.yunmenkeji.cloudtenant.bean.MessageOther;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.MessageSave;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.MyContract;
import com.cloudtenant.yunmenkeji.cloudtenant.bean.RoomMessageHistory;
import com.cloudtenant.yunmenkeji.cloudtenant.model.BaseBean;
import com.cloudtenant.yunmenkeji.cloudtenant.model.HouseDetil;
import com.cloudtenant.yunmenkeji.cloudtenant.model.MyRoom;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zt on 2017/3/10.
 */

public interface ApiService {
    /**
     *获取所有浮标
     * @return <List<BuoyListBean>>
     */
// @GET("buoy/getAllSimpleData")
// Observable<BuoyListBean> getAllBuoyHead();
//
//    @GET("buoy/searchBuoySimpleByType")
//    Observable<BuoyListBean> searchBuoySimpleByType(@Query("type") String type);
//
//    @POST("user/login")
//    Observable<UserBean>  login(@Body UserBean.DataBean userBean);
//    @POST("user/updateUserInfo")
//    Observable<UserBean>  updateUserInfo(@Body UserBean.DataBean userBean);
// @Multipart
//    @POST("user/uploadIcon")
//    Observable<ImageBean> upload(@Part("userId") RequestBody description,
//                                 @Part MultipartBody.Part file);
//    @GET("user/getUserInfo")
//    Observable<UserBean> getUserInfo(@Query("userId") String userId);
//
//    @POST ("buoy/getBuoyHistorysById")
//    Observable<BuoyHistoryList> getBuoyHistorysById(@Body HistoryParmerBean userBean);
       @POST("Login")
       Observable<BaseBean> login();
       @POST("HomeData")
       Observable<HouseDetil> homeData();
       @POST("MessageSave")
       Observable<MessageSave> messageSave();
       @POST("OtherMessage")
       Observable<MessageOther> otherMessage();
       @POST("RoomMessageHistory")
       Observable<RoomMessageHistory> roomMessageHistory();
       @POST("BrokenUpDone")
       Observable<BaseBean> brokenUpDone();
       @POST("MyContract")
       Observable<MyContract> myContract();



       @POST("MyRoom")
       Observable<MyRoom> MyRoom();


}
