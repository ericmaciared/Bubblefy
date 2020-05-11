package edu.url.salle.eric.macia.bubblefy.controller.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;



public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.ViewHolder> {

    private static final String TAG = "SelectPlaylistAdapter";

    private ArrayList<Playlist> mPlaylist;
    private Context mContext;
    private ArrayList<Playlist> selectedPlaylists = new ArrayList<>();

    public SelectPlaylistAdapter(Context mContext, ArrayList<Playlist> mPlaylist) {
        this.mPlaylist = mPlaylist;
        this.mContext = mContext;
    }

    public ArrayList<Playlist> getSelectedPlaylist() {
        return selectedPlaylists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item_select, parent, false);
        ViewHolder vh = new ViewHolder(itemView);

        Log.d(TAG, "onCreateViewHolder: called. viewHolder hashCode: " + vh.hashCode());
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. viewHolder hashcode: " + holder.hashCode());


        holder.tvTitle.setText(mPlaylist.get(position).getName());
        holder.tvAuthor.setText("By: " + mPlaylist.get(position).getUserLogin() + "  -  " + mPlaylist.get(position).countTracks() + " tracks");

        holder.setCheckBoxListener(new CheckBoxListener() {
            @Override
            public void onItemClicked(View v, int pos) {
                CheckBox checkBox = (CheckBox)v;
                if(checkBox.isChecked()){
                    selectedPlaylists.add(mPlaylist.get(pos));
                }else if(!checkBox.isChecked()){
                    selectedPlaylists.remove(mPlaylist.get(pos));
                }
            }
        });

        if (mPlaylist.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(mPlaylist.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }
    }

    @Override
    public int getItemCount() {
        return mPlaylist != null ? mPlaylist.size():0;
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout mLayout;
        CheckBox checkBox;
        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;

        CheckBoxListener checkBoxListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.playlist_item_layout_select);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvTitle = (TextView) itemView.findViewById(R.id.track_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.track_author);
            ivPicture = (ImageView) itemView.findViewById(R.id.track_img);

            checkBox.setOnClickListener(this);
        }

        public void setCheckBoxListener(CheckBoxListener checkBoxClicked){
            this.checkBoxListener = checkBoxClicked;
        }

        @Override
        public void onClick(View v) {
            this.checkBoxListener.onItemClicked(v, getLayoutPosition());
        }
    }
}


