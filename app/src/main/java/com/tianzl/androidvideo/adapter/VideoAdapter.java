package com.tianzl.androidvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.entity.Video;
import com.tianzl.androidvideo.entity.VideoInfo;

import java.util.List;

/**
 * Created by tianzl on 2017/8/6.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    private Context context;
    private List<VideoInfo> mData;
    private LayoutInflater inflater;
    public VideoAdapter(Context context,List<VideoInfo> mData){
        this.context=context;
        this.mData=mData;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(inflater.inflate(R.layout.item_fm_net,parent,false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {
        holder.icon.setImageResource(mData.get(position).getImg());
        holder.tvName.setText(mData.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(mData.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public interface OnItemClickListener{
        void onItemClick(VideoInfo video,int position);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener=listener;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView icon;
        TextView tvName;
        public VideoViewHolder(View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.item_fm_net_icon);
            tvName=itemView.findViewById(R.id.item_fm_net_name);
        }
    }
}
