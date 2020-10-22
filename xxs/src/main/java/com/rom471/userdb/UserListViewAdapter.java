package com.rom471.userdb;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rom471.lab1.R;

import java.util.List;

public class UserListViewAdapter extends ArrayAdapter<User> {
    Context context;
    int resource;
    List<User> users;
    SectionIndexer sectionIndexer;
    public UserListViewAdapter(@NonNull Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.users=objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=null;
        LinearLayout usersLayout;
        TextView name_tv;
        ImageView avatar_img;

        if(convertView!=null){
            view=convertView;
        }
        else {
            view= LayoutInflater.from(context).inflate(R.layout.userlist_layout,null);
        }
        usersLayout=view.findViewById(R.id.user_list_layout);
        name_tv=view.findViewById(R.id.item_name);
        avatar_img=view.findViewById(R.id.item_avatar);
        User user = users.get(position);
        name_tv.setText(user.getName());
        Drawable avatar=user.getAvatar();
        if(avatar!=null)
            avatar_img.setBackgroundDrawable(avatar);
        else //当用户没有头像时，使用默认头像
            avatar_img.setImageResource(R.drawable.default_avatar);
        return view;
    }
}
