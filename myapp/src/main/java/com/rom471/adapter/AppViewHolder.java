package com.rom471.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.rom471.db2.SimpleApp;
import com.rom471.recorder.R;
import com.rom471.utils.AppUtils;

public class AppViewHolder {

        ImageView appIcon;
        TextView appName;
        TextView appInfo;

        public AppViewHolder (View view)
        {

            appIcon = (ImageView) view.findViewById(R.id.appicon);
            appName = (TextView) view.findViewById(R.id.appname);
            appInfo = (TextView) view.findViewById(R.id.app_info);
        }

        public void update(SimpleApp app){
            if(app.getIcon()==null){
                AppUtils.setSimpleAppsIcon(app);
            }
            appIcon.setBackground(app.getIcon());
            appName.setText(app.getAppName());
        }
}
