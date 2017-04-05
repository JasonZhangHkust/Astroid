package com.gamecodeschool.astroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private GLSurfaceView asteroidsView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int bestLevel;
    private static TextView valueTV;
    private static simpleHandler handler = new simpleHandler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);
        asteroidsView = new AsteroidsView(this, resolution.x, resolution.y,handler);
        prefs = getSharedPreferences("HiScores",
                MODE_PRIVATE);
        editor = prefs.edit();
        bestLevel = prefs.getInt("best level", 0);

        setContentView(R.layout.activity_main);

        // Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        // Attach our surfaceview to our relative layout from our main layout.
        valueTV = new TextView(this);
        valueTV.setText("best levels: " + bestLevel);
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(asteroidsView, glParams);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        layout.addView(valueTV, lp1);

    }
    private static class simpleHandler extends Handler{
        private TextView textView;
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                if(textView != null){
                    textView.setText("best level " + msg.arg1);
                }
            }
        }
        public void attach(TextView view){
            this.textView = view;
        }
        public void detach(){
            this.textView = null;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        handler.detach();
        asteroidsView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.attach(valueTV);
        asteroidsView.onResume();

    }
}
