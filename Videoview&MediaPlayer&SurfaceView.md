# Android视频播放
## 自带的播放器
### Intent跳转的Intent.ACTION_VIEW
### 部分代码:
	Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");//调用系统自带的播放器读取网络视频
	Intent intent = new Intent(Intent.ACTION_VIEW);
	Log.v("URI:::::::::", uri.toString());
	intent.setDataAndType(uri, "video/mp4");  
	startActivity(intent);
## 使用VideoView实现播放效果
#### 部分代码:
	uri=Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
	//uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/video.mp4");//用本地路径
    video.setVideoURI(uri);//调用方法设置视频路径
    video.setMediaController(new MediaController(this));
	video.start();
## 使用MediaPlayer+Surface实现播放效果
### 一般来说，UI的刷新都需要在UI线程中完成，但是，surfaceview可以在非UI线程中完成刷新。这样以来就很方便了，比如在线播放，就不需要自己去写handler来实现两个线程之间的通信了，直接可以在非UI线程中播放视频。
### 整体使用步骤:
* 调用player.setDataSource（）方法设置要播放的资源，可以是文件、文件路径、或者URL。
* 调用MediaPlayer.setDisplay(holder)设置surfaceHolder，surfaceHolder可以通过surfaceview的getHolder()方法获得。
* 调用MediaPlayer.prepare()来准备。
* 调用MediaPlayer.start()来播放视频。

#### 在第二步之前需要确保surfaceHolder已经准备好了。因此需要给surfaceHolder设置一个callback，调用addCallback()方法。Callback 有三个回调函数，如下：
	SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }
#### surfaceCreated()会在SurfaceHolder被创建的时候回调，在这里可以做一些初始化的操作，surfaceDestroyed()会在SurfaceHolder被销毁的时候回调，在这里可以做一些释放资源的操作，防止内存泄漏。一般，会在surfaceCreated中给MediaPlayer设置surfaceHolder。
### Activity部分代码如下:
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
## 视频录制
### 利用安卓自带的MediaRecorder来录制视频，并制定视频保存路径，并且可以通过Camera来播放录制的视频
#### 视频质量问题:
	mRecorder.setVideoSize(640, 480); 
	mRecorder.setVideoFrameRate(30); 
	mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
* mRecorder.setVideoSize(640, 480);<br>
这是设置视频的分辨率，在手机上看不出什么区别，可能在大屏幕上投影或者电脑上观看的时候就有差距了。
* mRecorder.setVideoFrameRate(30);<br>
这是设置视频录制的帧率，即1秒钟30帧。
* mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);<br>
这个直接影响到视频录制的大小，这个设置的越大，视频越清晰
#### 视频录制时预览界面角度问题
	camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); 
	if (camera != null) { 
	camera.setDisplayOrientation(90); 
	camera.unlock(); 
	mRecorder.setCamera(camera); 
	}
必须得设置Camera的预览角度才行，也就是这行代码：camera.setDisplayOrientation(90);这样进行测试，摄像机的预览角度终于是竖屏的了，并且保存的文件播放时也是竖屏的。

### XML文件:
	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	
	    <SurfaceView
	        android:id="@+id/surfaceview"
	        android:layout_width="match_parent"
	        android:layout_marginBottom="60dp"
	        android:layout_height="match_parent" />
	
	    <ImageView
	        android:id="@+id/imageview"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:layout_marginBottom="60dp"
	        android:src="@mipmap/ic_launcher"/>
	
	    <Button
	        android:id="@+id/btnStartStop"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerHorizontal="true"
	        android:layout_alignParentBottom="true"
	        android:text="Start"/>
	
	    <Button
	        android:id="@+id/btnPlayVideo"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_alignParentBottom="true"
	        android:layout_toRightOf="@id/btnStartStop"
	        android:text="Play"
	        android:layout_marginLeft="20dp"/>
	
	    <TextView
	        android:id="@+id/text"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:textSize="25sp"
	        android:text="0"
	        android:layout_alignParentBottom="true"
	        android:layout_marginBottom="12dp"
	        android:layout_marginLeft="20dp"/>
	
	</RelativeLayout>
