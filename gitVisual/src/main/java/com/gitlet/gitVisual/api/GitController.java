package com.gitlet.gitVisual.api;

import com.gitlet.gitVisual.model.DataFile;
import com.gitlet.gitVisual.model.GitCommand;
import com.gitlet.gitVisual.model.CytoscapeElements;
import com.gitlet.gitVisual.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
        this._gitService.addFile(uuid, datafile.getFilename(), datafile.getContents());
    }


    @DeleteMapping (path = "{id}/file")
    public void removeFile(@PathVariable("id") UUID uuid, @RequestBody DataFile datafile) {
        //FIXME: need to find out how to make this variable optional
        this._gitService.removeFile(uuid, datafile.getFilename());
    }

    @PostMapping(path = "{id}/file/edit")
    public void editFile(@PathVariable("id") UUID id, @RequestBody DataFile datafile) {
        this._gitService.editFile(id, datafile.getFilename(), datafile.getContents());
    }

    @PostMapping(path = "{id}")
    public List<String> process(@PathVariable("id") UUID id, @RequestBody GitCommand command) {
        //return _gitService.process(args);
        return(this._gitService.process(id, command.getCommand()));
    }

    @GetMapping(path = "{id}")
    public List<DataFile> getFilesStructure(@PathVariable("id") UUID id) {
        return this._gitService.getFilesStructure(id);
    }

    @GetMapping(path = "{id}/elements")
    public CytoscapeElements getLatestElements(@PathVariable("id") UUID id) {
        return this._gitService.getLatestElements(id);
    }

    //getter for a list of branch heads
    @GetMapping(path = "{id}/branches")
    public Map<String, String> getBranchHeads(@PathVariable("id") UUID id) {
        return this._gitService.getBranchHeads(id);
    }

}
