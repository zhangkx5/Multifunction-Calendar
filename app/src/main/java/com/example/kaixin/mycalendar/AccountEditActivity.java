package com.example.kaixin.mycalendar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.AccountBill;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kaixin on 2018/2/7.
 */

public class AccountEditActivity extends AppCompatActivity {

    public MyDatabaseHelper myDatabaseHelper;
    public ImageButton ib_back, ib_save;
    public RadioGroup radio_type, radio_cost, radio_cost1, radio_income, radio_income1;
    public RadioButton type_cost, type_income;
    public RadioButton label_shopping, label_eating, label_travel, label_playing, label_living, label_else_cost;
    public RadioButton label_salary, label_redPacket, label_profit, label_raward, label_reimbursement, label_else_income;
    public TextView ac_date;
    public EditText ac_money, ac_notes;
    public int account_type = 0;
    public int type_label = 0;
    public String date;
    public String notes = "";
    public double money = 0.00;
    private AccountBill account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_bill_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        radio_type = (RadioGroup)findViewById(R.id.types);
        radio_cost = (RadioGroup)findViewById(R.id.labels_cost1);
        radio_cost1 = (RadioGroup)findViewById(R.id.labels_cost2);
        radio_income = (RadioGroup)findViewById(R.id.labels_income1);
        radio_income1 = (RadioGroup)findViewById(R.id.labels_income2);
        type_cost = (RadioButton)findViewById(R.id.type_cost);
        type_income = (RadioButton)findViewById(R.id.type_income);
        label_shopping = (RadioButton)findViewById(R.id.shopping);
        label_eating = (RadioButton)findViewById(R.id.eating);
        label_travel = (RadioButton)findViewById(R.id.travel);
        label_playing = (RadioButton)findViewById(R.id.playing);
        label_living = (RadioButton)findViewById(R.id.living);
        label_else_cost = (RadioButton)findViewById(R.id.else_cost);
        label_salary = (RadioButton)findViewById(R.id.salary);
        label_redPacket = (RadioButton)findViewById(R.id.redPacket);
        label_profit = (RadioButton)findViewById(R.id.profit);
        label_raward = (RadioButton)findViewById(R.id.raward);
        label_reimbursement = (RadioButton)findViewById(R.id.reimbursement);
        label_else_income = (RadioButton)findViewById(R.id.else_income);
        ac_date = (TextView)findViewById(R.id.date);
        ac_money = (EditText)findViewById(R.id.money);
        ac_notes = (EditText)findViewById(R.id.notes);

