package com.gitlet.gitVisual.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gitlet.gitVisual.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/git")
@RestController
@CrossOrigin("*")
public class GitController {
    private final GitService _gitService;
    @Autowired
    public GitController(GitService gitService) {
        this._gitService = gitService;
    }
    /**Process a post request to process a git command.
     * @param args Message sent to gitlet from REST
     */
    @PostMapping
    public String process(@RequestBody @JsonProperty("command") String... args) {
        //return _gitService.process(args);
        return("Hello world");
    }

    @GetMapping
    public String get() {
        //return _gitService.process(args);
        return("Hello world");
    }
}
