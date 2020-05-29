package com.gitlet.gitVisual.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class DataFile {
    private final String filename;
    private final String contents;
    public DataFile(@JsonProperty("filename") String filename,
                  @JsonProperty("contents")String contents) {
        this.filename = filename;
        this.contents = contents;
    }

    public String getName() {
        return filename;
    }

    public String getContents() {
        return contents;
    }
}
