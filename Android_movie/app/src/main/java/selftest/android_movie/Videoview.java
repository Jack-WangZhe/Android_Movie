package selftest.android_movie;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videoview extends AppCompatActivity implements View.OnClickListener{
    private VideoView video;
    private Button Start;
    private Button Pause;
    private Button kuaijin;
    private Button kuaitui;
    Uri uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        video=(VideoView)findViewById(R.id.video);
        Start=(Button)findViewById(R.id.Start);
        Pause=(Button)findViewById(R.id.Pause);
        kuaijin=(Button)findViewById(R.id.kuaijin);
        kuaitui=(Button)findViewById(R.id.kuaitui);
        Start.setOnClickListener(this);
        Pause.setOnClickListener(this);
        kuaijin.setOnClickListener(this);
        kuaitui.setOnClickListener(this);
        //uri=Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/video.mp4");
        video.setVideoURI(uri);//调用方法设置视频路径
        video.setMediaController(new MediaController(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Start:
                video.start();
                break;
            case R.id.Pause:
                video.pause();
                break;
            case R.id.kuaijin:
                if(video.canSeekForward())
                {
                    int pos= video.getCurrentPosition();
                    video.seekTo(pos+10000);
                }
                break;
            case R.id.kuaitui:
                if (video.canSeekBackward())
                {
                    int pos=video.getCurrentPosition();
                    if (pos<10000)
                    {
                        pos=0;
                    }
                    else
                        pos-=10000;
                    video.seekTo(pos);
                }
                break;
        }
    }
}
