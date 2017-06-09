package com.example.ja010.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends  YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    Button BNT_STORE,BNT_RANDOM,BNT_DOWNLOAD,BNT_SEARCH;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    EditText ED_SEARCH;
    ListView lv;
    String vodid = "";

    Task t;
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
                t = new Task();
                t.execute();


                adapter.notifyDataSetChanged();
                break;
            case R.id.btnstore: // db에 저장
                break;
            case R.id.btnrandom:
                break;
            case R.id.btndownload:
                Intent i = new Intent(MainActivity.this,Internet.class);
                startActivity(i);


                break;

        }

    }
    public JSONObject getUtube(){
        HttpGet httpGet = new HttpGet(
                "https://www.googleapis.com/youtube/v3/search?"
                        + "part=snippet&q=" +ED_SEARCH.getText().toString()
                        + "&key="+DeveloperKey.DEVELOPER_KEY+"&maxResults=50");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
    private void paringJsonData(JSONObject jsonObject) throws JSONException {
        JSONArray contacts = jsonObject.getJSONArray("items");
            JSONObject c = contacts.getJSONObject(1);
            String kind =  c.getJSONObject("id").getString("kind"); // 종류를 체크하여 playlist도 저장
            if(kind.equals("youtube#video")){
                vodid = c.getJSONObject("id").getString("videoId"); // 유튜브
            }else{
                vodid = c.getJSONObject("id").getString("playlistId"); // 유튜브
            }

            String title = c.getJSONObject("snippet").getString("title"); //유튜브 제목을 받아옵니다
        }
    class Task extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                paringJsonData(getUtube());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"id: "+vodid,Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
    }

}


    // internet setting

