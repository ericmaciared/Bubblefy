package edu.url.salle.eric.macia.bubblefy.controller.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.Playlist;


public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {

    private OnItemClickedListener mListener;
    private static final String TAG = "TrackListAdapter";
    private ArrayList<Playlist> mPlaylists;
    private Context mContext;

    public interface OnItemClickedListener{
        void onItemClicked(int position);
        void onOptionsClicked(int position);
    }

    public void setItemClickedListener(OnItemClickedListener listener){
        mListener = listener;
    }

    public PlaylistListAdapter(Context context, ArrayList<Playlist> playlists) {
        mContext = context;
        mPlaylists = playlists;
    }
    
    @NonNull
    @Override
    public PlaylistListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item_long, parent, false);
        return new PlaylistListAdapter.ViewHolder(itemView, mListener);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(mPlaylists.get(position).getName());
        holder.tvDescription.setText("By: " + mPlaylists.get(position).getUserLogin() + "  -  " + mPlaylists.get(position).countTracks() + " tracks");
        if (mPlaylists.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(mPlaylists.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }
    }

    @Override
    public int getItemCount() {
        return mPlaylists != null ? mPlaylists.size():0;
    }

    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
        mPlaylists.get(position).setFollowed(isLiked);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView, final OnItemClickedListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.track_title);
            tvDescription = (TextView) itemView.findViewById(R.id.track_author);
            ivPicture = (ImageView) itemView.findViewById(R.id.track_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClicked(position);
                        }
                    }
                }
            });

        }
    }
}