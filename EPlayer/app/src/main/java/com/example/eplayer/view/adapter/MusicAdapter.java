package com.example.eplayer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eplayer.R;
import com.example.eplayer.entity.Music;

import java.text.SimpleDateFormat;
import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private int resourceId;

    public MusicAdapter(Context context, int resource, List<Music> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前项的Music实例
        Music music = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView musicId = view.findViewById(R.id.music_id);
        TextView musicTitle = view.findViewById(R.id.music_title);
        TextView musicUrl = view.findViewById(R.id.music_url);
        TextView musicAuthor = view.findViewById(R.id.music_author);
        TextView musicDuration = view.findViewById(R.id.music_duration);
        musicId.setText("" + music.getId());
        musicTitle.setText(music.getTitle());
        musicUrl.setText(music.getUrl());
        musicAuthor.setText("" + music.getAuthor());
        musicDuration.setText(new SimpleDateFormat("mm:ss").format(music.getDuration()));
        return view;
    }

}