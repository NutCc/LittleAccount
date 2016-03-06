package com.moinut.littleaccount;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    public static final int RESULT_CHANGED = 1;
    public static final int RESULT_UNCHANGED = 0;
    private EditText edName, edMoney, edTime;
    private Button okButton;
    private CardDatabaseHelper mDBHelper;
    private SQLiteDatabase db;
    private Toolbar mToolbar;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setResult(RESULT_UNCHANGED);
        id  = getIntent().getStringExtra("id");
        mDBHelper = new CardDatabaseHelper(this, "little_account.db", null, 1);
        db = mDBHelper.getWritableDatabase();
        initView();
        if (id != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM CARD WHERE id = ?", new String[] {id});
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String money = cursor.getString(cursor.getColumnIndex("money"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    edName.setText(name);
                    edMoney.setText(money);
                    edTime.setText(time);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (id != null) {
            mToolbar.setTitle("修改账单");
        } else {
            mToolbar.setTitle("创建账单");
        }
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edMoney = (EditText) findViewById(R.id.et_money);
        edName = (EditText) findViewById(R.id.et_name);
        edTime = (EditText) findViewById(R.id.et_time);
        okButton = (Button) findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, time;
                double money = 0;
                name = edName.getText().toString();
                try {
                    money = Double.parseDouble(edMoney.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(EditActivity.this, "金额必须输入阿拉伯数字", Toast.LENGTH_SHORT).show();
                }
                time = edTime.getText().toString();
                if (!name.isEmpty() && money != 0 && !time.isEmpty()) {
                    if (id != null) {
                        db.execSQL("UPDATE CARD SET name = ?, money = ?, time = ? WHERE id = ?", new String[]{name, money + "", time, id});
                    } else {
                        db.execSQL("INSERT INTO CARD(name, money, time) VALUES (?, ? ,?)", new String[]{name, money + "", time});
                    }
                    setResult(RESULT_CHANGED);
                    finish();
                } else {
                    Toast.makeText(EditActivity.this, "不能为空且金额不得为零", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
