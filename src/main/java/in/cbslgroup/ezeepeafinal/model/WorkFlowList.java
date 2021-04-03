package in.cbslgroup.ezeepeafinal.model;

public class WorkFlowList {

    String workFlowName;
    String workFlowId;
    String arf,cte;
    //manoj shakya 01-04-21
    String wftype;


    //manoj shakya 01-04-21
    public WorkFlowList(String workFlowName, String workFlowId, String arf, String cte ,String wftype) {
        this.workFlowName = workFlowName;
        this.workFlowId = workFlowId;
        this.arf = arf;
        this.cte = cte;
        this.wftype=wftype;

    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getArf() {
        return arf;
    }

    public void setArf(String arf) {
        this.arf = arf;
    }

    public String getCte() {
        return cte;
    }

    public void setCte(String cte) {
        this.cte = cte;
    }
    public String getWftype() {
        return wftype;
    }

    public void setWftype(String wftype) {
        this.wftype = wftype;
    }






}
