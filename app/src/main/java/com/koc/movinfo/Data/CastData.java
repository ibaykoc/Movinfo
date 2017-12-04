package com.koc.movinfo.Data;

import android.graphics.Bitmap;

/**
 * Created by MochammadIqbal on 14/11/2017.
 */

public class CastData {
    public String name = null;
    public String character_name = null;
    public String profile_path = null;
    public Bitmap profile_picture = null;
    public boolean updatingImage = false;
    public boolean imageUpdated = false;

    public CastData() {

    }

    public boolean havePicture() {
        return !profile_path.matches("null");
    }
}
