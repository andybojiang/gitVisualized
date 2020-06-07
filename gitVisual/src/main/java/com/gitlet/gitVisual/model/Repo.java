package com.gitlet.gitVisual.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Repo extends Gitent{
    public Repo(File sys, File gitlet, File blobs, File commits, File stage) {
        _systemDir = sys;
        _gitlet = gitlet;
        _blobDir = blobs;
        _commitDir = commits;
        _stageDir = stage;
        _branches = new HashMap<String, String>();
        _stage = new HashMap<String, String>();
        _remove = new ArrayList<String>();
        _remote = new HashMap<String, File>();
    }

    /**
     * Initializes the .gitlet repository with a first commit, which is
     * also set as the head and master branch.
     * Always assume that this is called the first time.
     */
    public void init() {
        Commit first = new Commit();
        _head = first.getEncryption();
        branch("master");
        _currentBranch = "master";
        first.toFile(_commitDir);
        this.toFile(_gitlet, "repository");
    }

    /**
     * Adds a copy of the file as it currently exists to the staging area.
     * If the file does not exist, print error msg.
     * @param filename The name of the file being added.
     */
    public void add(String filename) {
        File toAdd = new File(_systemDir.getPath() + "/" + filename);
        if (toAdd.exists()) {
            Blob b = new Blob(toAdd);
            if (getCommit(_head).containsBlob(b)) {
                _stage.remove(b.getName());
            } else {
                _stage.put(b.getName(), b.getEncryption());
                b.toFile(_stageDir);
            }
            _remove.remove(b.getName());
        } else {
            throw new GitletException("File does not exist.");
        }
        this.toFile(_gitlet, "repository");
    }

    /**
     * Unstage the file if it is currently staged for addition.
     * f the file is tracked in the current commit, stage it for removal and
     * remove the file from the working directory if the user has not already
     * done so (do not remove it unless it is tracked in the current commit).
     * If the file does not exist, print error msg.
     * @param filename The name of the file being added.
     */
    public void rm(String filename) {
        boolean error = true;
        if (_stage.containsKey(filename)) {

            File stagedF = new File(_stageDir.getPath() + "/" + _stage.get(filename));
            stagedF.delete();
            _stage.remove(filename);
            error = false;
        }
        if (getCommit(_head).containsBlob(filename)) {
            _remove.add(filename);
            File f = new File(_systemDir.getPath() + "/" + filename);
            Utils.restrictedDelete(f);
            error = false;
        }
        if (error) {
            throw new GitletException("No reason to remove the file.");
        }
        this.toFile(_gitlet, "repository");
    }
    /**
     * Saves a snapshot of certain files in the current commit and staging area
     * so they can be restored at a later time, creating a new commit.
     * @param msg The commit message.
     */
    public void commit(String msg) {
        if (msg.isEmpty()) {
            throw new GitletException("Please enter a commit message.");
        }
        if (_stage.isEmpty() && _remove.isEmpty()) {
            throw new GitletException("No changes added to the commit.");
        } else {
            Commit head = getCommit(_head);
            Commit newCommit = new Commit(head, msg, _stage, _remove);
            newCommit.toFile(_commitDir);
            for (String blobName : Utils.plainFilenamesIn(_stageDir)) {
                File f = new File (_stageDir.getPath() + "/" + blobName);
                Blob b = (Blob)Gitent.fromFile(f);
                b.toFile(_blobDir);
                f.delete();
            }

            _stage.clear();
            _remove.clear();
            _head = newCommit.getEncryption();
            _branches.put(_currentBranch, _head);
        }
        this.toFile(_gitlet, "repository");
    }

    /**
     * Prints log messages starting with the current head commit.
     */
    public String log() {
        return logHelper(getCommit(_head));
    }
    /**
     * Recursively prints log messages for every first parent of a commit.
     */
    private String logHelper(Commit c) {

        String log =  "===\n" + "commit " + c.getEncryption()
                + "\n";
        if (c.secondParent() != null) {
            log += "Merge: " + c.firstParent().substring(0, 7) + " " + c.secondParent().substring(0, 7) + "\n";
        }
        log += "Date: " + c.time() + "\n" + c.getMessage() + "\n";

        if (c.firstParent() != null) {
            log += logHelper(getCommit(c.firstParent()));
        }

        return log;
    }
    /**
     * Like log, except displays information about all commits ever made. The order of the commits does not matter.
     */
    public String globalLog() {
        String result = "";
        for (String commitCode : _branches.values()) {
            Commit c = getCommit(commitCode);
            result += logHelper(c);
        }
        return result;
    }
    /**
     * Prints out the ids of all commits that have the given commit message, one per line.
     */
    public String find(String msg) {
        boolean found = false;
        String result = "";
        for (String commitCode : Utils.plainFilenamesIn(_commitDir)) {
            Commit c = getCommit(commitCode);
            if (c.getMessage().equals(msg)) {
                found = true;
                result += c.getEncryption() + "\n";
            }
        }
        if (!found) {
            throw new GitletException("Found no commit with that message.");
        }
        return result;
    }
    /**
     * Displays what branches currently exist, and marks the current branch with a *.
     */
    public String status() {
        Commit head = getCommit(_head);
        String result = "";
        result += "=== Branches ===" + "\n";
        ArrayList<String> keyset = new ArrayList<String>(_branches.keySet());
        Collections.sort(keyset);
        for (String br : keyset) {
            if (br.equals(_currentBranch)) {
                result += "*" + br + "\n";
            } else {
                result += br + "\n";
            }
        }
        result += "\n";
        result += ("=== Staged Files ===") + "\n";
        ArrayList<String> stagedset = new ArrayList<String>(_stage.keySet());
        Collections.sort(stagedset);
        for (String staged : stagedset) {
            result += staged + "\n";
        }
        result += "\n";
        result += "=== Removed Files ==="+ "\n";
        ArrayList<String> copyRemove = new ArrayList<String>(_remove);
        Collections.sort(copyRemove);
        for (String removed : copyRemove) {
            result += (removed) + "\n";
        }
        result += "\n";
        result +=("=== Modifications Not Staged For Commit ===") + "\n";
        HashMap<String, String> modifiedNotStatged = new HashMap<String, String>();
        for (String f : Utils.plainFilenamesIn(_systemDir)) {
            Blob b = new Blob(new File(_systemDir + "/" + f));
            if (head.containsBlob(f) && !b.getEncryption().equals(head.getBlobKey(f)) && !_stage.containsKey(f)) {
                modifiedNotStatged.put(f, " (modified)");
            } else if (_stage.containsKey(f) && !b.getEncryption().equals(_stage.get(f))) {
                modifiedNotStatged.put(f, " (modified)");
            }
        }
        for (String f : _stage.keySet()) {
            if (!Utils.plainFilenamesIn(_systemDir).contains(f)) {
                modifiedNotStatged.put(f, " (deleted)");
            }
        }
        for (String f : head.getBlobs().keySet()) {
            if (!Utils.plainFilenamesIn(_systemDir).contains(f) && !_remove.contains(f)) {
                modifiedNotStatged.put(f, " (deleted)");
            }
        }
        for (String f : modifiedNotStatged.keySet()) {
            result += (f + modifiedNotStatged.get(f)) + "\n";
        }
        result += "\n";
        result += ("=== Untracked Files ===") + "\n";
        List<String> systemFiles = Utils.plainFilenamesIn(_systemDir);
        for (String f : systemFiles) {
            if (!_stage.containsKey(f) && !head.containsBlob(f)) {
                result += (f) + "\n";
            }
        }
        result += "\n";
        return result;
    }

    /**
     * General checkout function called. Finds the commit refered to by ID and updates file with
     * given name in the system directory.
     * @param abbrevID Encryption hashcode of the commit.
     * @param fileName Name of file.
     */
    public void checkout(String abbrevID, String fileName) {
        String fullID = matchCommit(abbrevID);
        Commit c = getCommit(fullID);
        checkout(c, fileName);
    }


    private void checkout(Commit c, String fileName) {
        try {
            Blob b = getBlob(c.getBlobKey(fileName));
            b.saveBlob(_systemDir);
        } catch(GitletException e) {
            throw new GitletException("File does not exist in that commit.");
        }
    }

    /**
     * Helper fucntion that matches a single commit to a substring of its beginning.
     * @param beginning Substring of a commit ID.
     */
    private String matchCommit(String beginning) {
        List<String> commits = Utils.plainFilenamesIn(_commitDir);
        ArrayList<String> matched = new ArrayList<String>();
        Pattern commitP = Pattern.compile(String.format("%s.*", beginning));
        for (String c : commits) {
            Matcher mat = commitP.matcher(c);
            if (mat.matches()) {
                matched.add(c);
            }
        }
        if (matched.size() == 1) {
            return matched.get(0);
        } else if (matched.size() > 1) {
            throw new GitletException("More than 1 matched commits.");
        } else {
            throw new GitletException("No commit with that id exists.");
        }
    }

    /**
     * Checks out all the files in a given branch.
     * @param branchName Name of branch.
     */
    public void checkBranch(String branchName) {

        if (!_branches.containsKey(branchName)) {
            throw new GitletException("No such branch exists.");
        } else if (branchName.equals(_currentBranch)) {
            throw new GitletException("No need to checkout the current branch.");
        }
        String branchCommitID = _branches.get(branchName);
        Commit branchCommit = getCommit(branchCommitID);
        if (riskyFile(getCommit(_head), branchCommit)) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        } else {

            checkoutAll(branchCommit);
            _currentBranch = branchName;
            _head = _branches.get(_currentBranch);
        }
        this.toFile(_gitlet, "repository");
    }

    /**
     * Checkout all blobs inside a commit c and clears the staging area.
     */
    private void checkoutAll(Commit c) {
        Commit head = getCommit(_head);
        for (String file : head.getBlobs().keySet()) {
            if (!c.containsBlob(file)) {
                File gone = new File(_systemDir.getPath() + "/" + file);
                Utils.restrictedDelete(gone);
            }
        }
        for (String file : c.getBlobs().keySet()) {
            checkout(c, file);
        }
        _stage.clear();
        _remove.clear();
        this.toFile(_gitlet, "repository");
    }

    /**
     * Returns if there is an untracked file in the system directory by a given commit.
     * @param c Given commit object.
     */
    private boolean allFilesTracked(Commit c) {
        for (String filename : Utils.plainFilenamesIn(_systemDir)) {
            if (!c.containsBlob(filename)) {
                System.out.println(filename);
                return false;
            }
        }
        return true;
    }

    private boolean riskyFile(Commit curr, Commit other) {
        for (String filename : Utils.plainFilenamesIn(_systemDir)) {
            if (!curr.containsBlob(filename) && other.containsBlob(filename)) {
                return true;
            }
        }
        return false;
    }

    public void reset(String abbrevID) {
        String fullID = matchCommit(abbrevID);
        Commit c = getCommit(fullID);
        if (riskyFile(getCommit(_head), c)) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        checkoutAll(c);
        _branches.put(_currentBranch, fullID);
        _head = _branches.get(_currentBranch);
        this.toFile(_gitlet, "repository");
    }

    /**
     * Creates a new branch with the given name, and points it at the current head node.
     * @param branchName The name of the new branch.
     */
    public void branch(String branchName) {
        if (_branches.containsKey(branchName)) {
            throw new GitletException("A branch with that name already exists.");
        } else {
            _branches.put(branchName, _head);
        }
        this.toFile(_gitlet, "repository");
    }

    /**
     * Deletes the branch with the given name.
     * @param branchName The name of the removed branch.
     */
    public void rmBranch(String branchName) {
        if (_branches.containsKey(branchName)) {
            if (_branches.get(branchName).equals( _head)) {
                throw new GitletException("Cannot remove the current branch.");
            } else {
                _branches.remove(branchName);
            }
        } else {
            throw new GitletException("A branch with that name does not exist.");
        }
        this.toFile(_gitlet, "repository");
    }

    /**
     * Merges a given branch with the current branch.
     */
    public String merge(String branchName) {
        String result = "";
        if (!_branches.containsKey(branchName)) {
            throw new GitletException("A branch with that name does not exist.");
        } else if (branchName.equals(_currentBranch)) {
            throw new GitletException("Cannot merge a branch with itself.");
        }
        Commit current = getCommit(getHead());
        Commit other = getCommit(_branches.get(branchName));
        if (!_stage.isEmpty() || !_remove.isEmpty()) {
            throw new GitletException("You have uncommitted changes.");
        } else if (riskyFile(current, other)) {
            throw new GitletException("There is an untracked file in the way; delete it, or add and commit it first.");
        }
        Commit split = findSplit(current, other);
        if (split.equals(other)) {
            result += ("Given branch is an ancestor of the current branch.") + "\n";
            System.exit(0);
        } else if (split.equals(current)) {
            checkBranch(branchName);
            result += ("Current branch fast-forwarded.") + "\n";
            System.exit(0);
        }

        ArrayList<String> uniqueBlobName = new ArrayList<String>();
        for (String blob : current.getBlobs().keySet()) {
            if (!uniqueBlobName.contains(blob)) {
                uniqueBlobName.add(blob);
            }
        }
        for (String blob : other.getBlobs().keySet()) {
            if (!uniqueBlobName.contains(blob)) {
                uniqueBlobName.add(blob);
            }
        }
        for (String blob : split.getBlobs().keySet()) {
            if (!uniqueBlobName.contains(blob)) {
                uniqueBlobName.add(blob);
            }
        }
        boolean conflict = false;
        for (String blobName : uniqueBlobName) {
            Blob currblob, otherblob, splitblob;
            currblob = otherblob = splitblob = new Blob();
            if (current.containsBlob(blobName)) {
                currblob = getBlob(current.getBlobKey(blobName));
            }
            if (other.containsBlob(blobName)) {
                otherblob = getBlob(other.getBlobKey(blobName));
            }
            if (split.containsBlob(blobName)) {
                splitblob = getBlob(split.getBlobKey(blobName));
            }
            if (mergeHelper(blobName, splitblob, currblob, otherblob, other)) {
                conflict = true;
            }
        }
        if (conflict) {
            result += ("Encountered a merge conflict.") + "\n";
        }
        mergeCommit(String.format("Merged %s into %s.", branchName, _currentBranch), current, other);
        this.toFile(_gitlet, "repository");
        return result;
    }

    private void mergeCommit(String msg, Commit curr, Commit other) {
        Commit newCommit = new Commit(curr, other, msg, _stage, _remove);
        newCommit.toFile(_commitDir);
        for (String blobName : Utils.plainFilenamesIn(_stageDir)) {
            File f = new File (_stageDir.getPath() + "/" + blobName);
            Blob b = (Blob)Gitent.fromFile(f);
            b.toFile(_blobDir);
            f.delete();
        }

        _stage.clear();
        _remove.clear();
        _head = newCommit.getEncryption();
        _branches.put(_currentBranch, _head);
    }

    /**
     * Applies merge to the current working directory.
     * @return if there is a merge conflict.
     */
    private boolean mergeHelper(String fname, Blob split, Blob curr, Blob other, Commit otherC) {
        if (split.equals(curr) && !split.equals(other)) {
            if (other.isEmpty()) {
                rm(fname);
            } else {
                checkout(otherC.getEncryption(), fname);
                add(fname);
            }
        } else if (!split.equals(curr) && !split.equals(other) && !curr.equals(other)) {
            File merged = new File(_systemDir + "/" + fname);
            if(other.isEmpty()) {
                Utils.writeContents(merged, "<<<<<<< HEAD\n", curr.getContent().trim(), "\n=======\n", ">>>>>>>\n");
            } else {
                Utils.writeContents(merged, "<<<<<<< HEAD\n", curr.getContent().trim(), "\n=======\n", other.getContent().trim(), "\n>>>>>>>\n");
            }
            add(fname);
            return true;
        }
        return false;
    }

    /**
     * Returns the latest common ancestor of 2 commits on the commit tree.
     */
    public Commit findSplit(Commit to, Commit from) {
        ArrayList<String> commonAncestors = to.ancestors();
        commonAncestors.retainAll(from.ancestors());
        ArrayList<Commit> latestCA = new ArrayList<Commit>();
        for (String CA : commonAncestors) {
            Commit temp = getCommit(CA);
            if (!temp.ancestors().retainAll(commonAncestors)) {
                latestCA.add(temp);
            }
        }
        Commit result = latestCA.get(0);
        int min = to.steps(latestCA.get(0));
        for (Commit c : latestCA) {
            if (to.steps(c) < min) {
                result = c;
                min = to.steps(c);
            }
        }
        return result;
    }

    /**
     * Adds a new remote .gitlet directory.
     * @param name name of the remote path.
     * @param path path of the .gitlet directory.
     */
    public void addRemote (String name, String path) {
        if (_remote.containsKey(name)) {
            throw new GitletException("A remote with that name already exists.");
        }
        File remote = new File(path);
        _remote.put(name, remote);
        this.toFile(_gitlet, "repository");
    }

    /**
     * Removes an existing remote directory.
     * @param name name of the remote path.
     */
    public void rmRemote (String name) {
        if (!_remote.containsKey(name)) {
            throw new GitletException("A remote with that name does not exist.");
        }
        _remote.remove(name);
        this.toFile(_gitlet, "repository");
    }
    /**
     * Attempts to append the current branch's commits to the end
     * of the given branch at the given remote
     * @param remote name of the remote repository
     * @param branch name of the remote branch name
     */
    public void push(String remote, String branch) {
        File remoteRepoF;
        File remoteCommitF;
        File remoteBlobF;
        if (_remote.containsKey(remote)) {
            remoteRepoF = new File(_remote.get(remote).getPath() + "/" + "repository");
            remoteCommitF = new File(_remote.get(remote).getPath() + "/" + "commits");
            remoteBlobF = new File(_remote.get(remote).getPath() + "/" + "blobs");
        } else {
            throw new GitletException("Remote directory not found.");
        }
        if (!remoteRepoF.exists()) {
            throw new GitletException("Remote directory not found.");
        }
        Repo remoteRepo = (Repo)Gitent.fromFile(remoteRepoF);
        remoteRepo._commitDir = new File(_remote.get(remote).getAbsoluteFile() + "/" + "commits");
        remoteRepo._blobDir = new File(_remote.get(remote).getAbsoluteFile() + "/" + "blobs");
        Commit remoteBHead = remoteRepo.getCommit(remoteRepo._branches.get(branch));
        Commit currHead = getCommit(getHead());

        if (currHead.ancestors().contains(remoteBHead.getEncryption())) {
            for (String commitID : currHead.ancestors()) {
                if (!remoteBHead.ancestors().contains(commitID)) {
                    Commit curr = getCommit(commitID);
                    curr.toFile(remoteCommitF);
                    for (String blobID : curr.getBlobs().keySet()) {
                        Blob b = getBlob(curr.getBlobKey(blobID));
                        b.toFile(remoteBlobF);
                    }
                }
            }
            remoteRepo._branches.put(branch, currHead.getEncryption());
            remoteRepo.reset(currHead.getEncryption());
        } else {
            throw new GitletException("Please pull down remote changes before pushing.");
        }
        remoteRepo.toFile(_remote.get(remote), "repository");
        this.toFile(_gitlet, "repository");
    }

    public void fetch (String remote, String branch) {
        File remoteRepoF = new File("dummy");
        if (_remote.containsKey(remote)) {
            try {
                remoteRepoF = new File(_remote.get(remote).getCanonicalPath() + "/" + "repository");

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            throw new GitletException("Remote directory not found.");
        }
        if (!remoteRepoF.exists()) {
            throw new GitletException("Remote directory not found.");
        }
        Repo remoteRepo = Utils.readObject(remoteRepoF.getAbsoluteFile(), Repo.class);
        remoteRepo._commitDir = new File(_remote.get(remote).getAbsoluteFile() + "/" + "commits");
        remoteRepo._blobDir = new File(_remote.get(remote).getAbsoluteFile() + "/" + "blobs");

        if (!remoteRepo._branches.containsKey(branch)) {
            throw new GitletException("That remote does not have that branch.");
        }

        Commit remoteBHead = remoteRepo.getCommit(remoteRepo._branches.get(branch));

        for (String commitID : remoteBHead.ancestors()) {

            Commit curr = remoteRepo.getCommit(commitID);
            curr.toFile(_commitDir);
            for (String blobID : curr.getBlobs().keySet()) {
                Blob b = remoteRepo.getBlob(curr.getBlobs().get(blobID));
                b.toFile(_blobDir);
            }
        }
        _branches.put(remote + "/" + branch, remoteBHead.getEncryption());
        this.toFile(_gitlet, "repository");
    }
    
    public void pull(String remote, String branch) {
        fetch(remote, branch);
        merge(remote + "/" + branch);
        this.toFile(_gitlet, "repository");
    }

    public String getHead() {
        return _head;
    }

    /**
     * Returns a blob object based on its encryption.
     * Throws GitletException if commit does not exist.
     */
    private Blob getBlob(String encryption) {
        File cfile = new File(_blobDir.getPath() + "/" + encryption);
        if (cfile.exists()) {
            return (Blob)Gitent.fromFile(cfile);
        } else {
            throw new GitletException("Blob encryption does not exist");
        }
    }

    /**
     * Returns a commit object based on its encryption.
     * Throws GitletException if commit does not exist.
     */
    public Commit getCommit(String encryption) {
        File cfile = new File(this._commitDir.getAbsolutePath() + "/" + encryption);
        if (cfile.exists()) {
            return (Commit)Gitent.fromFile(cfile);
        } else {
            throw new GitletException("No commit with that id exists.");
        }
    }

    public HashMap<String, String> get_stage() {
        return _stage;
    }

    /**
     * Helper function to print messages.
     * @param msg The message.
     */
    protected void printMsg(String msg) {
        System.out.println(msg);
    }

    /** Directory where files are tracked.*/
    protected File _systemDir;
    /** Directory of Gitlet.*/
    protected File _gitlet;
    /** Directory of blobs.*/
    protected File _blobDir;
    /** Directory of commits.*/
    protected File _commitDir;
    /** Directory where blobs are staged.*/
    protected File _stageDir;
    /** Name of the current branch the repository is on. Should ALWAYS be the same commit as head*/
    protected String _currentBranch;
    /** Stores the _encryption of the HEAD commit.*/
    protected String _head;
    /**Maps the name of the branch to the encryption of its corresponding commit.*/
    protected HashMap<String, String> _branches;



    /** Represents the staging area. Maps _name of an added blob to its _encryption.*/
    protected HashMap<String, String> _stage;
    /** Represents the removing area. Contains names of files to be removed.*/
    protected ArrayList<String> _remove;
    /** All added remote directories.*/
    protected HashMap<String, File> _remote;
}
