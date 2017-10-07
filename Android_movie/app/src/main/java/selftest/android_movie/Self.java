package selftest.android_movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Self extends AppCompatActivity {
    private Button movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self);
        movie=(Button)findViewById(R.id.movie);
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");//调用系统自带的播放器读取网络视频
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Log.v("URI:", uri.toString());
                intent.setDataAndType(uri, "video/mp4");
                startActivity(intent);
            }
        });
    }
}
