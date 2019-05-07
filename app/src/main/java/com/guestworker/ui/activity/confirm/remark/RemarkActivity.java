package com.guestworker.ui.activity.confirm.remark;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.guestworker.R;
import com.guestworker.base.BaseActivity;
import com.guestworker.bean.eventbus.Remarkbus;
import com.guestworker.databinding.ActivityRemarkBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * @author 莫小婷
 * @create 2019/5/6
 * @Describe 添加备注
 */
public class RemarkActivity extends BaseActivity implements View.OnClickListener {

    private ActivityRemarkBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_remark);
        mBinding.setListener(this);
        mBaseActivityComponent.inject(this);
        initView();
    }

    private void initView() {
        mBinding.inClude.titleTv.setText("添加备注");
        mBinding.opinionEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        initText(mBinding.opinionEdit,mBinding.opinionText);
        if (!getIntent().getStringExtra("content").replace("备注：","").equals("添加备注")){
            mBinding.opinionEdit.setText(getIntent().getStringExtra("content").replace("备注：",""));
            String html1 = "您还可以输入<font color='#E31436' size='20'>"+(20-mBinding.opinionEdit.getText().toString().length())+"</font>个字";
            mBinding.opinionText.setText(Html.fromHtml(html1));
        }else {
            String html = "您还可以输入<font color='#E31436' size='20'>"+20+"</font>个字";
            mBinding.opinionText.setText(Html.fromHtml(html));
        }
        showKeyboardDelayed(mBinding.opinionEdit);
        mBinding.opinionEdit.setSelection(mBinding.opinionEdit.getText().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.opinion_submit:
                String remark = "";
                if (!TextUtils.isEmpty(mBinding.opinionEdit.getText().toString().trim())){
                    remark = mBinding.opinionEdit.getText().toString().trim();
                }
                if (getIntent().getBooleanExtra("isHome",false)){
                    //刷新首页购物车
                    EventBus.getDefault().post(new Remarkbus(remark));
                }
                Intent intent = new Intent();
                intent.putExtra("remark",remark);
                setResult(334,intent);
                finish();
                break;
        }
    }

    public void initText(final EditText mOpinionEdit , final TextView mOpinionText){
        mOpinionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String html = "您还可以输入<font color='#E31436' size='20'>"+(20-s.length())+"</font>个字";
                mOpinionText.setText(Html.fromHtml(html));
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                hideSoftInput();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideSoftInput() {
        if (mImm.isActive()) {
            // 如果开启
            mImm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
