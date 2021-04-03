package in.cbslgroup.ezeepeafinal.model;

public class LocalConveyanceAndOnOutstationVisit {

    public LocalConveyanceAndOnOutstationVisit(String sno, String date, String time, String from, String to, String mode, String mClass, String remarks, String claimAmt, String sancAmt) {
        this.sno = sno;
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.mode = mode;
        this.mClass = mClass;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
    }

    public LocalConveyanceAndOnOutstationVisit(String sno, String date, String time, String from, String to, String mode, String mClass, String remarks, String claimAmt, String sancAmt, Boolean isSanctioned,String lcid) {
        this.sno = sno;
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.mode = mode;
        this.mClass = mClass;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
        this.isSanctioned = isSanctioned;
        this.lcId = lcid;
    }

    String sno;
    String date;
    String time;
    String from;
    String to;
    String mode;
    String mClass;
    String remarks;
    String claimAmt;
    String sancAmt;

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    String lcId;

    public Boolean getSanctioned() {
        return isSanctioned;
    }

    public void setSanctioned(Boolean sanctioned) {
        isSanctioned = sanctioned;
    }

    Boolean isSanctioned;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClaimAmt() {
        return claimAmt;
    }

    public void setClaimAmt(String claimAmt) {
        this.claimAmt = claimAmt;
    }

    public String getSancAmt() {
        return sancAmt;
    }

    public void setSancAmt(String sancAmt) {
        this.sancAmt = sancAmt;
    }


}
