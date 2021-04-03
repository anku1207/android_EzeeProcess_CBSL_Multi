package in.cbslgroup.ezeepeafinal.model;

public class SharedFiles {


    String filename;
    String sharedTo;
    String sharedDate;
    String noOfPages;
    String storageName;
    String filepath;
    String fileExtension;
    String docid;
    String touserid;

    String slid;


    public SharedFiles(String filename, String sharedTo, String sharedDate, String noOfPages, String storageName, String filepath, String fileExtension, String docid, String touserid, String slid) {
        this.filename = filename;
        this.sharedTo = sharedTo;
        this.sharedDate = sharedDate;
        this.noOfPages = noOfPages;
        this.storageName = storageName;
        this.filepath = filepath;
        this.fileExtension = fileExtension;
        this.docid = docid;
        this.touserid = touserid;
        this.slid = slid;
    }

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public String getdocid() {
        return docid;
    }

    public void setdocid(String docid) {
        this.docid = docid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSharedTo() {
        return sharedTo;
    }

    public void setSharedTo(String sharedTo) {
        this.sharedTo = sharedTo;
    }

    public String getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(String sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }


}
