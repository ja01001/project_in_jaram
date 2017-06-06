package com.example.ja010.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple YouTube Android API demo application which shows how to create a simple application that
 * displays a YouTube Video in a {@link YouTubePlayerView}.
 * <p>
 * Note, to use a {@link YouTubePlayerView}, your activity must extend {@link YouTubeBaseActivity}.
 */

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.util.ArrayList;

public class MainActivity extends  YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    Button BNT_STORE,BNT_RANDOM,BNT_DOWNLOAD,BNT_SEARCH;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    EditText ED_SEARCH;
    ListView lv;
    ArrayList<dataclass > list = new ArrayList<dataclass>();
    DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ED_SEARCH = (EditText)findViewById(R.id.ed);
        lv = (ListView)findViewById(R.id.listview);
        adapter = new DataAdapter(this,list);
        lv.setAdapter(adapter);
        list.add(new dataclass("naver","aaa"));
        list.add(new dataclass("11111","112323"));
        adapter.notifyDataSetChanged();
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);


    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo("wKJ9KzGQq0w");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DeveloperKey.DEVELOPER_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    public void clcl(View v){
        switch(v.getId()){
            case R.id.btnsearch:
                lv.deferNotifyDataSetChanged();
                break;
            case R.id.btnstore:

                break;
            case R.id.btnrandom:

                break;
            case R.id.btndownload:
                Intent i = new Intent(MainActivity.this,Internet.class);
                startActivity(i);
                finish();

                break;

        }

    }
    // internet setting

}
