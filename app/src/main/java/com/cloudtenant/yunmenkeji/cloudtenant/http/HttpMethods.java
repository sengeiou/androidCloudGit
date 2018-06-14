package com.cloudtenant.yunmenkeji.cloudtenant.http;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zt on 2017/3/10.
 */

public class HttpMethods {

    private static final String BASE_URL = "http://192.168.1.166/";
    private static final int TIME_OUT=4;
    private Retrofit retrofit;
    private ApiService apiService;

    private HttpMethods() {
        /**
         * 构造函数私有化
         * 并在构造函数中进行retrofit的初始化
         */

        /**
         * 由于retrofit底层的实现是通过okhttp实现的，所以可以通过okHttp来设置一些连接参数
         * 如超时等
         */
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService=retrofit.create(ApiService.class);

    }


    private static class sinalInstance {
        public static final HttpMethods instance = new HttpMethods();
    }

    public  static HttpMethods getInstance(){
        return sinalInstance.instance;
    }
//   public  void getAllBuoyHead(BaseObserver<BuoyListBean> observer){
//        apiService.getAllBuoyHead().subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//
//    }
//
//    public void uploadIcon(Observer<ImageBean> observer, File file, String userId){
//
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file",file.getName(), requestFile);
//        // 添加描述
//
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), userId);
//        apiService.upload(description,body).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//
//    }
//    public void login(BaseObserver<UserBean> observer, UserBean.DataBean userBean){
//        apiService.login(userBean).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//
//    }
//    public void updateUserInfo(BaseObserver<UserBean.DataBean> observer, UserBean.DataBean userBean){
//        apiService.updateUserInfo(userBean).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//
//    }
//    public void getUserInfo(BaseObserver<UserBean.DataBean> observer, String  userId){
//        apiService.getUserInfo(userId).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
//
//
//    }
//    public void searchBuoySimpleByType(BaseObserver<BuoyListBean> observer, String  userType){
//        apiService.searchBuoySimpleByType(userType).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
//
//
//    }
//    public void   getBuoyHistorysById(BaseObserver<BuoyHistoryList> observer, HistoryParmerBean historyParmerBean){
//        apiService.getBuoyHistorysById(historyParmerBean).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
//    }
   /* public void getJoke(Observer<List<MyJoke>> observer){

        apiService.getData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }*/
}

