package com.gitlet.gitVisual.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlet.gitVisual.model.DataFile;
import com.gitlet.gitVisual.model.GitCommand;
import com.gitlet.gitVisual.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/git")
@RestController
@CrossOrigin("*")
public class GitController {
    private final GitService _gitService;
    @Autowired
    public GitController(GitService gitService) {
        this._gitService = gitService;
    }

    @PostMapping
    public String addUser() {
        return this._gitService.addUser();
    }

    @DeleteMapping(path = "{id}")
    public void removeUser(@PathVariable("id") UUID id) {
        this._gitService.removeUser(id);
    }

    @PostMapping(path = "{id}/file/add")
    public void addFile(@PathVariable("id") UUID uuid, @RequestBody DataFile datafile) {
        this._gitService.addFile(uuid, datafile.getName(), datafile.getContents());
    }


    @DeleteMapping (path = "{id}/file")
    public void removeFile(@PathVariable("id") UUID uuid, @RequestBody DataFile datafile) {
        //FIXME: need to find out how to make this variable optional
        this._gitService.removeFile(uuid, datafile.getName());
    }

    @PostMapping(path = "{id}/file/edit")
    public void editFile(@PathVariable("id") UUID id, @RequestBody DataFile datafile) {
        this._gitService.editFile(id, datafile.getName(), datafile.getContents());
    }

    @PostMapping(path = "{id}")
    public List<String> process(@PathVariable("id") UUID id, @RequestBody GitCommand command) {
        //return _gitService.process(args);
        return(this._gitService.process(id, command.getCommand()));
    }
}
