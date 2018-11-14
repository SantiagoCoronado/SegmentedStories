package mx.nextia.segmentedstories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity {

    View reveal;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        reveal = findViewById(R.id.reveal);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, StoriesActivity.class));
                //Bungee.inAndOut(MainActivity.this);
                revealButton();
                fadeoutBtn();
                waitThread();
            }
        });
    }

    public void revealButton(){
        btn.setElevation(0f);
        reveal.setVisibility(View.VISIBLE);

        int x = (int) (getFabWidth() / 2 + btn.getX());
        int y = (int) (getFabWidth() / 2 + btn.getY());

        int cx = reveal.getWidth();
        int cy = reveal.getHeight();

        float finalRadius = Math.max(cx,cy) * 1.2f;
        Animator revealer = ViewAnimationUtils
                .createCircularReveal(reveal, x, y, getFabWidth(), finalRadius);

        revealer.setDuration(250);
        revealer.start();

    }

    public void revealButtonBack(){
        btn.setElevation(1f);

        int x = reveal.getWidth();
        int y = reveal.getHeight();

        int cx = (int) (getFabWidth() / 2 + btn.getX());
        int cy = (int) (getFabWidth() / 2 + btn.getY());

        float finalRadius = Math.max(x,y) * 1.2f;
        Animator revealer = ViewAnimationUtils
                .createCircularReveal(reveal, cx, cy, finalRadius, getFabWidth());
        revealer.setDuration(250);
        revealer.start();
    }

    public void fadeoutBtn(){
        btn.animate().alpha(0f).setDuration(100).start();
    }

    public void fadeInBtn(){
        btn.animate().alpha(1f).setDuration(100).start();
    }

    private int getFabWidth(){
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    public void waitThread(){
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(250);  //Delay of .1 seconds
                } catch (Exception e) {

                } finally {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //finish();
                            startActivityForResult(new Intent(MainActivity.this, StoriesActivity.class), 1);
                            overridePendingTransition(0,0);

                        }
                    });
                }
            }
        };
        welcomeThread.start();
    }

    public void waitThread2(){
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(250);  //Delay of .1 seconds
                } catch (Exception e) {

                } finally {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reveal.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        };
        welcomeThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            //btn.setAlpha(1f);
            //reveal.setVisibility(View.INVISIBLE);
            revealButtonBack();
            fadeInBtn();
            waitThread2();
        }
    }
}
