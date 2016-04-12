package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.User;
import moe.akagi.chibaproject.network.API;
import moe.akagi.chibaproject.network.RetrieveData.DataListener;
import moe.akagi.chibaproject.network.RetrieveData.Manager;
//import moe.akagi.chibaproject.database.API;


/**
 * Created by yunze on 12/5/15.
 */
public class Login extends Activity {

    private int backPressed = 0;

    TextInputLayout phoneInputLayout;
    TextInputLayout passwordInputLayout;
    Button submit;

    Handler handler = new LoginHandler();

    public class LoginHandler extends Handler {

        public static final int LOGIN_SUCC = 100;
        public static final int LOGIN_FAIL_LOGGED = 101;
        public static final int LOGIN_FAIL_WRONG_PASS = 102;
        public static final int LOGIN_FAIL_SOMETHING_WRONG = 110;


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCC:
                    Toast.makeText(Login.this, "登录成功!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("login_succeed", true);
                    setResult(RESULT_OK, intent);
                    ActivityCollector.removeActivity(Login.this);
                    break;
                case LOGIN_FAIL_LOGGED:
                    Toast.makeText(Login.this, "已经登陆了啊?", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAIL_WRONG_PASS:
                    Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAIL_SOMETHING_WRONG:
                    Toast.makeText(Login.this, "有些不对劲!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(Login.this, "返回了未知的状态", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);

        setContentView(R.layout.login);
        phoneInputLayout = (TextInputLayout) findViewById(R.id.login_phone);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.login_password);
        submit = (Button) findViewById(R.id.login_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sphone = phoneInputLayout.getEditText().getText().toString();
                String spassword = passwordInputLayout.getEditText().getText().toString();
                submitLogin(sphone, spassword);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void submitLogin(final String phone, final String password) {
        Toast.makeText(this, "正在登陆...", Toast.LENGTH_SHORT).show();
        Manager manager = new Manager();
        manager.addDataListener(new DataListener() {
            @Override
            public void onDataReady(Object data) {
                handleLogin(data, phone, password);
            }
        });
        API.getUserByAuth(manager, phone, password);
    }

    private void handleLogin(Object data, String phone, String password) {
        if (data instanceof User) {
            User user = (User)data;
            Log.v("login", user.get_id());
            Log.v("login", user.getNickname());
            Log.v("login", user.getPhone());
            MyApplication.user = user;
            SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
            editor.putBoolean("logged", true);
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.apply();
            Message msg = new Message();
            msg.what = LoginHandler.LOGIN_SUCC;
            handler.sendMessage(msg);
        } else {
            int state = (Integer)data;
            Message msg = new Message();
            msg.what = state;
            handler.sendMessage(msg);
        }
    }


    @Override
    public void onBackPressed() {
        switch (backPressed) {
            case 0:
                Toast.makeText(Login.this, "再次按下返回键退出应用", Toast.LENGTH_SHORT).show();
                backPressed++;
                break;
            case 1:
                ActivityCollector.finishAll();
        }
    }
}

