package com.gitlet.gitVisual.model;

import java.io.File;

/**
 * Blob object that represents the contents of a file.
 * @author Andy Jiang
 */

public class Blob extends Gitent {
    /**
     * Creates an empty blob object.
     */
    public Blob() {
        _empty = true;
        encrypt("empty blob");
    }
    /**
     * Creates a blob object from a given file.
     * @param f file given.
     */
    public Blob(final File f) {
        _contents = Utils.readContentsAsString(f);
        _name = f.getName();
        encrypt("blob", _name, _contents);
    }

    /**
     * Create or overwrite the file that contains the contents in blob.
     * @param dir a directory in which to write the new file.
     */
    public void saveBlob(final File dir) {
        File f = new File(dir.getPath() + "/" + this._name);
        Utils.writeContents(f, this._contents);
    }
    /**
     * Returns name of the file tracked by blob.
     */
    public String getName() {
        return _name;
    }
    /**
     * Returns content of the file tracked by blob.
     */
    public String getContent() {
        return _contents;
    }
    /**
     * Returns true if the blob is empty.
     */
    public Boolean isEmpty() {
        return _empty;
    }

    /**Content of file as a string.*/
    private String _contents;
    /**Content of file name as a string.*/
    private String _name;
    /**If a blob is a null blob.*/
    private Boolean _empty = false;
}
