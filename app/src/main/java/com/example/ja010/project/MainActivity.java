package com.example.ja010.project;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    EditText ED_SEARCH;
    MyManageDB dbs;
    Button btnload;
    ListView lv;
    String vodid = "";
    YouTubePlayer ytp;
    String changString;
    YouTubePlayerView youTubeView;
    Task t;
    ArrayList<dataclass > list = new ArrayList<dataclass>();
    DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permi();
        ED_SEARCH = (EditText)findViewById(R.id.ed);
        lv = (ListView)findViewById(R.id.listview);
        adapter = new DataAdapter(this,list);
        lv.setAdapter(adapter);
        dbs = MyManageDB.getInstance(getApplicationContext());
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY,this);
        btnload = (Button)findViewById(R.id.load);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vodid = list.get(position).getURL();
                ytp.cueVideo(vodid);
                changString = list.get(position).getMUSIC_NAME();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setNegativeButton("아니요",null)
                        .setTitle("삭제하시겠습니까?")
                        .setMessage("삭제를 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name =list.get(position).getURL();
                        dbs.delete(name);
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,boolean wasRestored) {
        ytp = player;
        if (!wasRestored) {
            player.cueVideo("rAIXE6ilRQ0");
            player.play();
        }
    }//초기화 시
    @Override//초기화 실패시
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
    } // getdata
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
    public void clcl(View v){
        switch(v.getId()){
            case R.id.btnsearch: // search 후 바로 동영상 불러오기
                if (ED_SEARCH.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"찾을 데이터를 입력하세요! ",Toast.LENGTH_SHORT).show();
                    ED_SEARCH.setFocusable(true); // focusable
                    return;
                }
                t = new Task();
                t.execute();
                break;
            case R.id.btnurl: // clipboard 에 url 을 저장
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label","https://www.youtube.com/watch?v="+vodid);
                clipboardManager.setPrimaryClip(clipData);
                break;
            case R.id.btnstore: // list에 추가 && db에 저장
                if (ED_SEARCH.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"검색어를 입력하세요 ",Toast.LENGTH_SHORT).show();
                    ED_SEARCH.setFocusable(true); // focusable
                    return;
                }
                else {
                    list.add(new dataclass(vodid,changString));
                    dbs.insert(vodid,changString);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btnsort: //sort
                adapter.setNAMEASC();
                break;
            case R.id.btndownload: // download page로 직행 -> ransomeware! so, not apply
//                 Intent i = new Intent(MainActivity.this,Internet.class);
//                 startActivity(i);
//                 break;
            case R.id.load: /// 예외처리!!!!
                if(btnload.getText().toString().equals("LOAD")){
                    Cursor DBLIST =dbs.execSELECTStudent("Select * from music order by id");
                    DBLIST.moveToFirst();
                    String name;
                    String URL;
                    if(DBLIST.getCount() ==0){
                        Toast.makeText(getApplicationContext(),"NOT EXIST",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        do {
                            name = DBLIST.getString(1);
                            URL = DBLIST.getString(2);
                            list.add(new dataclass(name, URL));
                        }
                        while (DBLIST.moveToNext());
                        DBLIST.close();
                        adapter.notifyDataSetChanged();
                        btnload.setText("NONE");
                    }


                break;
                }}
    } // button event
    public JSONObject getUtube(){
        HttpGet httpGet = new HttpGet(
                "https://www.googleapis.com/youtube/v3/search?"
                        + "part=snippet&q=" +ED_SEARCH.getText().toString().trim().replaceAll(" ","")
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
    } // http 접속
    private void paringJsonData(JSONObject jsonObject) throws JSONException {
        JSONArray contacts = jsonObject.getJSONArray("items");
            JSONObject c = contacts.getJSONObject(0);
            String kind =  c.getJSONObject("id").getString("kind"); // 종류를 체크하여 playlist도 저장
            if(kind.equals("youtube#video")){
                vodid = c.getJSONObject("id").getString("videoId"); // 유튜브
            }else{
                vodid = c.getJSONObject("id").getString("playlistId"); // 유튜브
            }
            String title = c.getJSONObject("snippet").getString("title"); //유튜브 제목을 받아옵니다

        try {
            changString = new String(title.getBytes("8859_1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    } //web 통신
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
            ytp.cueVideo(vodid);
            super.onPostExecute(aVoid);
        }
    } // load youtube load
    private void permi(){ // 권한 설정
        int pinfo = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(pinfo == PackageManager.PERMISSION_GRANTED){
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,"권한설정좀...",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }
}
    // internet setting

