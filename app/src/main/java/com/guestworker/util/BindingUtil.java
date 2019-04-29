package com.guestworker.util;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.guestworker.R;
import com.guestworker.netwrok.RetrofitModule;
import com.guestworker.util.glide.GlideApp;
import com.guestworker.util.sp.CommonDate;
import com.guestworker.view.textView.TextViewDel;

import java.math.BigDecimal;

/**
 * @author 莫小婷
 * @create 2019/3/15
 * @Describe BindingAdapter工具类
 */
public class BindingUtil {

    @BindingAdapter("bind:srcRound")
    public static void setImage(ImageView imageView, String src){
        if (TextUtils.isEmpty(RetrofitModule.IMG_URL)){
            src = SPUtils.getInstance().getString(CommonDate.IMG_URL,"") + src;
        }else {
            src = RetrofitModule.IMG_URL + src;
        }
        GlideApp.loderImage(imageView.getContext(), src,imageView, R.color.transparent,R.color.transparent);
    }

    @BindingAdapter("bind:src")
    public static void setImageRound(ImageView imageView, String src){
        if (TextUtils.isEmpty(RetrofitModule.IMG_URL)){
            src = SPUtils.getInstance().getString(CommonDate.IMG_URL,"") + src;
        }else {
            src = RetrofitModule.IMG_URL + src;
        }
        GlideApp.loderCircleImage(imageView.getContext(), src,imageView, R.color.transparent,R.color.transparent);
    }

    @BindingAdapter("bind:headImg")
    public static void setHeadImg(ImageView imageView, String src){
        if (TextUtils.isEmpty(src)){
            GlideApp.loderCircleImage(imageView.getContext(), R.mipmap.default_img,imageView, R.color.transparent,R.color.transparent);
        }else {
            GlideApp.loderCircleImage(imageView.getContext(), src,imageView, R.color.transparent,R.color.transparent);
        }
    }

    @BindingAdapter("bind:price")
    public static void setPrice(TextView textView , double price){
        BigDecimal bigDecimal = new BigDecimal(price);
        textView.setText("¥" + bigDecimal.setScale(2,BigDecimal.ROUND_DOWN));
    }

    @BindingAdapter("bind:oldPrice")
    public static void setOldPrice(TextViewDel textView , double oldPrice){
        BigDecimal bigDecimal = new BigDecimal(oldPrice);
        textView.setText("¥" + bigDecimal.setScale(2,BigDecimal.ROUND_DOWN));
        textView.setTv(true);
    }

    @BindingAdapter("bind:web")
    public static void setWeb(WebView webView ,String webString){
//        String head = "<head>" +
//                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
//                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
//                "</head>";
//        webString = "<html>" + head + "<body>" + webString + "</body></html>";
        webView.loadDataWithBaseURL(null,
                webString, "text/html", "utf-8", null);
    }

    @BindingAdapter("bind:textNum")
    public static void int2String(TextView textView,int num){
        textView.setText(num + "");
    }
}
