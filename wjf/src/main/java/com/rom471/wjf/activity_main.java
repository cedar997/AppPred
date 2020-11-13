package com.rom471.wjf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class activity_main extends Activity {

    private LineChartView lineChart;

    String[] date = {"5-23","5-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","1-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","4-22","9-22","10-22","11-22","zxc"};//x轴
    int[] score= {74,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,20};//图表数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        getAxisXLables();//获取x轴标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
    }

    /**
     * 初始化
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(false);//选中备注
        line.setHasLines(true);//是否用线显示，false只有点无线
        line.setHasPoints(true);// 是否显示圆点，为false只有线无点
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //x轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴字体是否斜体
        //axisX.setTextColor(Color.WHITE);  //字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//字体颜色

//	    axisX.setName("");  //表格名称
        axisX.setTextSize(11);//字体大小
        axisX.setMaxLabelChars(7); //最多几个x轴坐标
        axisX.setValues(mAxisXValues);  //填充x轴坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        //y轴
        Axis axisY = new Axis();
        axisY.setName("");
        axisY.setTextSize(11);
        data.setAxisYLeft(axisY);  //y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动和平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 3);//最大放大比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        //**注：下面的7，10只是代表一个数字去类比而已
        // * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * 设置x轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    /**
     * 设置y轴的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }
}
