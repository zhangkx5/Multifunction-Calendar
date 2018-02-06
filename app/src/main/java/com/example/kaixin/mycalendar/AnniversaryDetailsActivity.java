package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;

/**
 * Created by kaixin on 2018/2/5.
 */

public class AnniversaryDetailsActivity  extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private TextView an_name, an_date, an_notes, an_from, an_next;
    private ImageButton ib_back, ib_delete;
    private Button btn_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_details);

        myDatabaseHelper = new MyDatabaseHelper(this);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        an_name = (TextView)findViewById(R.id.name);
        an_date = (TextView)findViewById(R.id.date);
        an_notes = (TextView)findViewById(R.id.notes);
        an_from = (TextView)findViewById(R.id.from);
        an_next = (TextView)findViewById(R.id.next);
        btn_edit = (Button)findViewById(R.id.btn_edit);

        Intent intent = getIntent();
        final AnniversaryDay anniversaryDay = (AnniversaryDay)intent.getSerializableExtra("anniversaryDay");
        an_name.setText(anniversaryDay.getName());
        an_date.setText("起始日期：" + anniversaryDay.getDate());
        an_notes.setText(anniversaryDay.getNotes());
        an_next.setText(anniversaryDay.getId());
        an_from.setText("--");
        try {
            String from = AnniversaryAdapter.fromThatDay(anniversaryDay.getDate());
            an_from.setText(from);
            String next = AnniversaryAdapter.toNextDay(anniversaryDay.getDate());
            an_next.setText(next);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnniversaryDetailsActivity.this.finish();
            }
        });
        ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnniversaryDetailsActivity.this);
                builder.setTitle("删除提醒");
                builder.setMessage("确定要删除吗？此操作不可逆！");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteInDB(anniversaryDay.getId());
                        dialogInterface.dismiss();
                        AnniversaryDetailsActivity.this.finish();
                    }
                });
                builder.create().show();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnniversaryDetailsActivity.this, AnniversaryEditActivity.class);
                intent.putExtra("anniversaryDay", anniversaryDay);
                startActivity(intent);
                AnniversaryDetailsActivity.this.finish();
            }
        });

    }
    public void deleteInDB(String id) {
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(MyDatabaseHelper.ANNIVERSARY_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
}
