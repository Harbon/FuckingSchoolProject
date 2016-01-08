package link.harbon.work.flipperbird;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                User user = new User();
                user.setName(name);
                user.setPassword(password);
                user.setEmail(email);
                user.setAvatar("null");
                user.setMoney(1000.0f);
                user.setScore(2000.0f);
                user.update(RegisterActivity.this);
                Intent intent  = new Intent();
                intent.putExtra("userLogin", user);
                intent.setClass(RegisterActivity.this, MainActivity.class);
                RegisterActivity.this.startActivity(intent);
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
