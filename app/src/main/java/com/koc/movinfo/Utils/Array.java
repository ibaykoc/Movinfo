package com.koc.movinfo.Utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by MochammadIqbal on 21/11/2017.
 * Project Movinfo
 */

public class Array {
    public static String ArrayToCSV(String[] arrayToChange) {
        if (arrayToChange == null || arrayToChange.length == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        for (String s : arrayToChange) {
            builder.append(s).append(",");
        }
        builder.delete(builder.length() - 1, builder.length());

        return builder.toString();
    }

    public static String ArrayToCSV(Integer[] arrayToChange) {
        if (arrayToChange == null || arrayToChange.length == 0)
            return null;

        StringBuilder builder = new StringBuilder();
        for (Integer i : arrayToChange) {
            builder.append(String.valueOf(i)).append(",");
        }
        builder.delete(builder.length() - 1, builder.length());

        return builder.toString();
    }


    public static byte[] BitmapToByteArray(Bitmap bmp) {
        if (bmp == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Integer[] CSVToIntegerArray(String stringToChange){
        if(stringToChange == null)
            return null;
        String[] stringArray = stringToChange.split(",");
        Integer[] result = new Integer[stringArray.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(stringArray[i]);
        }
        return result;
    }
    public static String[] CSVToStringArray(String stringToChange){
        if(stringToChange == null)
            return null;
        return stringToChange.split(",");
    }
}
