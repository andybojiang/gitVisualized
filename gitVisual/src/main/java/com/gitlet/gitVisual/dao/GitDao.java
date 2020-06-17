package com.gitlet.gitVisual.dao;

import com.gitlet.gitVisual.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.*;

@Repository("gitDao")
public class GitDao {



    /**
     * Reads files in a user's system and convert them to "datafile" objects that describe their status
     * within the git system.
     * @param uuid specifies the user.
     * @return List of datafiles.
     */
    public List<DataFile> getFileStructure(UUID uuid) {
        File user = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + uuid.toString());
        List<String> files = Utils.plainFilenamesIn(user);
        Repo r = _repos.get(uuid);
        List<DataFile> result = new ArrayList<>();
        for (String fname : files) {
            DataFile df = new DataFile(fname, Utils.readContentsAsString(new File(user.getPath() + "/" + fname)));
            if (r != null && r.get_stage().containsKey(fname)) {
                df.setStatus(1);

            } else if (r != null && r.getCommit(r.getHead()).containsBlob(fname)) {
                df.setStatus(2);

            } else {
                df.setStatus(0);
            }
            result.add(df);
        }
        return result;
    }


    public void newUser(UUID id) {
        File user = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + id.toString());
        user.mkdir();
        _repos.put(id, null);
    }

    public void removeUser(UUID id) {
        File user = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + id.toString());
        //FIXME: need to be able to delete folder with files in it recursively
        deleteDirectory(user);
        _repos.remove(id);
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public void addFile(UUID uuid, String name, String contents) {
        File f = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + uuid.toString() + "/" + name);
        if (f.exists()) {
            throw new GitletException("A file with this name already exists.");
        } else if (contents.equals("")) {
            throw new GitletException("A file cannot have no content.");
        } else if (name.equals("")) {
            throw new GitletException("A file cannot have no name.");
        } else {
            try {
                f.createNewFile();
            } catch (java.io.IOException e) {
                throw new GitletException(e.getMessage());
            }
            Utils.writeContents(f, contents);

        }
    }

    public void removeFile(UUID uuid, String name) {
        File f = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + uuid.toString() + "/" + name);
        if (f.exists()) {
            f.delete();
        }
    }


    public void initRepo(UUID id) {
        File system = new File("src/main/java/com/gitlet/gitVisual/dao/data/" + id.toString());
        File gitlet = new File(system.getPath() + "/.gitlet");
        if (gitlet.exists()) {
            throw new GitletException("A Gitlet version-control system already exists in the current directory.");
        } else {
            gitlet.mkdir();
            File blobs = new File(gitlet.getPath() + "/blobs");
            blobs.mkdir();
            File commits = new File(gitlet.getPath() + "/commits");
            commits.mkdir();
            File stage = new File(gitlet.getPath() + "/stage");
            stage.mkdir();
            Repo r = new Repo(system, gitlet, blobs, commits, stage);
            _repos.put(id, r);
        }
    }

    /**
     * Getter function for in-memory repo.
     * @return Repo object.
     */
    public Repo getRepo(UUID id) {
        return _repos.get(id);
    }

    public Map<UUID, Repo> getRepoMap() {
        return _repos;
    }

    /**In-memory repository.*/
    private Map<UUID, Repo> _repos = new HashMap<>();
}
