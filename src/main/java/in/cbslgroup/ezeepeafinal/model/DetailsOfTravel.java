package in.cbslgroup.ezeepeafinal.model;

public class DetailsOfTravel {

    public DetailsOfTravel(String sno, String dateOfTravel, String mode, String destFrom, String destTo, String mClass, String remarks, String advReq) {
        this.sno = sno;
        this.dateOfTravel = dateOfTravel;
        this.mode = mode;
        this.destFrom = destFrom;
        this.destTo = destTo;
        this.mClass = mClass;
        this.remarks = remarks;
        this.advReq = advReq;
    }

    public DetailsOfTravel(String sno, String dateOfTravel, String mode, String destFrom, String destTo, String mClass, String remarks, String advReq,String travelId,String sancAmt,boolean isSanctioned) {
        this.sno = sno;
        this.dateOfTravel = dateOfTravel;
        this.mode = mode;
        this.destFrom = destFrom;
        this.destTo = destTo;
        this.mClass = mClass;
        this.remarks = remarks;
        this.advReq = advReq;
        this.travelId = travelId;
        this.sancAmt = sancAmt;
        this.isSanctioned = isSanctioned;

    }

    private String sno;
    private String dateOfTravel;
    private String mode;
    private String destFrom;
    private String destTo;
    private String mClass;
    private String remarks;
    private String advReq;

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    private String travelId;

    public void setSancAmt(String sancAmt) {
        this.sancAmt = sancAmt;
    }

    public String getSancAmt() {
        return sancAmt;
    }

    private String sancAmt;

    public Boolean getSanctioned() {
        return isSanctioned;
    }

    public void setSanctioned(Boolean sanctioned) {
        isSanctioned = sanctioned;
    }

    private Boolean isSanctioned = false;


    @Override
    public String toString() {
        return "DetailsOfTravel{" +
                "sno='" + sno + '\'' +
                ", dateOfTravel='" + dateOfTravel + '\'' +
                ", mode='" + mode + '\'' +
                ", destFrom='" + destFrom + '\'' +
                ", destTo='" + destTo + '\'' +
                ", mClass='" + mClass + '\'' +
                ", remarks='" + remarks + '\'' +
                ", advReq='" + advReq + '\'' +
                '}';
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDestFrom() {
        return destFrom;
    }

    public void setDestFrom(String destFrom) {
        this.destFrom = destFrom;
    }

    public String getDestTo() {
        return destTo;
    }

    public void setDestTo(String destTo) {
        this.destTo = destTo;
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

    public String getAdvReq() {
        return advReq;
    }

    public void setAdvReq(String advReq) {
        this.advReq = advReq;
    }



}
