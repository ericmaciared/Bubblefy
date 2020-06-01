package edu.url.salle.eric.macia.bubblefy.restapi.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.IOException;
import java.io.InputStream;


public class ImageManager {
    private static final String TAG = "ImageManager";

    public ImageManager(){
    }

    public Drawable getDrawable (Context context, String str) {
        Drawable createFromStream;
        IOException e;
        try {
            InputStream open = context.getAssets().open(str);
            createFromStream = Drawable.createFromStream(open, str);
            try {
                open.close();
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                return createFromStream;
            }
        } catch (IOException e3) {
            IOException iOException = e3;
            createFromStream = null;
            e = iOException;
            e.printStackTrace();
            return createFromStream;
        }
        return createFromStream;
    }
}
