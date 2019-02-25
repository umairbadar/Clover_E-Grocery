package com.example.lubna.cloverweb;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class splashscreen extends AwesomeSplash
{
    @Override
    public void initSplash(ConfigSplash configSplash)
    {
        configSplash.setBackgroundColor(R.color.back1);//any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP
        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default
        //Customize Logo
        configSplash.setLogoSplash(R.drawable.clover_logo_final); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000);
        //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)
        //Customize Path
        //configSplash.setPathSplash(SyncStateContract.Constants.); set path String
        configSplash.setOriginalHeight(500); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(500); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(2500);
        configSplash.setPathSplashFillColor(R.color.clovergreen); //path object filling color
        //Customize Title
        configSplash.setTitleSplash("E-Grocery");
        //change your app name here
        configSplash.setTitleTextColor(R.color.clovergreen);
        configSplash.setTitleTextSize(50f);
        //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }
    @Override
    public void animationsFinished() {

            Intent i = new Intent(getApplicationContext(),egrocery.class);
            startActivity(i);
            finish();

    }
}
