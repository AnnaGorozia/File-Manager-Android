package com.example.anna.assign2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private static final String ROOT_DIR="/sdcard/";
    Stack<String> history;
    ListView listview;
    ImageButton back;
    ImageButton grid;
    ImageButton list;
    GridView gridview;
    TextView path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showActionBar();

        listview = (ListView) findViewById(R.id.listview);
        gridview = (GridView) findViewById(R.id.gridview);
        path = (TextView) findViewById(R.id.path);

        history = new Stack<>();
        try {
            getData(ROOT_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addToolbarCliCkListeners();

    }

    private String transferPath(String s) {
        if (s.length() == 0) return "";
        String res = "";
        String sep = " > ";
        ArrayList<String> names = new ArrayList<>(Arrays.asList(s.split("/")));
        String prev = "";
        for (int i = names.size() - 1; i >= 1 ; i--) {
            prev = res;

            if (names.get(i).equals("sdcard")) {
                res = "Internal Storage" + res;
            } else {
                res = sep + names.get(i) + res;
            }

            if (getTextWidth(res) >= (getDisplayWidth() - 60)) {
                res = prev;
                break;
            }
        }
        return res;
    }

    private int getDisplayWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private int getTextWidth(String s) {
        Rect bounds = new Rect();
        Paint textPaint = path.getPaint();
        textPaint.getTextBounds(s, 0, s.length(), bounds);
        int width = bounds.width();
        return width;
    }


    private void addToolbarCliCkListeners() {
        back = (ImageButton) findViewById(R.id.menu_back);
        list = (ImageButton) findViewById(R.id.menu_list);
        grid = (ImageButton) findViewById(R.id.menu_grid);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewVisibility(false);
            }
        });

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewVisibility(true);
            }
        });
    }


    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.ab_custom, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }

    private void goBack() {
        try {
            history.pop();
            if (history.isEmpty()) {
                getData(ROOT_DIR);
            } else {
                getData(history.pop());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeViewVisibility(boolean b) {
        if (b) {
            gridview.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        } else {
            listview.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.INVISIBLE);
        }
    }

    public static void openFile(Context context, File url) throws IOException {
        File file=url;
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            intent.setDataAndType(uri, "video/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private ArrayList<FileElement> showFolders(File f) throws IOException {
        ArrayList<FileElement> fileElements = new ArrayList<>();
        if (f == null) return null;
        File [] childFiles = f.listFiles();
        if (childFiles == null) {
            openFile(getApplicationContext(), f);
            return null;
        }
        String name;
        String size = "";
        String date;
        String path = "";
        FileType fileType = null;
        for(File ffs : childFiles){

            if(ffs != null){
                name = ffs.getName();
                date = DateFormat.getDateTimeInstance().format(new Date(ffs.lastModified()));
                if(ffs.isDirectory()){
                    fileType = FileType.FOLDER;
                    File [] childParent = ffs.listFiles();
                    if (childParent != null) {
                        size = "" + childParent.length;
                    }
                }else{
                    fileType = FileType.OTHER;
                    if (ffs.getName().contains(".mp3")) {
                        fileType = FileType.MP3;
                    } else if (ffs.getName().contains(".png")) {
                        fileType = FileType.PNG;
                    } else if (ffs.getName().contains(".doc")) {
                        fileType = FileType.DOC;
                    } else if (ffs.getName().contains(".xls")) {
                        fileType = FileType.XLS;
                    } else if (ffs.getName().contains(".zip")) {
                        fileType = FileType.ZIP;
                    } else if (ffs.getName().contains(".txt")) {
                        fileType = FileType.TXT;
                    }

                    size = "" + ffs.length() + " Bytes";
                }
                fileElements.add(new FileElement(fileType, name, date, size, path));
            }
        }
        return fileElements;
    }

    private void getData(final String s) throws IOException {
        File file = new File(s);
        if (file.isDirectory()) {
            history.push(s);
            path.setText(transferPath(s));

        }

        ArrayList<FileElement> fileElements = showFolders(file);
        if (fileElements == null){
            return;
        }
        final FileViewAdapter listAdapter = new FileViewAdapter(this, R.layout.list_item_view ,fileElements);
        listview.setAdapter(listAdapter);

        final GridFileViewAdapter gridAdapter = new GridFileViewAdapter(this, R.layout.grid_item_view, fileElements);
        gridview.setAdapter(gridAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileElement fileElement = ((FileViewAdapter)parent.getAdapter()).getItem(position);
                try {
                    getData(s + fileElement.getName() + "/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                                                          int position, long id, boolean checked) {

                        mode.setTitle(listview.getCheckedItemCount() + " Selected");
                        listAdapter.toggleSelection(position);
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.delete){
                            SparseBooleanArray selected = listAdapter.getSelectedIds();
                            short size = (short)selected.size();
                            for (byte i = 0; i<size; i++){
                                if (selected.valueAt(i)) {
                                    FileElement selectedItem = listAdapter.getItem(selected.keyAt(i));
                                    listAdapter.remove(selectedItem);
                                }
                            }

                            mode.finish();
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.main_menu, menu);
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                });
                return false;
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileElement fileElement = ((GridFileViewAdapter)parent.getAdapter()).getItem(position);
                try {
                    getData(s + fileElement.getName() + "/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                gridview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode,
                                                          int position, long id, boolean checked) {

                        mode.setTitle(gridview.getCheckedItemCount() + " Selected");
                        gridAdapter.toggleSelection(position);
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.getItemId() == R.id.delete){
                            SparseBooleanArray selected = gridAdapter.getSelectedIds();
                            short size = (short)selected.size();
                            for (byte i = 0; i<size; i++){
                                if (selected.valueAt(i)) {
                                    FileElement selectedItem = gridAdapter.getItem(selected.keyAt(i));
                                    gridAdapter.remove(selectedItem);
                                }
                            }

                            mode.finish();
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.main_menu, menu);
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                });
                return false;
            }
        });
    }
}
