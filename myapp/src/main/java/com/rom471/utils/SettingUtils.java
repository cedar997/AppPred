package com.rom471.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rom471.db2.AppDao;
import com.rom471.db2.AppRecordsRepository;
import com.rom471.recorder.R;

public class SettingUtils {
    //编辑服务器地址的对话框
    public static void alert_host_edit(Context context){
        final EditText et = new EditText(context);
        String host =(String) MyProperties.get(context, "host", "");
        et.setText(host);
        new AlertDialog.Builder(context).setTitle("请输入服务器地址")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyProperties.set(context,"host",et.getText().toString());
                        //按下确定键后的事件
                        Toast.makeText(context, "修改完成",Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消",null).show();
    }
    //确认清除数据的弹出对话框
    public static void confirmClearRecordsDialog(Context context, AppRecordsRepository appDao){
        final boolean[] ret = {false};
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("警告！");
        normalDialog.setMessage("确认要删除已有的数据吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appDao.deleteAll();

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();

    }

    //跳转到打开辅助功能界面
    public static void getAccessibilityPermission(Context context){
        final String mAction= Settings.ACTION_ACCESSIBILITY_SETTINGS;//辅助功能
        Intent intent=new Intent(mAction);
        context.startActivity(intent);
    }

    //确认清除预测记录的弹出对话框
    public static void confirmClearPredsDialog(Context context,AppRecordsRepository appRecordsRepository){
        final boolean[] ret = {false};
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.drawable.ic_launcher_background);
        normalDialog.setTitle("警告！");
        normalDialog.setMessage("确认要重新开始预测吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appRecordsRepository.deletePreds();

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.show();

    }
}
