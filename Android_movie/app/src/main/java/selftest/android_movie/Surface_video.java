package selftest.android_movie;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;

public class Surface_video extends AppCompatActivity implements View.OnClickListener{
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private Button Start;
    private Button Pause;
    private Button Stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_video);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        //视频链接可能已失效
        String uri="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        player=new MediaPlayer();
        Start=(Button)findViewById(R.id.Start);
        Pause=(Button)findViewById(R.id.Pause);
        Stop=(Button)findViewById(R.id.Stop);
        Start.setOnClickListener(this);
        Pause.setOnClickListener(this);
        Stop.setOnClickListener(this);
        try {
            player.setDataSource(this, Uri.parse(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder=surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Start:
                try {
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Pause:
                player.pause();
                break;
            case R.id.Stop:
                player.stop();
                break;
        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}