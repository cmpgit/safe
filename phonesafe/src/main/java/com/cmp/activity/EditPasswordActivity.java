package com.cmp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cmp.phonesafe.R;
import com.cmp.util.MD5Util;

/**
 * 作者：cmp on 2016/9/10 01:13
 * <p>
 * 手机防盗输入密码
 */
public class EditPasswordActivity extends Activity implements View.OnClickListener {
    private EditText mEdit;
    private Button mBtn;
    private SharedPreferences preferences;
    private TextView mToolbarTitleTv;
    private ImageButton mToolbarBackBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpw);
        initView();
    }

    private void initView() {
        mEdit = (EditText) findViewById(R.id.edit_pw);
        mBtn = (Button) findViewById(R.id.btn_true);
        mEdit.addTextChangedListener(mTextWatcher);
        //设置标题
        mToolbarTitleTv = (TextView) findViewById(R.id.toolbar_title_tv);
        mToolbarTitleTv.setText(R.string.activity_editpw_title);
        mToolbarBackBtn = (ImageButton) findViewById(R.id.toolbar_back_btn);
        mToolbarBackBtn.setOnClickListener(this);
    }

    //返回按钮
    public void btnBack(View v) {
        finish();
    }

    public void check(View v) {
        //提取设置好的密码
        preferences = getSharedPreferences("MyData", 2);
        String burglar = preferences.getString("burglar", "");
        if (MD5Util.md5(mEdit.getText().toString()).equals(burglar))// 判断密码是否一致
        {
            //跳转
            Intent intent = new Intent();
            intent.setClass(EditPasswordActivity.this, BurglarActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

        }

        //控制登录按钮
        @Override
        public void afterTextChanged(Editable s) {
            editStart = mEdit.getSelectionStart();
            editEnd = mEdit.getSelectionEnd();
            if (temp.length() > 5) {
                mBtn.setBackgroundResource(R.drawable.click_true);
                mBtn.setEnabled(true);
            } else {
                mBtn.setBackgroundResource(R.drawable.click_false);
                mBtn.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_btn:
                finish();
                break;
        }
    }
}
