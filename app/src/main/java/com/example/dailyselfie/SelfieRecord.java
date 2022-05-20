package com.example.dailyselfie;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfieRecord {
    private String path;
    private String name;
    private boolean selected;

    public SelfieRecord(String mParth, String mName){
        path = mParth;
        name = mName;
        selected = false;
    }

    @Override
    public String toString() {
        return "SelfieRecord{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", select=" + selected +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean select) {
        this.selected = select;
    }

    public String getDisplayName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = format.parse(name, new ParsePosition(0));
        return new SimpleDateFormat("dd MMM, yyyy HH:mm:ss").format(date);
    }
}
