package com.moinut.littleaccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class  MainActivity extends AppCompatActivity {

    public static final int REQUSET_CREATE = 0;

    private CardDatabaseHelper mDBHelper;
    private SQLiteDatabase db;
    private List<Card> mDatas = new ArrayList<>();
    private CardAdapter mAdapter;

    //这里是item的数据
    //private String[] date = {"包子","辣条","红烧狮子头"};// 我就想知道，怎么全是吃的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new CardDatabaseHelper(this, "little_account.db", null, 1);
        db = mDBHelper.getWritableDatabase();
        initDatas(); // 初始化数据
        initView();

    }

    private void initView() {

        mAdapter = new CardAdapter(mDatas, this);

        ListView listView = (ListView) findViewById(R.id.list_view); // 找到list_view
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id", mDatas.get(position).getId());
                startActivityForResult(intent, REQUSET_CREATE);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String mId = mDatas.get(position).getId();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.execSQL("DELETE FROM CARD WHERE id = ?", new String[]{mId});
                                mDatas.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                return false;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, EditActivity.class), REQUSET_CREATE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSET_CREATE) {
            switch (resultCode) {
                case EditActivity.RESULT_CHANGED:
                    initDatas();
                    mAdapter.notifyDataSetChanged();
                    break;
                case EditActivity.RESULT_UNCHANGED:
                    break;
            }
        }

    }

    private void initDatas() {
        mDatas.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM CARD", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double money = 0;
                try {
                    money = Double.parseDouble(cursor.getString(cursor.getColumnIndex("money")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                mDatas.add(0, new Card(id, name, money, time));
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

}
