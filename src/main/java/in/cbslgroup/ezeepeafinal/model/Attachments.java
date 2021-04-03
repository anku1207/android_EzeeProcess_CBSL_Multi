package in.cbslgroup.ezeepeafinal.model;

public class Attachments {

    private String fileName,filePath,fileType,docid;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public Boolean getSanctioned() {
        return isSanctioned;
    }

    public void setSanctioned(Boolean sanctioned) {
        isSanctioned = sanctioned;
    }

    Boolean isSanctioned;

    public Attachments(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public Attachments(String fileName, String filePath, String fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public Attachments(String fileName, String filePath, String fileType,String docid,Boolean isSanctioned) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.docid = docid;
        this.isSanctioned = isSanctioned;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }



}
