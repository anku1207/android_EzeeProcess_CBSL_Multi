package in.cbslgroup.ezeepeafinal.model;

public class DetailsOfTravelExpenses {

    private String sno;
    private String mode;
    private String mClass;
    private String remarks;
    private String claimAmt;
    private String sancAmt;

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    private String travelId;
    boolean isSanctioned = false;

    public boolean isSanctioned() {
        return isSanctioned;
    }

    public void setSanctioned(boolean sanctioned) {
        isSanctioned = sanctioned;
    }

    //Departure
    private String deptDateOfTravel,deptPlace,deptTime;

    //Arrival
    private String arrvDateOfTravel,arrvPlace,arrvTime;


    public DetailsOfTravelExpenses(String sno, String mode, String mClass, String remarks, String claimAmt, String sancAmt, String deptDateOfTravel, String deptPlace, String deptTime, String arrvDateOfTravel, String arrvPlace, String arrvTime,Boolean isSanctioned) {

        this.sno = sno;
        this.mode = mode;
        this.mClass = mClass;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
        this.deptDateOfTravel = deptDateOfTravel;
        this.deptPlace = deptPlace;
        this.deptTime = deptTime;
        this.arrvDateOfTravel = arrvDateOfTravel;
        this.arrvPlace = arrvPlace;
        this.arrvTime = arrvTime;
        this.isSanctioned = isSanctioned;

    }

    public DetailsOfTravelExpenses(String sno, String mode, String mClass, String remarks, String claimAmt, String sancAmt, String deptDateOfTravel, String deptPlace, String deptTime, String arrvDateOfTravel, String arrvPlace, String arrvTime,Boolean isSanctioned,String travelId) {

        this.sno = sno;
        this.mode = mode;
        this.mClass = mClass;
        this.remarks = remarks;
        this.claimAmt = claimAmt;
        this.sancAmt = sancAmt;
        this.deptDateOfTravel = deptDateOfTravel;
        this.deptPlace = deptPlace;
        this.deptTime = deptTime;
        this.arrvDateOfTravel = arrvDateOfTravel;
        this.arrvPlace = arrvPlace;
        this.arrvTime = arrvTime;
        this.isSanctioned = isSanctioned;
        this.travelId = travelId;

    }



    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getmClass() {
        return mClass;
    }

    public void setmClass(String mClass) {
        this.mClass = mClass;
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

    public String getDeptDateOfTravel() {
        return deptDateOfTravel;
    }

    public void setDeptDateOfTravel(String deptDateOfTravel) {
        this.deptDateOfTravel = deptDateOfTravel;
    }

    public String getDeptPlace() {
        return deptPlace;
    }

    public void setDeptPlace(String deptPlace) {
        this.deptPlace = deptPlace;
    }

    public String getDeptTime() {
        return deptTime;
    }

    public void setDeptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    public String getArrvDateOfTravel() {
        return arrvDateOfTravel;
    }

    public void setArrvDateOfTravel(String arrvDateOfTravel) {
        this.arrvDateOfTravel = arrvDateOfTravel;
    }

    public String getArrvPlace() {
        return arrvPlace;
    }

    public void setArrvPlace(String arrvPlace) {
        this.arrvPlace = arrvPlace;
    }

    public String getArrvTime() {
        return arrvTime;
    }

    public void setArrvTime(String arrvTime) {
        this.arrvTime = arrvTime;
    }


}
