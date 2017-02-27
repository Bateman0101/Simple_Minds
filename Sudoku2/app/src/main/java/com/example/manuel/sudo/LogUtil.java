package com.example.manuel.sudo;

import android.util.Log;


public class LogUtil {
    public static void log(String content) {
        Log.i("sudoku", content);
    }

    public static void log(Object obj) {
        Log.i("sudoku", obj.toString());
    }
}
