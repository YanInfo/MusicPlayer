package com.example.eplayer.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.eplayer.R;
import com.example.eplayer.entity.Music;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author zhangyan
 */
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
        TextView musicTitle = view.findViewById(R.id.music_title);
        TextView musicAuthor = view.findViewById(R.id.music_author);
        TextView musicDuration = view.findViewById(R.id.music_duration);
        ImageView imageList = view.findViewById(R.id.image_list);
        musicTitle.setText(music.getTitle());
        musicAuthor.setText("" + music.getAuthor());
        musicDuration.setText(new SimpleDateFormat("mm:ss").format(music.getDuration()));
        imageList.setImageBitmap(music.getMusicBitMap());
        return view;
    }

}