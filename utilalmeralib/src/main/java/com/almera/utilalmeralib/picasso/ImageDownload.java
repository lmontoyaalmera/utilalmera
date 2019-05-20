package com.almera.utilalmeralib.picasso;

public class ImageDownload {
    private String text;
    private String filename;

    public ImageDownload(String text, String filename) {
        this.text = text;
        this.filename = filename;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
