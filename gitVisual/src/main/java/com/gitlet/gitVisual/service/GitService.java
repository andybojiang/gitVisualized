package com.gitlet.gitVisual.service;

import com.gitlet.gitVisual.dao.GitDao;
import com.gitlet.gitVisual.model.Gitent;
import com.gitlet.gitVisual.model.GitletException;
import com.gitlet.gitVisual.model.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
//FIXME: page is still in progress
@Service
public class GitService {
    /**In-memory database.*/
    private static GitDao _gitdao;

    @Autowired
    public GitService(@Qualifier("gitDao") GitDao gitDao) {
        this._gitdao = gitDao;
    }

    public String process(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        try {
            switch (args[0]) {
                case "init":
                    init(args);
                    break;
                case "add":
                    add(args);
                    break;
                case "rm":
                    rm(args);
                    break;
                case "commit":
                    commit(args);
                    break;
                case "log":
                    log(args);
                    break;
                case "global-log":
                    globalLog(args);
                    break;
                case "find":
                    find(args);
                    break;
                case "status":
                    status(args);
                    break;
                case "checkout":
                    checkout(args);
                    break;
                case "branch":
                    branch(args);
                    break;
                case "rm-branch":
                    rmBranch(args);
                    break;
                case "reset":
                    reset(args);
                    break;
                case "merge":
                    merge(args);
                    break;
                case "add-remote":
                    addRemote(args);
                    break;
                case "rm-remote":
                    rmRemote(args);
                    break;
                case "push":
                    push(args);
                    break;
                case "fetch":
                    fetch(args);
                    break;
                case "pull":
                    pull(args);
                    break;
                default:
                    System.out.println("No command with that name exists.");
                    System.exit(0);
                    break;
            }
        } catch (GitletException e) {
            printError(e.getMessage());
        }
        return "Hello World";
    }

    private static void init(String... args) {
        rightArgs(1, args);
        //if system is already initialized
        File system = new File(System.getProperty("user.dir"));
        File gitlet = new File(".gitlet");
        if (gitlet.exists()) {
            printError("A Gitlet version-control system already exists in the current directory.");
        } else {
            gitlet.mkdir();
            File blobs = new File(gitlet.getPath() + "/blobs");
            blobs.mkdir();
            File commits = new File(gitlet.getPath() + "/commits");
            commits.mkdir();
            File stage = new File(gitlet.getPath() + "/stage");
            stage.mkdir();
            Repo repo = new Repo(system, gitlet, blobs, commits, stage);
            repo.init();
        }
    }

    private static void add(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.add(args[1]);
    }

    private static void rm(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.rm(args[1]);
    }
    private static void commit(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.commit(args[1]);
    }
    private static void log(String... args) {
        rightArgs(1, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.log();
    }

    private static void globalLog(String... args) {
        rightArgs(1, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.globalLog();
    }

    private static void find(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.find(args[1]);
    }
    private static void status(String... args) {
        rightArgs(1, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.status();
    }
    private static void checkout(String... args) {
        alreadyInit();
        Repo r = _gitdao.getRepo();
        if (args.length == 2) {
            r.checkBranch(args[1]);
            //checkout branch
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                printError("Incorrect operands");
                System.exit(0);
            } else {
                r.checkout(r.getHead(), args[2]);
            }
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                printError("Incorrect operands");
                System.exit(0);
            } else {
                r.checkout(args[1], args[3]);
            }
        } else {
            printError("Incorrect operands");
            System.exit(0);
        }
    }
    private static void branch(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.branch(args[1]);
    }
    private static void rmBranch(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.rmBranch(args[1]);
    }

    private static void reset(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.reset(args[1]);
    }

    private static void merge(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.merge(args[1]);
    }

    private static void addRemote(String... args) {
        rightArgs(3, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.addRemote(args[1], args[2]);
    }

    private static void rmRemote(String... args) {
        rightArgs(2, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.rmRemote(args[1]);
    }

    private static void push(String... args) {
        rightArgs(3, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.push(args[1], args[2]);
    }

    private static void fetch(String... args) {
        rightArgs(3, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.fetch(args[1], args[2]);
    }

    private static void pull(String... args) {
        rightArgs(3, args);
        alreadyInit();
        Repo r = _gitdao.getRepo();
        r.pull(args[1], args[2]);
    }

    /**
     * Returns if .gitlet has been initialized already.
     */
    private static void alreadyInit() {
        File gitlet = new File(".gitlet");
        if (!gitlet.exists()) {
            printError("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    private static void rightArgs(int correct, String... args) {
        if (args.length != correct) {
            printError("Incorrect operands");
            System.exit(0);
        }
    }

    /**
     * Helper function to print errors.
     * @param msg The error message.
     */
    protected static void printError(String msg) {
        System.out.println(msg);
    }



}