### Activity文件:
	package selftest.android_movie;
	
	import android.hardware.Camera;
	import android.media.AudioManager;
	import android.media.MediaPlayer;
	import android.media.MediaRecorder;
	import android.net.Uri;
	import android.os.Environment;
	import android.support.v7.app.AppCompatActivity;
	import android.os.Bundle;
	import android.util.Log;
	import android.view.SurfaceHolder;
	import android.view.SurfaceView;
	import android.view.View;
	import android.widget.Button;
	import android.widget.ImageView;
	import android.widget.TextView;
	
	import java.io.File;
	import java.util.Calendar;
	
	public class Record_movie extends AppCompatActivity implements SurfaceHolder.Callback{
	
	    private static final String TAG = "MainActivity";
	    private SurfaceView mSurfaceview;
	    private Button mBtnStartStop;
	    private Button mBtnPlay;
	    private boolean mStartedFlg = false;//是否正在录像
	    private boolean mIsPlay = false;//是否正在播放录像
	    private MediaRecorder mRecorder;
	    private SurfaceHolder mSurfaceHolder;
	    private ImageView mImageView;
	    private Camera camera;
	    private MediaPlayer mediaPlayer;
	    private String path;
	    private TextView textView;
	    private int text = 0;
	
	    private android.os.Handler handler = new android.os.Handler();
	    private Runnable runnable = new Runnable() {
	        @Override
	        public void run() {
	            text++;
	            textView.setText(text+"");
	            handler.postDelayed(this,1000);
	        }
	    };
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_record_movie);
	        mSurfaceview = (SurfaceView) findViewById(R.id.surfaceview);
	        mImageView = (ImageView) findViewById(R.id.imageview);
	        mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
	        mBtnPlay = (Button) findViewById(R.id.btnPlayVideo);
	        textView = (TextView)findViewById(R.id.text);
	        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                if (mIsPlay) {
	                    if (mediaPlayer != null) {
	                        mIsPlay = false;
	                        mediaPlayer.stop();
	                        mediaPlayer.reset();
	                        mediaPlayer.release();
	                        mediaPlayer = null;
	                    }
	                }
	                if (!mStartedFlg) {
	                    handler.postDelayed(runnable,1000);
	                    mImageView.setVisibility(View.GONE);
	                    if (mRecorder == null) {
	                        mRecorder = new MediaRecorder();
	                    }
	
	                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
	                    if (camera != null) {
	                        camera.setDisplayOrientation(90);
	                        camera.unlock();
	                        mRecorder.setCamera(camera);
	                    }
	
	                    try {
	                        // 这两项需要放在setOutputFormat之前
	                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	
	                        // Set output file format
	                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	
	                        // 这两项需要放在setOutputFormat之后
	                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
	
	                        mRecorder.setVideoSize(640, 480);
	                        mRecorder.setVideoFrameRate(30);
	                        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
	                        mRecorder.setOrientationHint(90);
	                        //设置记录会话的最大持续时间（毫秒）
	                        mRecorder.setMaxDuration(30 * 1000);
	                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
	
	                        path = getSDPath();
	                        if (path != null) {
	                            File dir = new File(path + "/recordtest");
	                            if (!dir.exists()) {
	                                dir.mkdir();
	                            }
	                            path = dir + "/" + getDate() + ".mp4";
	                            mRecorder.setOutputFile(path);
	                            mRecorder.prepare();
	                            mRecorder.start();
	                            mStartedFlg = true;
	                            mBtnStartStop.setText("Stop");
	                        }
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                } else {
	                    //stop
	                    if (mStartedFlg) {
	                        try {
	                            handler.removeCallbacks(runnable);
	                            mRecorder.stop();
	                            mRecorder.reset();
	                            mRecorder.release();
	                            mRecorder = null;
	                            mBtnStartStop.setText("Start");
	                            if (camera != null) {
	                                camera.release();
	                                camera = null;
	                            }
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                    mStartedFlg = false;
	                }
	            }
	        });
	
	        mBtnPlay.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                mIsPlay = true;
	                mImageView.setVisibility(View.GONE);
	                if (mediaPlayer == null) {
	                    mediaPlayer = new MediaPlayer();
	                }
	                mediaPlayer.reset();
	                Uri uri = Uri.parse(path);
	                mediaPlayer = MediaPlayer.create(Record_movie.this, uri);
	                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	                mediaPlayer.setDisplay(mSurfaceHolder);
	                try{
	                    mediaPlayer.prepare();
	                }catch (Exception e){
	                    e.printStackTrace();
	                }
	                mediaPlayer.start();
	            }
	        });
	
	        SurfaceHolder holder = mSurfaceview.getHolder();
	        holder.addCallback(this);
	        // setType必须设置，要不出错.
	        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }
	    @Override
	    protected void onResume() {
	        super.onResume();
	        if (!mStartedFlg) {
	            mImageView.setVisibility(View.VISIBLE);
	        }
	    }
	
	    /**
	     * 获取系统时间
	     *
	     * @return
	     */
	    public static String getDate() {
	        Calendar ca = Calendar.getInstance();
	        int year = ca.get(Calendar.YEAR);           // 获取年份
	        int month = ca.get(Calendar.MONTH);         // 获取月份
	        int day = ca.get(Calendar.DATE);            // 获取日
	        int minute = ca.get(Calendar.MINUTE);       // 分
	        int hour = ca.get(Calendar.HOUR);           // 小时
	        int second = ca.get(Calendar.SECOND);       // 秒
	
	        String date = "" + year + (month + 1) + day + hour + minute + second;
	        Log.d(TAG, "date:" + date);
	
	        return date;
	    }
	
	    /**
	     * 获取SD path
	     *
	     * @return
	     */
	    public String getSDPath() {
	        File sdDir = null;
	        boolean sdCardExist = Environment.getExternalStorageState()
	                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
	        if (sdCardExist) {
	            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
	            return sdDir.toString();
	        }
	
	        return null;
	    }
	
	    @Override
	    public void surfaceCreated(SurfaceHolder surfaceHolder) {
	        mSurfaceHolder = surfaceHolder;
	    }
	
	    @Override
	    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
	        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
	        mSurfaceHolder = surfaceHolder;
	    }
	
	    @Override
	    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
	        mSurfaceview = null;
	        mSurfaceHolder = null;
	        handler.removeCallbacks(runnable);
	        if (mRecorder != null) {
	            mRecorder.release();
	            mRecorder = null;
	            Log.d(TAG, "surfaceDestroyed release mRecorder");
	        }
	        if (camera != null) {
	            camera.release();
	            camera = null;
	        }
	        if (mediaPlayer != null){
	            mediaPlayer.release();
	            mediaPlayer = null;
	        }
	    }
	}
### Manifest文件中加入权限:
	<uses-feature android:name="android.hardware.camera"/>
    <uses-feature android:name="android.hardware.camera.autofocus"/>
    <uses-permission android:name="android.permission.CAMERA" ></uses-permission>
    <uses-permission android:name="android.permission.RECORD_AUDIO" ></uses-permission>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.INTERNET" />