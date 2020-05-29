package com.gitlet.gitVisual.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitCommand {
    private final String[] _command;
    public GitCommand(@JsonProperty("command") String[] command) {
        this._command = command;
    }

    public String[] getCommand() {
        return _command;
    }
}