        radio_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                if (type_cost.getId() == checkId) {
                    account_type = 0;
                    radio_cost.setVisibility(View.VISIBLE);
                    radio_cost1.setVisibility(View.VISIBLE);
                    radio_income.setVisibility(View.GONE);
                    radio_income1.setVisibility(View.GONE);
                } else {
                    account_type = 1;
                    radio_cost.setVisibility(View.GONE);
                    radio_cost1.setVisibility(View.GONE);
                    radio_income.setVisibility(View.VISIBLE);
                    radio_income1.setVisibility(View.VISIBLE);
                }
            }
        });

        radio_cost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.shopping:
                        if (label_shopping.isChecked()) radio_cost1.clearCheck();
                        type_label = 0;
                        break;
                    case R.id.eating:
                        if (label_eating.isChecked()) radio_cost1.clearCheck();
                        type_label = 1;
                        break;
                    case R.id.living:
                        if (label_living.isChecked()) radio_cost1.clearCheck();
                        type_label = 2;
                        break;
                }
            }
        });
        radio_cost1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.travel:
                        if (label_travel.isChecked()) radio_cost.clearCheck();
                        type_label = 3;
                        break;
                    case R.id.playing:
                        if (label_playing.isChecked()) radio_cost.clearCheck();
                        type_label = 4;
                        break;
                    case R.id.else_cost:
                        if (label_else_cost.isChecked()) radio_cost.clearCheck();
                        type_label = 5;
                        break;
                }
            }
        });
        radio_income.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.salary:
                        if (label_salary.isChecked()) radio_income1.clearCheck();
                        type_label = 6;
                        break;
                    case R.id.redPacket:
                        if (label_redPacket.isChecked()) radio_income1.clearCheck();
                        type_label = 7;
                        break;
                    case R.id.profit:
                        if (label_profit.isChecked()) radio_income1.clearCheck();
                        type_label = 8;
                        break;
                }
            }
        });
        radio_income1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.raward:
                        if (label_raward.isChecked()) radio_income.clearCheck();
                        type_label = 9;
                        break;
                    case R.id.reimbursement:
                        if (label_reimbursement.isChecked()) radio_income.clearCheck();
                        type_label = 10;
                        break;
                    case R.id.else_income:
                        if (label_else_income.isChecked()) radio_income.clearCheck();
                        type_label = 11;
                        break;
                }
            }
        });

        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_save = (ImageButton) findViewById(R.id.ib_save);
        setDate();
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ac_money.getText().toString())) {
                    Toast.makeText(AccountEditActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                } else if (account != null){
                    money = Double.valueOf(ac_money.getText().toString());
                    notes = ac_notes.getText().toString();
                    date = ac_date.getText().toString();
                    updateInDB(account, account_type, type_label, date, money, notes);
                    AccountEditActivity.this.finish();
                } else {
                    money = Double.valueOf(ac_money.getText().toString());
                    notes = ac_notes.getText().toString();
                    date = ac_date.getText().toString();
                    addInDB(account_type, type_label, date, money, notes);
                    AccountEditActivity.this.finish();
                }
            }
        });
        ac_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        Intent intent = getIntent();
        account = (AccountBill) intent.getSerializableExtra("account");
        if (account != null) {
            TextView title = (TextView)findViewById(R.id.title);
            title.setText("编辑账单");
            account_type = account.getType();
            type_label = account.getLabel();
            date = account.getDate();
            money = account.getMoney();
            notes = account.getNotes();
            if (account_type == 0) {
                radio_type.check(R.id.type_cost);
                radio_cost.setVisibility(View.VISIBLE);
                radio_cost1.setVisibility(View.VISIBLE);
                radio_income.setVisibility(View.GONE);
                radio_income1.setVisibility(View.GONE);
            } else {
                radio_type.check(R.id.type_income);
                radio_cost.setVisibility(View.GONE);
                radio_cost1.setVisibility(View.GONE);
                radio_income.setVisibility(View.VISIBLE);
                radio_income1.setVisibility(View.VISIBLE);
            }
            switch (type_label) {
                case 0:
                    radio_cost.check(R.id.shopping);
                    break;
                case 1:
                    radio_cost.check(R.id.eating);
                    break;
                case 2:
                    radio_cost.check(R.id.living);
                    break;
                case 3:
                    radio_cost1.check(R.id.travel);
                    break;
                case 4:
                    radio_cost1.check(R.id.playing);
                    break;
                case 5:
                    radio_cost1.check(R.id.else_cost);
                    break;
                case 6:
                    radio_income.check(R.id.salary);
                    break;
                case 7:
                    radio_income.check(R.id.redPacket);
                    break;
                case 8:
                    radio_income.check(R.id.profit);
                    break;
                case 9:
                    radio_income1.check(R.id.raward);
                    break;
                case 10:
                    radio_income1.check(R.id.reimbursement);
                    break;
                case 11:
                    radio_income1.check(R.id.else_income);
                    break;
            }
            ac_date.setText(date);
            ac_money.setText(""+money);
            ac_notes.setText(notes);
        }
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear+1;
                String month = (monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear);
                String day = (dayOfMonth < 10) ? ("0" + dayOfMonth) :("" + dayOfMonth);
                ac_date.setText(year+"年"+month+"月"+day+"日");
            }
        }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    public void setDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date ymd = new Date(System.currentTimeMillis());
        ac_date.setText(simpleDateFormat.format(ymd));
    }

    public void addInDB(int type, int label, String date, double money, String notes) {
        String acid = String.valueOf(System.currentTimeMillis());
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.ACCOUNT_TABLE_INSERT, new Object[]{acid, type, label, date, money, notes});
        dbWrite.close();
        Toast.makeText(AccountEditActivity.this, acid + type + label + date + money + notes, Toast.LENGTH_SHORT).show();
    }

    public void updateInDB(AccountBill account, int type, int label, String date, double money, String notes) {
        String ac_id = account.getId();
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("label", label);
        values.put("date", date);
        values.put("money", money);
        values.put("notes", notes);
        dbUpdate.update(MyDatabaseHelper.ACCOUNT_TABLE_NAME, values,
                "acid = ?", new String[] {ac_id});
    }
}
