package in.cbslgroup.ezeepeafinal.model;

import java.io.Serializable;


public class Foldername implements Serializable {


    String foldername;
    String blankfolder;
    String lockfolder;
    String rootlockfolder;


    public String getRootlockfolder() {
        return rootlockfolder;
    }

    public void setRootlockfolder(String rootlockfolder) {
        this.rootlockfolder = rootlockfolder;
    }



    public String getLockfolder() {
        return lockfolder;
    }

    public void setLockfolder(String lockfolder) {
        this.lockfolder = lockfolder;
    }



    public String getBlankfolder() {
        return blankfolder;
    }

    public void setBlankfolder(String blankfolder) {
        this.blankfolder = blankfolder;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

}
