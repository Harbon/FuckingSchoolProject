package link.harbon.work.flipperbird;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by harbon on 16/1/3.
 */
public class LoginActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mUserPasswordEditText;
    TextView mNotificationText;
    TextView mLoginButton;
    TextView mRegisterButton;
    RelativeLayout mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mUserPasswordEditText = (EditText) findViewById(R.id.userPasswordEditText);
        mNotificationText = (TextView) findViewById(R.id.notification);
        mLoginButton = (TextView) findViewById(R.id.loginButton);
        mRegisterButton = (TextView) findViewById(R.id.registerButton);
        mProgressBar = (RelativeLayout) findViewById(R.id.progressBarRela);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String password = mUserPasswordEditText.getText().toString();
                StringEntity stringEntity  = null;
                mProgressBar.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonParams = new JSONObject().put("username", userName).put("password", password);
                    stringEntity = new StringEntity(jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.i("aaaaaa", "start reaqueeeeee");
                API.asyncHttpClient.post(LoginActivity.this, API.BASE_URL+API.USER_LOGIN, stringEntity, "application/json",new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("aaaaaa", "resopnse login:"+response.toString());
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                JSONObject userJson = response.getJSONObject("user");
                                String userName = userJson.getString("username");
                                float money = (float) userJson.getDouble("money");
                                int score = userJson.getInt("score");
                                Log.i("aaaaaa", " user login socre:"+score);
                                String userId = userJson.getString("_id");
                                String email = userJson.getString("email");
                                User user = new User();
                                user.setName(userName);
                                user.setMoney(money);
                                user.setScore(score);
                                user.setEmail(email);
                                user.setUserId(userId);
                                Intent intent  = new Intent();
                                intent.putExtra("userLogin", user);
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }else {
                                mNotificationText.setTextColor(Color.RED);
                                mProgressBar.setVisibility(View.GONE);
                                mNotificationText.setText("用户名不存在或密码错误");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                User user = User.getUserByName(userName, LoginActivity.this);
//                if (user == null) {
//                    mNotificationText.setTextColor(Color.RED);
//                    mNotificationText.setText("用户名不存在");
//                }else if (!password.equals(user.getPassword())) {
//                    mNotificationText.setTextColor(Color.RED);
//                    mNotificationText.setText("密码错误");
//                    return;
//                }else {
//                    Intent intent  = new Intent();
//                    intent.putExtra("userLogin", user);
//                    intent.setClass(LoginActivity.this, MainActivity.class);
//                    LoginActivity.this.startActivity(intent);
//                }

            }
        });


        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });



    }
}
