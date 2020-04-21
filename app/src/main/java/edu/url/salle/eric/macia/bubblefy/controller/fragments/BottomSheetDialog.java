package edu.url.salle.eric.macia.bubblefy.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import edu.url.salle.eric.macia.bubblefy.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private String title;

    public BottomSheetDialog(String text) {
        title = text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.track_option_sheet, container, false);
        TextView tvTitle = v.findViewById(R.id.title);
        tvTitle.setText(title);
        Button buttonLike = v.findViewById(R.id.like_button);
        Button buttonAddQueue = v.findViewById(R.id.add_queue);
        Button buttonShare= v.findViewById(R.id.share);
        Button buttonAddPlaylist = v.findViewById(R.id.add_to_playlist);
        Button buttonShuffle = v.findViewById(R.id.random_playback);

        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
            //IMPLEMENTAR ELS ALTRES BUTTONS
        });

        buttonAddQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IMPLEMENTAR
            }
        });

        return v;
    }

    public interface BottomSheetListener{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        }
        catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
