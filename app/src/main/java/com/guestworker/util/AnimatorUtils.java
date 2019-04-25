package com.guestworker.util;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guestworker.R;

/**
 * @author 莫小婷
 * @create 2019/4/16
 * @Describe //加入购物车动画
 */
public class AnimatorUtils {

    /**
     * 加入购物车动画
     * @param activity  上下文
     * @param carTextView  贝塞尔曲线的出发view
     * @param cartView    贝塞尔曲线的结束view
     * @param parentView   父布局  整个布局的父布局只是用来辅助计算抛物线的起始位置和终点位置
     * @param listener   动画结束时的回调
     */
    public static void doCartAnimator(Activity activity,ImageView carTextView,
                                      View cartView, final ViewGroup parentView,
                                      final OnAnimatorListener listener) {
        //第一步：
        //创造出执行动画的主题---imageView
        //代码new一个imageView，图片资源是上面的imageView的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        if (activity == null || cartView == null || parentView == null) return;
        final ImageView goods = new ImageView(activity);
        goods.setPadding(1, 1, 1, 1);
        //图片切割方式
        goods.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //获取图片资源
//        goods.setImageResource(R.color.color_DC3D27);
        goods.setImageDrawable(carTextView.getDrawable());
        //设置RelativeLayout容器(这里必须设置RelativeLayout 设置LinearLayout动画会失效)
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) activity.getResources().getDimension(R.dimen.x100),(int) activity.getResources().getDimension(R.dimen.y100));
        //把动画view添加到动画层
        parentView.addView(goods, params);

        //第二步:
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        parentView.getLocationInWindow(parentLocation);
        //获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）//获取商品图片在屏幕中的位置
        int startLoc[] = new int[2];
        carTextView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        cartView.getLocationInWindow(endLoc);

        //第三步:
        //正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX =  startLoc[0] - parentLocation[0] + carTextView.getWidth() / 2;// 动画开始的X坐标
        float startY = startLoc[1] - parentLocation[1] + carTextView.getHeight() / 2;//动画开始的Y坐标

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0]  - parentLocation[0] + cartView.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        //第四步:
        //计算中间动画的插值坐标，绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //第一个起始坐标越大，贝塞尔曲线的横向距离就会越大 toX,toY:为终点
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        //实现动画具体博客可参考 鸿洋大神的https://blog.csdn.net/lmj623565791/article/details/38067475
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        //设置动画时间
        valueAnimator.setDuration(700);
        //LinearInterpolator补间器:它的主要作用是可以控制动画的变化速率，比如去实现一种非线性运动的动画效果
        //具体可参考郭霖大神的：https://blog.csdn.net/guolin_blog/article/details/44171115
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(animation -> { //更新动画
            float value = (Float) animation.getAnimatedValue();
            float[] currentPosition = new float[2];
            pathMeasure.getPosTan(value, currentPosition, null);
            goods.setTranslationX(currentPosition[0]);//改变了ImageView的X位置
            goods.setTranslationY(currentPosition[1]);//改变了ImageView的Y位置
        });

        //第五步:
        //开始执行动画
        valueAnimator.start();

        //第六步:
        //对动画添加监听
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //onAnimationStart()方法会在动画开始的时候调用
            }

            //onAnimationEnd()方法会在动画结束的时候调用
            @Override
            public void onAnimationEnd(Animator animation) {
                //把移动的图片imageView从父布局里移除
                parentView.removeView(goods);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //onAnimationCancel()方法会在动画被取消的时候调用
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //onAnimationRepeat()方法会在动画重复执行的时候调用
            }
        });
    }

    //上下抖动 用于吸引用户去点击 防京东购物车的上下抖动
    public static void viewRotate(View view) {
        if (view == null) return;
        float shakeFactor = 5f;

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000).start();
    }

    public interface OnAnimatorListener {
        void onAnimationEnd(Animator animator);
    }

}
