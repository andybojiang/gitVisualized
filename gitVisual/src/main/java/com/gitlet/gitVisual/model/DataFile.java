package com.gitlet.gitVisual.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DataFile {
    private final String filename;
    private final String contents;


    public int getStatus() {
        return status;
    }

    /**
     * 0 is not tracked
     * 1 is staged
     * 2 is tracked in current commit
     */
    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

    public DataFile(@JsonProperty("filename") String filename,
                  @JsonProperty("contents")String contents) {
        this.filename = filename;
        this.contents = contents;
    }

    public String getFilename() {
        return filename;
    }

    public String getContents() {
        return contents;
    }
}
