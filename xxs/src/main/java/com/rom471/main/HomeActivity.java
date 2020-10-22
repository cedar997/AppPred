package com.rom471.main;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.rom471.lab1.R;
import com.rom471.userdb.UsersDBHelper;
import com.rom471.userdb.User;
import com.rom471.userdb.UserListViewAdapter;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG="cedar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UsersDBHelper db=new UsersDBHelper(this);

        db.InitialWithTestData();
        print();
        List<User> users = db.queryAll();

        ListView users_listview=findViewById(R.id.users_listview);
        UserListViewAdapter ulva=new UserListViewAdapter(this,R.layout.userlist_layout,users);
        users_listview.setAdapter(ulva);
    }
    public void print(String ... s){

    }
}