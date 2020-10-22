package com.rom471.main;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.userdb.DBHelper;
import com.rom471.userdb.User;
import com.rom471.userdb.UserListViewAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG="cedar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DBHelper db=new DBHelper(this);

        db.InitialWithTestData();
        Log.d(TAG, "onCreate: users size=");
        List<User> users = db.queryAll();
        Log.d(TAG, "onCreate: users size="+users.size());
        ListView users_listview=findViewById(R.id.users_listview);
        UserListViewAdapter ulva=new UserListViewAdapter(this,R.layout.userlist_layout,users);
        users_listview.setAdapter(ulva);
    }
}