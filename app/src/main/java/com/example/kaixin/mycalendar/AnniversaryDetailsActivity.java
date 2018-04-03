package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Adapter.AnniversaryAdapter;
import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;

import java.text.ParseException;

/**
 * Created by kaixin on 2018/2/5.
 */

public class AnniversaryDetailsActivity  extends AppCompatActivity {

    private TextView an_name, an_date, an_notes, an_from, an_next;
    private ImageButton ib_back, ib_delete;
    private Button btn_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_details);

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
        an_name.setText(anniversaryDay.getAnniversaryName());
        an_date.setText("起始日期：" + anniversaryDay.getAnniversaryDate());
        an_notes.setText(anniversaryDay.getAnniversaryNotes());
        an_next.setText(anniversaryDay.getObjectId());
        an_from.setText("--");
        try {
            String from = AnniversaryAdapter.fromThatDay(anniversaryDay.getAnniversaryDate());
            an_from.setText(from);
            String next = AnniversaryAdapter.toNextDay(anniversaryDay.getAnniversaryDate());
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
                        AnniversaryUtils.deleteLocalAnniversary(AnniversaryDetailsActivity.this, anniversaryDay.getObjectId());
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
}
