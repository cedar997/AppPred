package com.rom471.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.rom471.lab1.R;
import com.rom471.userdb.User;
import com.rom471.userdb.UserListViewAdapter;
import com.rom471.userdb.UsersDBHelper;

import java.util.List;

public class UsersFragment extends Fragment {
    ListView users_listview;
    public UsersFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.layout_users_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        users_listview=getActivity().findViewById(R.id.users_listview);
        UsersDBHelper db=new UsersDBHelper(getContext());

        db.InitialWithTestData();

        List<User> users = db.queryAll();
        Log.d("CEDAR", "user size"+users.size());
        UserListViewAdapter ulva=new UserListViewAdapter(getContext(),R.layout.userlist_layout,users);
        users_listview.setAdapter(ulva);
    }
}
