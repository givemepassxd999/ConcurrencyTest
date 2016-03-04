package com.example.givemepass.concurrenttest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick.wu on 2016/3/4.
 */
public class MyData {
    private static MyData ourInstance = new MyData();
    private List<String> mData;
    public static MyData getInstance() {
        return ourInstance;
    }

    private MyData() {
        mData = new ArrayList<>();
    }

    public static List<String> getData(){
        return ourInstance.mData;
    }

    public static void clear(){
        ourInstance.mData.clear();
    }
    public static void removeItem(int i){
        ourInstance.mData.remove(i);
    }
    public static void addItem(String s){
        if(s != null) {
            ourInstance.mData.add(s);
        }
    }
}
