package com.gitlet.gitVisual.model;
import ucb.junit.textui;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class RepoTest {
    File system = new File("/Users/yuyanzhang/Desktop/CS Miscellaneous/webdev/gitProject/repo/gitVisual/src/main/java/com/gitlet/gitVisual/model/repo");
    File gitlet = new File(system.getPath() + "/.gitlet");
    File blobs = new File(gitlet.getPath() + "/blobs");
    File commits = new File(gitlet.getPath() + "/commits");
    File stage = new File(gitlet.getPath() + "/stage");
    File repo = new File(gitlet.getPath() + "/repository");

    /** Initializes the repository, checking if the first commit is correct **/
    @Test
    public void initTest() {
        Repo r = new Repo(system, gitlet, blobs, commits, stage);
        r.init();
        assertEquals("Repo directory has been created", repo.exists(), true);
        assertEquals("Repo is on the master branch", r._currentBranch, "master");
        assertEquals("Branch maps current branch to head commit", r._branches.get(r._currentBranch), r._head);
        Commit c = r.getCommit(r.getHead());
        assertEquals("First commit messgae is correct", c.getMessage(), "initial commit");
    }

    @Test
    public void commitTest() {
        Repo r = new Repo(system, gitlet, blobs, commits, stage);
        r.init();
        r.add("hello.txt");
        Repo r1 = (Repo)Repo.fromFile(repo);
        r1.commit("adding hello.txt");

        Repo r2 = (Repo)Repo.fromFile(repo);
        r2.add("hello.txt");

        Commit c = r2.getCommit(r2.getHead());
        System.out.println(c);
        System.out.println(c.getBlobs());
        assertEquals(c.containsBlob(new Blob(new File(repo.getPath() + "/hello.txt"))), true);
    }
    @Test
    public void rmTest() {
        Repo r = (Repo)Repo.fromFile(repo);

        r.rm("hello.txt");
        Repo r1 = (Repo)Repo.fromFile(repo);
        r1.commit("removed hello.txt");
    }
    @Test
    public void statusTest() {
        Repo r = (Repo)Repo.fromFile(repo);

        System.out.println(r.log());
    }

}
