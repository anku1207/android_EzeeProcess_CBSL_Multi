package in.cbslgroup.ezeepeafinal.model;

public class SharedWithMe {

    public SharedWithMe(String docid, String docname, String docsize, String noOfPages, String sharedBy, String sharedDate, String storageName, String docPath, String extension, String slid) {
        this.docid = docid;
        this.docname = docname;
        this.docsize = docsize;
        this.noOfPages = noOfPages;
        this.sharedBy = sharedBy;
        this.sharedDate = sharedDate;
        this.storageName = storageName;
        this.docPath = docPath;
        this.extension = extension;
        this.slid = slid;
    }

    String docid;
    String docname;
    String docsize;
    String noOfPages;
    String sharedBy;
    String sharedDate;
    String storageName;
    String docPath;
    String extension;

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    String slid;



    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocsize() {
        return docsize;
    }

    public void setDocsize(String docsize) {
        this.docsize = docsize;
    }

    public String getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(String noOfPages) {
        this.noOfPages = noOfPages;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(String sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }





}
