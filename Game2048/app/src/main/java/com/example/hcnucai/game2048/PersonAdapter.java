package com.example.hcnucai.game2048;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hcnucai on 2016/12/5.
 */

public class PersonAdapter extends ArrayAdapter<Person>{
    private int resourceId;
    public PersonAdapter(Context context, int resource, List<Person> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前的person实例
        final Person person = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
       TextView positionView = (TextView)view.findViewById(R.id.position);
        positionView.setText(String.valueOf(position + 1));
        TextView username = (TextView)view.findViewById(R.id.username);
        username.setText(person.getUsername());
        TextView highScore = (TextView)view.findViewById(R.id.highScore);
        highScore.setText(person.getHighScore());
        return view;
    }
}
