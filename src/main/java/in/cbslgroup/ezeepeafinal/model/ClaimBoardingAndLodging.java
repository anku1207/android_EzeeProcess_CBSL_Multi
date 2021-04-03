package in.cbslgroup.ezeepeafinal.model;

public class ClaimBoardingAndLodging {

    public ClaimBoardingAndLodging(String sno, String date, String boarding, String loadging, String remarks, String claimAmt, String sancAmt, Boolean isSanctioned,String blid) {
        this.sno = sno;
        this.date = date;
        this.boarding = boarding;
        this.loadging = loadging;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
        this.isSanctioned = isSanctioned;
        this.blid = blid;
    }

    String sno;
    String date;
    String boarding;
    String loadging;
    String remarks;
    String claimAmt;
    String sancAmt;

    public String getBlid() {
        return blid;
    }

    public void setBlid(String blid) {
        this.blid = blid;
    }

    String blid;

    public Boolean getSanctioned() {
        return isSanctioned;
    }

    public void setSanctioned(Boolean sanctioned) {
        isSanctioned = sanctioned;
    }

    Boolean isSanctioned = false;

    public ClaimBoardingAndLodging(String sno, String date, String boarding, String loadging, String remarks, String claimAmt, String sancAmt) {
        this.sno = sno;
        this.date = date;
        this.boarding = boarding;
        this.loadging = loadging;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
    }


    @Override
    public String toString() {
        return "ClaimBoardingAndLodging{" +
                "sno='" + sno + '\'' +
                ", date='" + date + '\'' +
                ", boarding='" + boarding + '\'' +
                ", loadging='" + loadging + '\'' +
                ", remarks='" + remarks + '\'' +
                ", claimAmt='" + claimAmt + '\'' +
                ", sancAmt='" + sancAmt + '\'' +
                '}';
    }


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

    public String getBoarding() {
        return boarding;
    }

    public void setBoarding(String boarding) {
        this.boarding = boarding;
    }

    public String getLoadging() {
        return loadging;
    }

    public void setLoadging(String loadging) {
        this.loadging = loadging;
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
