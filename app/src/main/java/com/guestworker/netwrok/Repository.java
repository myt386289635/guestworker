package com.guestworker.netwrok;

import com.guestworker.bean.AreaUserBean;
import com.guestworker.bean.ClassifyBean;
import com.guestworker.bean.DetailBean;
import com.guestworker.bean.IsLoginBean;
import com.guestworker.bean.ListBean;
import com.guestworker.bean.LoginBean;
import com.guestworker.bean.MyUserBean;
import com.guestworker.bean.OrderBean;
import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;
import com.guestworker.bean.PayResultBean;
import com.guestworker.bean.SystemBean;
import com.guestworker.bean.UserInfoBean;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

public class Repository {

    @Inject
    APIService mAPIService;

    @Inject
    public Repository() {
    }

    /**
     * 获取系统变量
     */
    public Observable<SystemBean> systemVariables() {
        return mAPIService.systemVariables();
    }

    /**
     * 登录
     */
    public Observable<LoginBean> login( String mobile, String password){
        return mAPIService.login(mobile, password);
    }

    /**
     * 商品分类
     */
    public Observable<ClassifyBean> goodsTypes(String parentgid){
        return mAPIService.goodsTypes(parentgid);
    }

    /**
     * 分页获取客工下属的会员
     */
    public Observable<MyUserBean> myMembers( String pageon ){
        return mAPIService.myMembers(pageon,RetrofitModule.pageSize);
    }

    /**
     * 分页获取运营中心会员，客工app代客下单时选择会员使用
     */
    public Observable<AreaUserBean> areaMembers(String pageon ){
        return mAPIService.areaMembers(pageon,RetrofitModule.pageSize);
    }

    /**
     * 用户是否登陆
     */
    public Observable<IsLoginBean> isLogin(){
        return mAPIService.isLogin();
    }

    /**
     * 获取客工个人信息
     */
    public Observable<UserInfoBean> getUserInfo(){
        return mAPIService.getUserInfo();
    }

    /**
     * 商品列表
     */
    public Observable<ListBean> goodsList(Map<String,String> map){
        return mAPIService.goodsList(map);
    }

    /**
     * 获取商品详情
     */
    public Observable<DetailBean> goodsDetail(String gid){
        return mAPIService.goodsDetail(gid);
    }

    /**
     * 生成订单
     */
    public Observable<OrderSaveBean> orderSave(OrderBean bean){
        return mAPIService.orderSave(bean);
    }

    /**
     * 生成二维码链接
     */
    public Observable<PayCodeBean> payCode(String tradeNo){
        return mAPIService.payCode(tradeNo,1,"NATIVE");
    }

    /**
     * 用户获取订单明细(用于扫码支付后的回调)
     */
    public Observable<PayResultBean> payResult(String orderID , String userID){
        return mAPIService.payResult(orderID,userID);
    }

}
