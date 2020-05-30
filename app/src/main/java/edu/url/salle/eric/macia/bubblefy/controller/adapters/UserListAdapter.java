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

import java.util.ArrayList;

import edu.url.salle.eric.macia.bubblefy.R;
import edu.url.salle.eric.macia.bubblefy.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private OnItemClickedListener mListener;
    private static final String TAG = "TrackListAdapter";
    private ArrayList<User> mUsers;
    private Context mContext;

    public interface OnItemClickedListener{
        void onItemClicked(int position);
        void onOptionsClicked(int position);
    }

    public void setItemClickedListener(OnItemClickedListener listener){
        mListener = listener;
    }

    public UserListAdapter(Context context, ArrayList<User> user) {
        mContext = context;
        mUsers = user;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserListAdapter.ViewHolder(itemView, mListener);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(mUsers.get(position).getLogin());
        holder.tvDescription.setText(mUsers.get(position).getFollowers() + " followers  -  " + mUsers.get(position).getPlaylists() + " playlists");
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size():0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        ImageButton btOptions;

        public ViewHolder(@NonNull View itemView, final OnItemClickedListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.track_title);
            tvDescription = (TextView) itemView.findViewById(R.id.track_author);
            btOptions = (ImageButton) itemView.findViewById(R.id.option_button);

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