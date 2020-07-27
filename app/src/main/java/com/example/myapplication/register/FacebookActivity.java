package com.example.myapplication.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Constants;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SplashActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FacebookActivity extends AppCompatActivity implements RegisterDialog.RegisterDialogListener {
    private CallbackManager callbackManager;
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private JSONObject userData;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("액티비티 진입", "Facebook activity");
        setContentView(R.layout.activity_facebook);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null && !accessToken.isExpired())
        {   Log.d("액티비티 facebook", "로그인된 상태");
            launchMainActivity(accessToken.getUserId());}

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setPermissions("email");
        loginButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        id = user.optString("id");
                        Log.d("facebook액티비티", "id = "+id);

                        OkHttpClient client = new OkHttpClient();
                        RequestBody body = new FormBody.Builder()
                                .add("body", "ToGetInfo")
                                .build();
                        Request request_ = new Request.Builder()
                                .url(String.format("%s/api/recorder/%s", Constants.SERVER_IP, id))
                                .post(body)
                                .build();
                        Log.d("Facebook액티비티", request_.toString());

                        client.newCall(request_).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.d("Facebook액티비티", "failed");
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast.makeText(FacebookActivity.this, "서버 연결이 불안정 합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                call.cancel();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException{
                                final String jsonString = response.body().string();
                                Log.d("main액티비티로", jsonString.toString());

                                if(jsonString.contains("user not found")){ //isMember 대신 사용함
                                    launchRegisterDialog();
                                }
                                else {launchMainActivity(id);}
                            }

                        });
                    }
                }).executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                finish();
            }
        });

    }

    public void launchRegisterDialog() {
        RegisterDialog registerDialog = new RegisterDialog();
        registerDialog.show(getSupportFragmentManager(), "Register");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void register(String nickname, String name) throws JSONException {
        //Iterator<String> keys = userData.keys();
        JSONObject postBody = new JSONObject();
//        while (keys.hasNext()) {
//            String key = keys.next();
//            // insert keys except "member"
//            if (userData.get(key) instanceof String)
//                postBody.put("facebookID", (String) userData.get(key));
//        }
        if(id != null) postBody.put("facebookID", id);
        else {
            Log.d("가입 액티비티", "id null");
            finish();
        }
        postBody.put("name", name);
        postBody.put("nickname", nickname);
        Log.d("가입 액티비티", postBody.toString());

        RequestBody body = RequestBody.create(postBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(String.format("%s/api/register", Constants.SERVER_IP))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                final String jsonString = response.body().string();
//                JSONObject data = null;
//                String id = null;
//                try {
//                    data = new JSONObject(jsonString);
//                    id = data.getString("id");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                launchMainActivity(id);
            }
        });
    }

    public void launchMainActivity(String id_) {
//        Intent intent = new Intent(FacebookActivity.this, MainActivity.class);
        if(id_ == null) Log.d("main액티비티로", "id_ null");
        Intent intent = new Intent(FacebookActivity.this, SplashActivity.class);
        intent.putExtra("USER_ID", id_);
        startActivity(intent);
        finish();
    }
}