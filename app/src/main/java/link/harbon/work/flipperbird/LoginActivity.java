package link.harbon.work.flipperbird;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by harbon on 16/1/3.
 */
public class LoginActivity extends AppCompatActivity {

    EditText mUserNameEditText;
    EditText mUserPasswordEditText;
    TextView mNotificationText;
    TextView mLoginButton;
    TextView mRegisterButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserNameEditText = (EditText) findViewById(R.id.userNameEditText);
        mUserPasswordEditText = (EditText) findViewById(R.id.userPasswordEditText);
        mNotificationText = (TextView) findViewById(R.id.notification);
        mLoginButton = (TextView) findViewById(R.id.loginButton);
        mRegisterButton = (TextView) findViewById(R.id.registerButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String password = mUserPasswordEditText.getText().toString();
                User user = User.getUserByName(userName, LoginActivity.this);
                if (user == null) {
                    mNotificationText.setTextColor(Color.RED);
                    mNotificationText.setText("用户名不存在");
                }else if (!password.equals(user.getPassword())) {
                    mNotificationText.setTextColor(Color.RED);
                    mNotificationText.setText("密码错误");
                    return;
                }else {
                    Intent intent  = new Intent();
                    intent.putExtra("userLogin", user);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                }

            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });



    }
}
