package in.cbslgroup.ezeepeafinal.model;

public class TaskProcessDocs {

    public TaskProcessDocs(String docname, String docid, String docPath, String extension, String slid) {
        this.docname = docname;
        this.docid = docid;
        this.docPath = docPath;
        this.extension = extension;
        this.slid = slid;
    }

    String docname, docid;
    String docPath;
    String extension;

    public String getSlid() {
        return slid;
    }

    public void setSlid(String slid) {
        this.slid = slid;
    }

    String slid;


    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
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
