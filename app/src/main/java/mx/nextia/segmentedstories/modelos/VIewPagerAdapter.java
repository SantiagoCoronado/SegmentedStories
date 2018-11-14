package mx.nextia.segmentedstories.modelos;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

import mx.nextia.segmentedstories.R;

public class VIewPagerAdapter extends PagerAdapter implements View.OnTouchListener{

    ArrayList<StoriesUser> al_stories_user;
    LayoutInflater inflater;
    Context context;
    int counter;
    SegmentedProgressBar bar;
    long cStart, cStop, startmillis, start_pause;
    int positionUser;
    Timer timer;
    ImageView iv_stories, iv_back;
    TextureVideoView videoView;
    private float rawYStart, rawYEnd;

    /**
     * importante : cuando le das next y el video no ha cargado, el video player no reconoce el pause. SOLUCIONAR ESTO
     */

    public VIewPagerAdapter(ArrayList<StoriesUser> al_stories_user, Context context) {
        this.al_stories_user = al_stories_user;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al_stories_user.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rl = inflater.inflate(R.layout.layout_historias, container, false);
        bar = (SegmentedProgressBar)rl.findViewById(R.id.segmented_progress);
        iv_stories = (ImageView)rl.findViewById(R.id.iv_historias);
        iv_back = (ImageView)rl.findViewById(R.id.iv_back);
        videoView = rl.findViewById(R.id.videoview);
        videoView.setScaleType(TextureVideoView.ScaleType.CENTER_CROP);
        positionUser = position;
        counter = 0;
        bar.setSegmentCount(al_stories_user.get(position).al_historias.size());
        bar.playSegment(al_stories_user.get(position).al_historias.get(0).duration);
        iv_stories.setOnTouchListener(VIewPagerAdapter.this);
        bar.pause();

        if(al_stories_user.get(position).al_historias.get(0).tipo == 1) {
            Picasso.get().load(al_stories_user.get(position).al_historias.get(0).url).into(iv_stories, new Callback() {
                @Override
                public void onSuccess() {
                    bar.resume();
                    newTimer(al_stories_user.get(position).al_historias.get(0).duration);
                    startmillis = System.currentTimeMillis();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else if (al_stories_user.get(position).al_historias.get(0).tipo == 2){
            videoView.setVisibility(View.VISIBLE);
            //videoView.setVideoPath(al_stories_user.get(position).al_historias.get(0).url);
            //videoView.start();
            videoView.setDataSource(al_stories_user.get(position).al_historias.get(0).url);
            videoView.play();
            bar.resume();
            newTimer(al_stories_user.get(position).al_historias.get(0).duration);
            startmillis = System.currentTimeMillis();
        }

        iv_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        bar.pause();
                        if(videoView.getVisibility() == View.VISIBLE){
                            if(videoView.mMediaPlayer.isPlaying()){
                                videoView.pause();
                            }else {
                                videoView.mMediaPlayer.reset();
                            }

                        }
                        cStart = System.currentTimeMillis();
                        start_pause = startmillis - cStart;
                        globalTimer.cancel();
                        break;
                    case MotionEvent.ACTION_UP:
                        //bar.resume();
                        cStop = System.currentTimeMillis();
                        Log.i("TIMER_START", String.valueOf(cStart));
                        Log.i("TIMER_STOP", String.valueOf(cStop));
                        if(cStop < cStart+120L ){
                            if (counter>0) {
                                counter--;
                                storesLogic();
                            }else {
                                storesLogic();
                            }
                        }else {
                            bar.resume();
                            globalTimer.cancel();
                            newTimer(al_stories_user.get(positionUser).al_historias.get(counter).duration - start_pause);
                            if (videoView.getVisibility() == View.VISIBLE){
                                //videoView.resume();
                                videoView.play();

                            }
                        }
                        break;
                }
                return true;
            }
        });
        ((ViewPager)container).addView(rl);

        return rl;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                bar.pause();
                cStart = System.currentTimeMillis();
                start_pause = cStart - startmillis;
                Log.i("START_PAUSE", String.valueOf(start_pause));
                globalTimer.cancel();
                if(videoView.getVisibility() == View.VISIBLE){
                    if(videoView.mMediaPlayer.isPlaying()){
                        videoView.pause();
                    }else {
                        videoView.mMediaPlayer.reset();
                    }

                }
                rawYStart = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //bar.resume();
                rawYEnd = event.getRawY();
                cStop = System.currentTimeMillis();
                // aqui
                float deltaTime = (float) (cStop - cStart);
                float deltaDistance = rawYEnd - rawYStart;
                float velocity = deltaDistance/deltaTime;
                Log.i("VELOCITY", String.valueOf(velocity));

                if(velocity > 3f){
                    Activity activity = (Activity)context;
                    activity.finish();
                    activity.overridePendingTransition(0,0);
                    videoView.mMediaPlayer.reset();
                }

                Log.i("TIMER_START", String.valueOf(cStart));
                Log.i("TIMER_STOP", String.valueOf(cStop));
                if(cStop < cStart+120L ){
                    counter++;
                    storesLogic();
                }else {
                    //aqui
                    bar.resume();
                    if (videoView.getVisibility() == View.VISIBLE){
                        //videoView.resume();
                        videoView.play();
                    }
                    globalTimer.cancel();
                    newTimer(al_stories_user.get(positionUser).al_historias.get(counter).duration - start_pause);
                }
                break;
        }
        return true;
    }

    private Timer globalTimer;

    public TimerTask newTimerTask(){

            return new TimerTask() {
                @Override
                public void run() {
                    Activity activity = (Activity) context;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "TERMINADO", Toast.LENGTH_SHORT).show();
                            final int contador = counter;
                            counter++;
                            storesLogic();
                        }
                    });
                }
            };
    }


    public void newTimer(long duration){
        globalTimer = new Timer();
        globalTimer.schedule(newTimerTask(),duration);
        //timer.schedule(timerTask, duration);

    }

    public void storesLogic(){
        if (counter < al_stories_user.get(positionUser).al_historias.size()) {
            //globalTimer.cancel();
            bar.setCompletedSegments(counter);
            Log.i("URL", al_stories_user.get(positionUser).al_historias.get(counter).url);
            if (al_stories_user.get(positionUser).al_historias.get(counter).tipo == 1) {
                videoView.setVisibility(View.INVISIBLE);
                Picasso.get().load(al_stories_user.get(positionUser).al_historias.get(counter).url).into(iv_stories, new Callback() {
                    @Override
                    public void onSuccess() {
                        bar.playSegment(al_stories_user.get(positionUser).al_historias.get(counter).duration);
                        newTimer(al_stories_user.get(positionUser).al_historias.get(counter).duration);
                        startmillis = System.currentTimeMillis();
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.i("ERROR", e.getMessage());
                    }
                });
            }else if(al_stories_user.get(positionUser).al_historias.get(counter).tipo == 2){
                //videoView.setVideoPath(al_stories_user.get(positionUser).al_historias.get(counter).url);
                videoView.setDataSource(al_stories_user.get(positionUser).al_historias.get(counter).url);
                videoView.setVisibility(View.VISIBLE);
                //videoView.start();
                videoView.play();
                bar.playSegment(al_stories_user.get(positionUser).al_historias.get(counter).duration);
                newTimer(al_stories_user.get(positionUser).al_historias.get(counter).duration);
                startmillis = System.currentTimeMillis();
            }
        }else{
            bar.setCompletedSegments(counter);
            Toast.makeText(context, "Completado", Toast.LENGTH_SHORT).show();
            videoView.mMediaPlayer.reset();
            Activity activity = (Activity) context;
            activity.finish();
            activity.overridePendingTransition(0,0);
        }
    }

    public void stopVideo(){
        bar.reset();
        videoView.stop();
    }






}
