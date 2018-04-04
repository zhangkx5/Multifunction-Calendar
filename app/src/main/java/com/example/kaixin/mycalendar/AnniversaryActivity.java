package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Adapter.AnniversaryAdapter;
import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class AnniversaryActivity extends AppCompatActivity {

    private AnniversaryAdapter anniversaryAdapter;
    private ListView listView;
    private List<AnniversaryDay> list;
    private ImageView ib_back, ib_add;
    private TextView tv_showzero;

    @Override
    protected void onResume() {
        super.onResume();
        list = AnniversaryUtils.queryAllLocalAnniversary(this, UserUtils.getUserId(this));
        if (list.size() == 0) {
            list = AnniversaryUtils.queryAllBmobAnniversaryDay(this);
        }
        if (list.size() == 0) {
            tv_showzero.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            tv_showzero.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            anniversaryAdapter = new AnniversaryAdapter(AnniversaryActivity.this, list);
            listView.setAdapter(anniversaryAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);


        ib_back = (ImageView)findViewById(R.id.ib_back);
        ib_add = (ImageView)findViewById(R.id.ib_add);
        listView = (ListView) findViewById(R.id.listView);
        tv_showzero = (TextView)findViewById(R.id.whenZero);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnniversaryActivity.this.finish();
            }
        });
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnniversaryActivity.this, AnniversaryEditActivity.class);
                startActivity(intent);
            }
        });
        list = AnniversaryUtils.queryAllLocalAnniversary(this, UserUtils.getUserId(this));
        if (list.size() == 0) {
            list = AnniversaryUtils.queryAllBmobAnniversaryDay(this);
        }
        if (list.size() == 0) {
            tv_showzero.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            tv_showzero.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            anniversaryAdapter = new AnniversaryAdapter(AnniversaryActivity.this, list);
            listView.setAdapter(anniversaryAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(i);
                Intent intent = new Intent(AnniversaryActivity.this, AnniversaryDetailsActivity.class);
                intent.putExtra("anniversaryDay",anniversaryDay);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnniversaryActivity.this);
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
                        AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(pos);
                        AnniversaryUtils.deleteBmobAnniversaryDay(anniversaryDay.getObjectId());
                        AnniversaryUtils.deleteLocalAnniversary(AnniversaryActivity.this, anniversaryDay.getObjectId());
                        list.remove(pos);
                        anniversaryAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}