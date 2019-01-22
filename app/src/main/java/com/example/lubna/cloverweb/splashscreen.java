package com.example.lubna.cloverweb;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
public class splashscreen extends AppCompatActivity
{
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
       StartAnimations();
    }
    Thread splashTread;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
    }
    private void StartAnimations()
    {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
//        LinearLayout l= findViewById(R.id.lin_lay);
//        l.clearAnimation();
//        l.startAnimation(anim);
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
        ImageView iv =  findViewById(R.id.splash);
//        iv.clearAnimation();
//        iv.startAnimation(anim);
        splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    while (waited < 3500)
                    {
                        waited += 200;
                    }
                    Intent intent = new Intent(splashscreen.this, egrocery.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    splashscreen.this.finish();
//
                }
                finally
                {
                    splashscreen.this.finish();
                }
            }
        };
        splashTread.start();
  }
}
