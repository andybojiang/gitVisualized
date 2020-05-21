package com.gitlet.gitVisual.model;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Gitent implements Serializable {

    /**
     * Recovers a Gitent object from a file.
     * @param f file that contains a blob.
     */
    public static Gitent fromFile (File f) {
        Gitent g = Utils.readObject(f, Gitent.class);
        return g;
    }

    /**
     * Saves a Gitent object to a file for future use.
     */
    public void toFile(File dir) {
        toFile(dir, _encryption);
    }

    /**
     * Saves a Gitent object to a file for future use.
     */
    public void toFile(File dir, String name) {
        File f = new File(dir.getPath() + "/" + name);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException | ClassCastException excp) {
                throw new IllegalArgumentException(excp.getMessage());
            }
        }
        Utils.writeObject(f, this);
    }

    /**
     * Obtain an unique encryption key for this file.
     */
    public void encrypt(Object... vals) {
        _encryption = (Utils.sha1(vals));
    }

    public boolean equals(Gitent other) {
        return _encryption.equals(other._encryption);
    }

    public String getEncryption() {
        return _encryption;
    }

    /**Name of file contained in this blob.*/
    private String _encryption;
}
