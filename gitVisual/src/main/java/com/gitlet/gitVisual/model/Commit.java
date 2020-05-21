package com.gitlet.gitVisual.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a commit that tracks multiple files as blob encryption strings.
 * @author Andy Jiang
 */
public class Commit extends Gitent {
    /**
     * This constructor is only used for the very first commit used
     * in init. Time is arbitrary.
     */
    public Commit() {
        _message = "initial commit";
        LocalDateTime t = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        String zone = "America/Los_Angeles";
        _time = ZonedDateTime.of(t, ZoneId.of(zone)).format(form());
        _time = _time.replace(".", "");
        encrypt("commit", _message, _time);
        _blobs = new HashMap<>();
        _ancestors = new HashMap<>();
        _ancestors.put(getEncryption(), 0);
    }
    /**
     * Creating a commit normally - by using files added to the staging area.
     * @param p Current head commit as of calling.
     * @param m Commit message.
     * @param a Files in the staging area.
     * @param r Files in removing area.
     */
    public Commit(Commit p, String m, HashMap<String, String> a, List<String> r) {
        _message = m;
        _firstParent = p.getEncryption();
        String zone = "America/Los_Angeles";
        LocalDateTime t = LocalDateTime.now();
        _time = ZonedDateTime.of(t, ZoneId.of(zone)).format(form());
        _time = _time.replace(".", "");
        _blobs = p._blobs;
        if (a != null) {
            for (String b : a.keySet()) {
                _blobs.put(b, a.get(b));
            }
        }
        if (r != null) {
            for (String file : r) {
                if (containsBlob(file)) {
                    _blobs.remove(file);
                }
            }
        }

        encrypt("commit", _message,  _time);
        _ancestors = new HashMap<>();
        _ancestors.putAll(p._ancestors);
        _ancestors.replaceAll((k, v) -> v + 1);
        _ancestors.put(getEncryption(), 0);
    }

    public Commit(Commit parent, Commit parent2, String message, HashMap<String, String> addfiles, List<String> removefiles) {
        _message = message;
        _firstParent = parent.getEncryption();
        _secondParent = parent2.getEncryption();
        _time = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/Los_Angeles")).format(form());
        _time = _time.replace(".", "");
        _blobs = parent._blobs;
        if (addfiles != null) {
            for (String b : addfiles.keySet()) {
                _blobs.put(b, addfiles.get(b));
            }
        }
        if (addfiles != null) {
            for (String file : removefiles) {
                if (containsBlob(file)) {
                    _blobs.remove(file);
                }
            }
        }

        encrypt("commit", _message,  _time);
        _ancestors = new HashMap<>();
        _ancestors.putAll(parent._ancestors);
        _ancestors.putAll(parent2._ancestors);
        _ancestors.replaceAll((k, v) -> v + 1);
        _ancestors.put(getEncryption(), 0);
    }

    private static DateTimeFormatter form() {
        return DateTimeFormatter.ofPattern("E LLL d HH:mm:ss u Z");
    }
    /**
     * If this commit contains a given blob.
     * @param b The blob being checked.
     * @return Whether _blobs contains b.
     */
    public boolean containsBlob(Blob b) {
        if (_blobs.containsKey(b.getName())) {
            return b.getEncryption().equals(_blobs.get(b.getName()));
        }
        return false;
    }

    /**
     * If this commit contains a blob with a given name
     * @param name The name of the blob being checked.
     * @return Whether _blobs contains a blob with name.
     */
    public boolean containsBlob(String name) {
        return _blobs.containsKey(name);
    }

    public HashMap<String, String> getBlobs() {
        return  _blobs;
    }

    public String getBlobKey(String fileName) {
        return _blobs.get(fileName);
    }

    @Override
    public String toString() {
        return "===\n" + "commit " + getEncryption()
        + "\n" + "Date: " + _time + "\n" + _message + "\n";
    }

    public String getMessage() {
        return _message;
    }
    public String firstParent() {
        return _firstParent;
    }

    public String secondParent() {
        return _secondParent;
    }

    public String time() {
        return _time;
    }

    public ArrayList<String> ancestors() {
        return new ArrayList<>(_ancestors.keySet());
    }

    public int steps(String commitID) {
        return _ancestors.get(commitID);
    }

    public int steps(Commit c) {
        String encryption = c.getEncryption();
        return _ancestors.get(encryption);
    }

    /**Commit log code.*/
    private String _message;
    /**Time of creating commit.*/
    private String _time;
    /**Hashmap that matches file names to blob encryption.*/
    private HashMap<String, String> _blobs;
    /**Hashmap that contains ancestors and steps away from it.*/
    private HashMap<String, Integer> _ancestors;
    /**First parent of the commit.*/
    private String _firstParent;
    /**Second parent of the commit.*/
    private String _secondParent;

}
