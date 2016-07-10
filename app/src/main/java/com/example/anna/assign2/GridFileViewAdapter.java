package com.example.anna.assign2;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anna on 4/15/2016.
 */
public class GridFileViewAdapter extends ArrayAdapter<FileElement> {

    private ArrayList<FileElement> list;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;

    public GridFileViewAdapter (Context context, int id, ArrayList<FileElement> list){
        super(context, id, list);
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
        this.list = list;
    }

    @Override
    public void remove(FileElement string) {
        list.remove(string);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        ViewHolder viewHolder = null;
        if(convertView == null){
            rootView = View.inflate(context, R.layout.grid_item_view, null);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.grid_image);
            TextView nameView = (TextView) rootView.findViewById(R.id.grid_text);
            viewHolder = new ViewHolder();
            viewHolder.imageView = imageView;
            viewHolder.nameView = nameView;
            rootView.setTag(viewHolder);
        }else{
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        FileElement fileElement = (FileElement) getItem(position);

        viewHolder.nameView.setText(fileElement.getName());
        viewHolder.imageView.setImageResource(fileElement.getIcon());

        return rootView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView nameView;
    }
}
