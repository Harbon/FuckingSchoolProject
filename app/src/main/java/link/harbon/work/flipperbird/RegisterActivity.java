package link.harbon.work.flipperbird;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by harbon on 16/1/3.
 */
public class RegisterActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mUserPasswordEditText;
    EditText mUserRePasswordEditText;
    EditText mUserEmailEditText;
    TextView mNotificationText;
    TextView mRegisterButton;
    RelativeLayout mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mUserPasswordEditText = (EditText) findViewById(R.id.userPasswordEditText);
        mUserRePasswordEditText = (EditText) findViewById(R.id.userRePasswordEditText);
        mUserEmailEditText = (EditText) findViewById(R.id.userEmailEditText);
        mNotificationText = (TextView) findViewById(R.id.notification);
        mRegisterButton = (TextView) findViewById(R.id.registerButton);
        mProgressBar = (RelativeLayout) findViewById(R.id.progressBarRela);
        mNotificationText.setTextColor(Color.RED);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mUserNameEditText.getText().toString();
                String password = mUserPasswordEditText.getText().toString();
                String repassword = mUserRePasswordEditText.getText().toString();
                String email = mUserEmailEditText.getText().toString();


                if (!isAvailable(name)) {
                    mNotificationText.setText("用户名非法");
                    return;
                }
                if (!isAvailable(password)) {
                    mNotificationText.setText("无效密码字符");
                    return;
                }
                if (!repassword.equals(password)) {
                    mNotificationText.setText("两次输入的密码不一致");
                    return;
                }
                Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
                Matcher m = p.matcher(email);
                boolean b = m.matches();
                if (!b) {
                    mNotificationText.setText("邮箱格式不正确");
                    return;
                }
                StringEntity stringEntity = null;
                try {
                    JSONObject jsonParams = new JSONObject().put("username", name).put("password", password).put("email", email);
                    stringEntity = new StringEntity(jsonParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.i("aaaaaa", "start register reaqueeeeee");
                API.asyncHttpClient.post(RegisterActivity.this, API.BASE_URL+API.USER_REGISTER, stringEntity, "application/json",new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("aaaaaa", "resopnse register:"+response.toString());
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                JSONObject userJson = response.getJSONObject("user");
                                String userName = userJson.getString("username");
                                float money = (float) userJson.getDouble("money");
                                int score = userJson.getInt("score");
                                String userId = userJson.getString("_id");
                                String email = userJson.getString("email");
                                Intent intent  = new Intent();
                                User user = new User();
                                user.setName(userName);
                                user.setMoney(money);
                                user.setScore(score);
                                user.setEmail(email);
                                user.setUserId(userId);
                                intent.putExtra("userLogin", user);
                                intent.setClass(RegisterActivity.this, MainActivity.class);
                                RegisterActivity.this.startActivity(intent);
                                RegisterActivity.this.finish();
                            }else {
                                mNotificationText.setTextColor(Color.RED);
                                mProgressBar.setVisibility(View.GONE);
                                mNotificationText.setText("用户名已存在");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//                User user = new User();
//                user.setName(name);
//                user.setPassword(password);
//                user.setEmail(email);
//                user.setAvatar("null");
//                user.setMoney(1000.0f);
//                user.setScore(2000.0f);
//                user.update(RegisterActivity.this);
//                Intent intent  = new Intent();
//                intent.putExtra("userLogin", user);
//                intent.setClass(RegisterActivity.this, MainActivity.class);
//                RegisterActivity.this.startActivity(intent);
            }
        });
    }

    private boolean isAvailable(String property) {
        if (property == null) {
            return false;
        }
        if (property.equals("")) {
            return false;
        }
        return true;
    }
}
