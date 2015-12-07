package moe.akagi.chibaproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import moe.akagi.chibaproject.MyApplication;
import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.database.API;
import moe.akagi.chibaproject.datatype.User;

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
                submitLogin(sphone, spassword);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void submitLogin(String phone, String password) {
        User user = API.getUserByAuth(phone, password);
        if (user == null) {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        } else {
            MyApplication.user = user;
            SharedPreferences.Editor editor = getSharedPreferences("AppData", MODE_PRIVATE).edit();
            editor.putBoolean("logged", true);
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.apply();
            Intent intent = new Intent();
            intent.putExtra("login_succeed", true);
            setResult(RESULT_OK, intent);
            finish();
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

