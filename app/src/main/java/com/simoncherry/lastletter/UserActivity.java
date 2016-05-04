package com.simoncherry.lastletter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import android.support.v7.app.AlertDialog;

import org.apache.http.params.HttpParams;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserActivity extends AppCompatActivity {
    private LetterUpLoadTask mUploadTask = null;

    private TextView tv_letter;
    private EditText et_letter;

    private final static int MSG_SHOW_TEXT = 0x123;
    private final static int MSG_SHOW_JSON = 0x456;
    //private String TOURL = "http://lastletter.vicp.net:8090/servlet/GetPostTestDemo";
    //private String TOURL = "http://lastletter.vicp.net:8090/servlet/DataBaseUtil";
    private String TOURL = "http://192.168.1.102:8090/servlet/DataBaseUtil";

    String response;
    String user_name;
    boolean isEditable = false;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == MSG_SHOW_JSON) {
                Gson gson = new Gson();
                UserBean bean = gson.fromJson(response, UserBean.class);
                tv_letter.setText(bean.getUserLetter());
            }else if(msg.what == MSG_SHOW_TEXT){
                tv_letter.setText(response);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user_name = intent.getStringExtra("user");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                new Thread(){
//                    @Override
//                    public void run()
//                    {
//                        response = NetWorkUtil.sendGet(TOURL,"str=mysql");
//                        if(response.equals("") == false){
//                            handler.sendEmptyMessage(MSG_SHOW_JSON);
//                        }else{
//                            response = "Illegal operation on empty result set.";
//                            handler.sendEmptyMessage(MSG_SHOW_TEXT);
//                        }
//                    }
//                }.start();
                if(isEditable == false){
                    String text = tv_letter.getText().toString();
                    et_letter.setText(text);
                    //et_letter.setFocusable(true);
                    et_letter.setInputType(InputType.TYPE_CLASS_TEXT);
                    et_letter.setSingleLine(false);

                    tv_letter.setVisibility(View.INVISIBLE);
                    et_letter.setVisibility(View.VISIBLE);
                    isEditable = true;
                }else{

                    new AlertDialog.Builder(UserActivity.this)
                            .setTitle("Dear")
                            .setMessage("Are you sure to save the editing results?")
                            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    et_letter.setInputType(InputType.TYPE_NULL);

                                    tv_letter.setVisibility(View.VISIBLE);
                                    et_letter.setVisibility(View.INVISIBLE);
                                    isEditable = false;
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String text = et_letter.getText().toString();
                                    tv_letter.setText(text);
                                    et_letter.setInputType(InputType.TYPE_NULL);

                                    tv_letter.setVisibility(View.VISIBLE);
                                    et_letter.setVisibility(View.INVISIBLE);
                                    isEditable = false;
                                    dialog.dismiss();

                                    mUploadTask = new LetterUpLoadTask(user_name, text);
                                    mUploadTask.execute((Void) null);
                                }
                            })
                            .create()
                            .show();
                }

            }
        });

        tv_letter = (TextView) findViewById(R.id.tv_letter);
        et_letter = (EditText) findViewById(R.id.et_letter);

        new Thread(){
            @Override
            public void run()
            {
                //response = NetWorkUtil.sendGet(TOURL,"str=mysql");
                String str = "str=query:" + user_name;
                response = NetWorkUtil.sendGet(TOURL, str);
                if(response.equals("") == false){
                    handler.sendEmptyMessage(MSG_SHOW_JSON);
                }else{
                    response = "Illegal operation on empty result set.";
                    handler.sendEmptyMessage(MSG_SHOW_TEXT);
                }
            }
        }.start();
    }

    public class LetterUpLoadTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mLetterText;

        LetterUpLoadTask(String name, String text) {
            mUserName = name;
            mLetterText = text;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String response = "";
            String urlPath = "http://192.168.1.102:8090/servlet/LetterUtil";
            URL url;

            try {
                url = new URL(urlPath);

                UserBean bean = new UserBean();
                bean.setUserName(mUserName);
                bean.setUserLetter(mLetterText);
                Gson gson = new Gson();
                String content = gson.toJson(bean);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("ser-Agent", "Fiddler");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(content.getBytes());
                os.close();

                int code = conn.getResponseCode();
                if (code == 200) {
                    return true;
                }

//                Log.e("sendResponse: ", response);
//                if(response.trim().equals("success")){
//                    return true;
//                }

            } catch (Exception e) {
                return false;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mUploadTask = null;
            //TODO
            Log.e("modify: ", String.valueOf(success));
            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mUploadTask = null;
        }
    }
}
