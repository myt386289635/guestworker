package com.guestworker.netwrok;

import com.guestworker.bean.AddressBean;
import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.ClassifyBean;
import com.guestworker.bean.DetailBean;
import com.guestworker.bean.InvitationBean;
import com.guestworker.bean.IsLoginBean;
import com.guestworker.bean.ListBean;
import com.guestworker.bean.LoginBean;
import com.guestworker.bean.MyUserBean;
import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;
import com.guestworker.bean.SystemBean;
import com.guestworker.bean.UploadFileBean;
import com.guestworker.bean.UserInfoBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIService {

    /**
     * 获取系统变量
     */
    @GET("/app/variables.do")
    Observable<SystemBean> systemVariables();

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/app/user/SalesLogin.do")
    Observable<LoginBean> login(@Field("mobile") String mobile, @Field("password") String password);

    /**
     * 商品分类
     */
    @GET("/app/goods/types.do")
    Observable<ClassifyBean> goodsTypes(@Query("parentgid") String parentgid);

    /**
     * 分页获取客工下属的会员 (401)
     */
    @GET("/app/my/sales/members.do")
    Observable<MyUserBean> myMembers(@Query("pageon") String pageon , @Query("pageSize") String pageSize);

    /**
     * 分页获取运营中心会员，客工app代客下单时选择会员使用 (401)
     */
    @GET("/app/my/area/members.do")
    Observable<AreaUserBean> areaMembers(@QueryMap Map<String ,String> map);

    /**
     * 用户是否登陆
     */
    @GET("/app/user/IsLogin.do")
    Observable<IsLoginBean> isLogin();

    /**
     * 获取客工个人信息
     */
    @GET("/app/my/user/getUserInfo.do")
    Observable<UserInfoBean> getUserInfo();

    /**
     * 商品列表
     */
    @GET("/app/goods/list.do")
    Observable<ListBean> goodsList(@QueryMap Map<String,String> map);

    /**
     * 获取商品详情
     */
    @GET("/app/goods/detail.do")
    Observable<DetailBean> goodsDetail(@Query("gid") String gid);

    /**
     * 生成订单
     */
    @POST("/app/my/order/OrderSave.do")
    Observable<OrderSaveBean> orderSave(@Body OrderBean bean);

    /**
     * 生成二维码链接
     */
    @GET("/my/pay/PlaceOrder.do")
    Observable<PayCodeBean> payCode(@Query("tradeNo") String tradeNo,@Query("payType") int payType , @Query("tradeType") String tradeType);

    /**
     * 用户获取订单明细(用于扫码支付后的回调)
     */
    @FormUrlEncoded
    @POST("/app/my/order/orderInfo.do")
    Observable<PayResultBean> payResult(@Field("orderID") String orderID ,@Field("userID") String userID);

    /**
     * 新增地址
     */
    @FormUrlEncoded
    @POST("/app/my/member/AddAddress.do")
    Observable<AddressBean> addAddress(@Field("userName")String userName ,@Field("mobile")String mobile,@Field("address")String address,@Field("regionCode")String regionCode,@Field("isDefault")String isDefault,@Field("userId")String userId);

    /**
     * 获取客工邀请码和生成二维码的url
     */
    @GET("/app/my/sales/salescode.do")
    Observable<InvitationBean> salescode();

    /**
     * 上传图片
     */
    @Multipart
    @POST("/app/my/UploadFile.do")
    Observable<UploadFileBean> uploadFile(@Part("uploadFileType") int uploadFileType, @Part("id") int id, @Part MultipartBody.Part importFile);
}
