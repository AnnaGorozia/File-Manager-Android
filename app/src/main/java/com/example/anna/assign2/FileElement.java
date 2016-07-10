package com.example.anna.assign2;

/**
 * Created by Anna on 4/13/2016.
 */
public class FileElement {

    private FileType fileType;
    private String name;
    private String creationDate;
    private String size;
    private int icon;
    private String path;

    public FileElement(FileType fileType, String name, String creationDate, String size, String path) {

        this.fileType = fileType;
        this.name = name;
        this.creationDate = creationDate;
        this.size = size;
        this.path = path;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getIcon() {
        int[] images = {
                R.drawable.doc,
                R.drawable.folder,
                R.drawable.mp3,
                R.drawable.png,
                R.drawable.txt,
                R.drawable.xls,
                R.drawable.zip

        };
        switch (fileType){
            case DOC:
                icon = images[0];
                break;
            case FOLDER:
                icon = images[1];
                break;
            case MP3:
                icon = images[2];
                break;
            case PNG:
                icon = images[3];
                break;
            case TXT:
                icon = images[4];
                break;
            case XLS:
                icon = images[5];
                break;
            case ZIP:
                icon = images[6];
                break;
            case OTHER:
                icon = images[1];
                break;
        }
        return icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
