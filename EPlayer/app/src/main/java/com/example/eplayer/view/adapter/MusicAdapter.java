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
 * 音乐适配器，缓存优化
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

        View view;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder.musicTitle = view.findViewById(R.id.music_title);
            viewHolder.musicAuthor = view.findViewById(R.id.music_author);
            viewHolder.musicDuration = view.findViewById(R.id.music_duration);
            viewHolder.imageList = view.findViewById(R.id.image_list);
            // 将ViewHolder存储在View中
            view.setTag(viewHolder);
        } else {
            view = convertView;
            // 重新获取VIewHolder
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.musicTitle.setText(music.getTitle());
        viewHolder.musicAuthor.setText("" + music.getAuthor());
        viewHolder.musicDuration.setText(new SimpleDateFormat("mm:ss").format(music.getDuration()));
        viewHolder.imageList.setImageBitmap(music.getMusicBitMap());
        return view;
    }

    /**
     * 控件实例缓存
     */
    class ViewHolder {

        TextView musicTitle;
        TextView musicAuthor;
        TextView musicDuration;
        ImageView imageList;

    }

}