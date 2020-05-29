package com.gitlet.gitVisual.service;

import com.gitlet.gitVisual.dao.GitDao;
import com.gitlet.gitVisual.model.GitletException;
import com.gitlet.gitVisual.model.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//FIXME: page is still in progress
@Service
public class GitService {
    /**In-memory database.*/
    private static GitDao _gitdao;

    @Autowired
    public GitService(@Qualifier("gitDao") GitDao gitDao) {
        this._gitdao = gitDao;
    }

    public GitService() {
        this._gitdao = new GitDao();
    }

    /**
     * Adds a new user in the database.
     */
    public void addUser() {
        UUID newId = UUID.randomUUID();
        _gitdao.newUser(newId);
    }
    /**
     * Removes user with UUID uuid from database.
     */
    public void removeUser(UUID uuid) {
        _gitdao.removeUser(uuid);
    }
    /**
     * Adds a file to the system for a specified user with file name and content.
     */
    public void addFile(UUID uuid, String name, String contents) {
        _gitdao.addFile(uuid, name, contents);
    }
    /**
     * Deletes file for a user.
     */
    public void removeFile(UUID uuid, String name) {
        _gitdao.removeFile(uuid, name);
    }
    /**
     * Edits the content of a file.
     */
    public void editFile(UUID uuid, String name, String newContents) {
        _gitdao.removeFile(uuid, name);
        _gitdao.addFile(uuid, name, newContents);
    }

    public List<String> process(UUID uuid, String... args) {
        List<String> result = new ArrayList<>();
        if (args.length == 0) {
            result.add("Please enter a command.");
        } else {
            try {
                switch (args[0]) {
                    case "init":
                        init(uuid, args);
                        break;
                    case "add":
                        add(uuid, args);
                        break;
                    case "rm":
                        rm(uuid, args);
                        break;
                    case "commit":
                        commit(uuid, args);
                        break;
                    case "log":
                        result.add(log(uuid, args));
                        break;
                    case "global-log":
                        result.add(globalLog(uuid, args));
                        break;
                    case "find":
                        result.add(find(uuid, args));
                        break;
                    case "status":
                        result.add(status(uuid, args));
                        break;
                    case "checkout":
                        checkout(uuid, args);
                        break;
                    case "branch":
                        branch(uuid, args);
                        break;
                    case "rm-branch":
                        rmBranch(uuid, args);
                        break;
                    case "reset":
                        reset(uuid, args);
                        break;
                    case "merge":
                        result.add(merge(uuid, args));
                        break;
                    /*
                    case "add-remote":
                        addRemote(uuid, args);
                        break;
                    case "rm-remote":
                        rmRemote(uuid, args);
                        break;
                    case "push":
                        push(uuid, args);
                        break;
                    case "fetch":
                        fetch(uuid, args);
                        break;
                    case "pull":
                        pull(uuid, args);
                        break;
                     */

                    default:
                        result.add("No command with that name exists.");
                        break;
                }
            } catch (GitletException e) {
                result.add(e.getMessage());
            }
        }
        return result;
    }

    private static void init(UUID uuid, String... args) {
        rightArgs(1, args);
        _gitdao.initRepo(uuid);
        _gitdao.getRepo(uuid).init();
    }

    private static void add(UUID uuid,String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.add(args[1]);
    }

    private static void rm(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.rm(args[1]);
    }
    private static void commit(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.commit(args[1]);
    }
    private static String log(UUID uuid, String... args) {
        rightArgs(1, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        return r.log();
    }

    private static String globalLog(UUID uuid, String... args) {
        rightArgs(1, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        return r.globalLog();
    }

    private static String find(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        return r.find(args[1]);
    }
    private static String status(UUID uuid, String... args) {
        rightArgs(1, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        return r.status();
    }
    private static void checkout(UUID uuid, String... args) {
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        if (args.length == 2) {
            r.checkBranch(args[1]);
            //checkout branch
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                throw new GitletException("Incorrect operands");
            } else {
                r.checkout(r.getHead(), args[2]);
            }
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                throw new GitletException("Incorrect operands");
            } else {
                r.checkout(args[1], args[3]);
            }
        } else {
            throw new GitletException("Incorrect operands");
        }
    }
    private static void branch(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.branch(args[1]);
    }
    private static void rmBranch(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.rmBranch(args[1]);
    }

    private static void reset(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.reset(args[1]);
    }

    private static String merge(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        return r.merge(args[1]);
    }

    private static void addRemote(UUID uuid, String... args) {
        rightArgs(3, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.addRemote(args[1], args[2]);
    }

    private static void rmRemote(UUID uuid, String... args) {
        rightArgs(2, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.rmRemote(args[1]);
    }

    private static void push(UUID uuid, String... args) {
        rightArgs(3, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.push(args[1], args[2]);
    }

    private static void fetch(UUID uuid, String... args) {
        rightArgs(3, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.fetch(args[1], args[2]);
    }

    private static void pull(UUID uuid, String... args) {
        rightArgs(3, args);
        alreadyInit(uuid);
        Repo r = _gitdao.getRepo(uuid);
        r.pull(args[1], args[2]);
    }

    /**
     * Returns if .gitlet has been initialized already.
     */
    private static void alreadyInit(UUID uuid) {
        if (_gitdao.getRepoMap().containsKey(uuid)) {
            if (_gitdao.getRepoMap().get(uuid) != null) {
                return;
            }
        }
        throw new GitletException("Not in an initialized Gitlet directory.");

    }

    private static void rightArgs(int correct, String... args) {
        if (args.length != correct) {
            throw new GitletException("Incorrect operands");
        }
    }

    /**
     * Helper function to print errors.
     * @param msg The error message.
     */
    protected static void printError(String msg) {
        System.out.println(msg);
    }

    private static String DIR = "src/main/java/com/gitlet/gitVisual/dao/data/";

}
