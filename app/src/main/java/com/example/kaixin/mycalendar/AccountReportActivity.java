package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by kaixin on 2018/2/8.
 */

public class AccountReportActivity extends AppCompatActivity {

    private ImageButton ib_back;
    public PieChart pieChart_cost, pieChart_income;
    private double[] mon;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_report);

        intent = getIntent();
        mon = intent.getDoubleArrayExtra("mon");
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        pieChart_cost = (PieChart)findViewById(R.id.pieChart_cost);
        pieChart_income = (PieChart)findViewById(R.id.pieChart_income);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountReportActivity.this.finish();
            }
        });

        setData(mon, 0, 5, pieChart_cost);
        setData(mon, 6, 11, pieChart_income);
    }

    private void setData(double[] array, int i, int len, PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] LABLES = new String[] {"购物", "餐饮", "居住", "交通", "娱乐", "其他",
        "工资", "红包", "收益", "奖金", "报销", "其他"};
        double money = 0;
        for (; i < len; i++) {
            if (array[i] != 0) {
                entries.add(new PieEntry((float) array[i], LABLES[i]));
                money += array[i];
            }
        }
        /*entries.add(new PieEntry(30, "餐饮"));
        entries.add(new PieEntry(20, "居住"));
        entries.add(new PieEntry(10, "交通"));
        entries.add(new PieEntry(20, "娱乐"));
        entries.add(new PieEntry(10, "其他"));*/

        PieDataSet pieDataSet = new PieDataSet(entries, "类型");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c :ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setData(pieData);
        pieChart.highlightValue(null);
        pieChart.invalidate();

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        if (len == 5) {
            pieChart.setCenterText("本月消费情况" + "\n" + "共支出 " + money + " 元");
        } else {
            pieChart.setCenterText("本月收入情况" + "\n" + "共收入 " + money + " 元");
        }
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleColor(Color.BLACK);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
    }
}
