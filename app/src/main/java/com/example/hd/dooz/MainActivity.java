package com.example.hd.dooz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    ImageView circleButton ;
    ImageView crossButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleButton = (ImageView) findViewById(R.id.circleButton);
        crossButton = (ImageView) findViewById(R.id.crossButton);
//        circleButton.setImageAlpha(51);
//        crossButton.setImageAlpha(51);

    }
    boolean selected =false;
    public void Select(View view){
        if (selected) return;
        selected = true;
        final ImageView selected;
        ImageView nonSelected;
        selected = view.getId()==circleButton.getId()?circleButton:crossButton;
        nonSelected = view.getId()==circleButton.getId()?crossButton:circleButton;
        AlphaAnimation fadeIn = new AlphaAnimation(0.8f, 1.0f);
        AlphaAnimation fadeOut = new AlphaAnimation(0.8f, 0.5f);
        fadeIn.setDuration(1200);
        fadeOut.setDuration(12000);
        fadeIn.setFillAfter(true);
        fadeOut.setFillAfter(true);
        selected.startAnimation(fadeIn);
        nonSelected.startAnimation(fadeOut);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("human",selected.getId()==circleButton.getId()?Main2Activity.CIRCLE_PLAYER:Main2Activity.CROSS_PLAYER);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        selected.animate().scaleY(1.2f).scaleX(1.2f).setDuration(1000);
        nonSelected.animate().setDuration(1000);


    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();;
//        crossButton.setScaleX(1f);
//        crossButton.setScaleY(1f);
//        circleButton.setScaleY(1f);
//        circleButton.setScaleX(1f);
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Select(v);
//            }
//        };
//        circleButton.setOnClickListener(onClickListener);
//        crossButton.setOnClickListener(onClickListener);
//    }
}
