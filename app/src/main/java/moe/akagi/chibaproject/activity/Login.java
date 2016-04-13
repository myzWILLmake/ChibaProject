package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.datatype.User;
import moe.akagi.chibaproject.network.API;

/**
 * Created by yunze on 12/5/15.
 */
public class Login extends Activity {

    private int backPressed = 0;

    TextInputLayout phoneInputLayout;
    TextInputLayout passwordInputLayout;
    Button submit;

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
                LoginTask task = new LoginTask();
                task.execute(sphone, spassword);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
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

    private class LoginTask extends AsyncTask<String, Integer, Integer> {

        public static final int LOGIN_SUCC = 100;
        public static final int LOGIN_FAIL_LOGGED = 101;
        public static final int LOGIN_FAIL_WRONG_ACCO = 102;
        public static final int LOGIN_FAIL_WRONG_PASS = 103;
        public static final int LOGIN_FAIL_SOMETHING_WRONG = 110;


        @Override
        protected void onPreExecute() {
            Toast.makeText(Login.this, "正在登陆...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String phone = params[0];
            String password = params[1];
            Object res = API.getUserByAuth(phone, password);
            if (res instanceof User) {
                User user = (User)res;
                Log.v("login", user.get_id());
                Log.v("login", user.getNickname());
                Log.v("login", user.getPhone());
                MyApplication.user = user;
                SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
                editor.putBoolean("logged", true);
                editor.putString("phone", phone);
                editor.putString("password", password);
                editor.apply();
                return LOGIN_SUCC;
            } else if (res instanceof Integer) {
                return (Integer)res;
            } else {
                Log.v("Login", "Something wrong! return null in LoginTask.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
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
                case LOGIN_FAIL_WRONG_ACCO:
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
}

