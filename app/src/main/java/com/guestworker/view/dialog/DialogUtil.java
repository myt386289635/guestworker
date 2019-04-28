package com.guestworker.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guestworker.R;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public class DialogUtil {

    public static void LoginDialog(Context context, String titleStr, String sureText , String cancleText, final View.OnClickListener sureListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_log,null);
        TextView title = view.findViewById(R.id.dialog_title);
        title.setText(titleStr);
        TextView sure =  view.findViewById(R.id.dialog_sure);
        sure.setText(sureText);
        final Dialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0.35f);
        sure.setOnClickListener(view1 -> {
            sureListener.onClick(view1);
            dialog.dismiss();
        });
        TextView cancle =  view.findViewById(R.id.dialog_cancle);
        cancle.setText(cancleText);
        cancle.setOnClickListener(view12 -> dialog.dismiss());
        dialog.show();
        dialog.setContentView(view);
    }

    public static Dialog payDialog(Context context , Bitmap bitmap, final View.OnClickListener sureListener, final DissListener dissListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pay,null);
        TextView sure =  view.findViewById(R.id.dialog_sure);
        ImageView dialog_code = view.findViewById(R.id.dialog_code);
        dialog_code.setImageBitmap(bitmap);
        final Dialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0.35f);
        sure.setOnClickListener(view1 -> {
            sureListener.onClick(view1);
            dialog.dismiss();
        });
        ImageView cancle =  view.findViewById(R.id.dialog_cancle);
        cancle.setOnClickListener(view12 ->{
            if (dissListener != null){
                dissListener.onClick(dialog);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }

    public interface DissListener{
        void onClick(Dialog dialog);
    }

    public static  void SingleDialog(Context context,String titleStr,String sureText , final View.OnClickListener sureListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_single,null);
        TextView title = view.findViewById(R.id.dialog_title);
        title.setText(titleStr);
        TextView sure =  view.findViewById(R.id.dialog_sure);
        sure.setText(sureText);
        final Dialog dialog = builder.create();
        dialog.getWindow().setDimAmount(0.35f);
        sure.setOnClickListener(v -> {
            sureListener.onClick(v);
            dialog.dismiss();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(view);
    }

}
