package com.example.dailyselfie;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class SelfieRecordAdapter extends BaseAdapter {

    private ArrayList<SelfieRecord> recordList = new ArrayList<SelfieRecord>();
    private Context context;

    public SelfieRecordAdapter(Context mContext) {
        context = mContext;

        File storageDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] selfieFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".jpg");
                }
            });

            for (File file : selfieFiles) {
                SelfieRecord selfieRecord = new SelfieRecord(file.getAbsolutePath(), file.getName());
                recordList.add(selfieRecord);
            }
        }


    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SelfieRecord currentSefileRecord = recordList.get(position);

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);

            convertView = inflater.inflate(R.layout.selfie_listitem, parent, false);

            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_selected);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.date = (TextView) convertView.findViewById(R.id.selfie_date);

            convertView.setTag(viewHolder);

        }
        else
            viewHolder = (ViewHolder) convertView.getTag();



        viewHolder.checkBox.setChecked(currentSefileRecord.isSelected());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                currentSefileRecord.setSelected(isChecked);

            }
        });
        ImageHelper.setImageFromFilePath(currentSefileRecord.getPath(), viewHolder.imageView);
        viewHolder.date.setText(currentSefileRecord.getDisplayName());

        return convertView;
    }

    public void add(SelfieRecord selfieRecord) {
        recordList.add(selfieRecord);
        notifyDataSetChanged();
    }

    public ArrayList<SelfieRecord> getAllRecords() {
        return recordList;
    }

    public ArrayList<SelfieRecord> getSelectedRecords() {
        ArrayList<SelfieRecord> selectedRecordList = new ArrayList<>();
        for (SelfieRecord record : recordList) {
            if (record.isSelected()) {
                selectedRecordList.add(record);
            }
        }
        return selectedRecordList;
    }

    public void clearAll() {
        recordList.clear();
        notifyDataSetChanged();
    }

    public void clearSelected() {
        recordList.removeAll(getSelectedRecords());
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView date;
    }

}
