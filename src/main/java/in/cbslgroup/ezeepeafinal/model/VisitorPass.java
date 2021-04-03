package in.cbslgroup.ezeepeafinal.model;

public class VisitorPass{

	private String id;
	private String visitPurpose;
	private String intime;
	private String actionby;
	private String departmentName;
	private String deptId;
	private String outtime;
	private String meetingwith;
	private String remark;
	private String mobileno;
	private String pic;
	private String visitorName;
	private String noofvisitors;
	private String companyname;
	private String addOutTime;
	private String passno;
	private String visitDate;
	private String employeename;
	private String updateMeetingStatus;
	private String status;


	public VisitorPass(String visitPurpose, String intime, String actionby, String departmentName, String deptId, String outtime, String meetingwith, String remark, String mobileno, String pic, String visitorName, String noofvisitors, String companyname, String addOutTime, String passno, String id, String visitDate, String employeename, String updateMeetingStatus, String status) {
		this.visitPurpose = visitPurpose;
		this.intime = intime;
		this.actionby = actionby;
		this.departmentName = departmentName;
		this.deptId = deptId;
		this.outtime = outtime;
		this.meetingwith = meetingwith;
		this.remark = remark;
		this.mobileno = mobileno;
		this.pic = pic;
		this.visitorName = visitorName;
		this.noofvisitors = noofvisitors;
		this.companyname = companyname;
		this.addOutTime = addOutTime;
		this.passno = passno;
		this.id = id;
		this.visitDate = visitDate;
		this.employeename = employeename;
		this.updateMeetingStatus = updateMeetingStatus;
		this.status = status;
	}



	public void setVisitPurpose(String visitPurpose){
		this.visitPurpose = visitPurpose;
	}

	public String getVisitPurpose(){
		return visitPurpose;
	}

	public void setIntime(String intime){
		this.intime = intime;
	}

	public String getIntime(){
		return intime;
	}

	public void setActionby(String actionby){
		this.actionby = actionby;
	}

	public String getActionby(){
		return actionby;
	}

	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public void setDeptId(String deptId){
		this.deptId = deptId;
	}

	public String getDeptId(){
		return deptId;
	}

	public void setOuttime(String outtime){
		this.outtime = outtime;
	}

	public String getOuttime(){
		return outtime;
	}

	public void setMeetingwith(String meetingwith){
		this.meetingwith = meetingwith;
	}

	public String getMeetingwith(){
		return meetingwith;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setMobileno(String mobileno){
		this.mobileno = mobileno;
	}

	public String getMobileno(){
		return mobileno;
	}

	public void setPic(String pic){
		this.pic = pic;
	}

	public String getPic(){
		return pic;
	}

	public void setVisitorName(String visitorName){
		this.visitorName = visitorName;
	}

	public String getVisitorName(){
		return visitorName;
	}

	public void setNoofvisitors(String noofvisitors){
		this.noofvisitors = noofvisitors;
	}

	public String getNoofvisitors(){
		return noofvisitors;
	}

	public void setCompanyname(String companyname){
		this.companyname = companyname;
	}

	public String getCompanyname(){
		return companyname;
	}

	public void setAddOutTime(String addOutTime){
		this.addOutTime = addOutTime;
	}

	public String getAddOutTime(){
		return addOutTime;
	}

	public void setPassno(String passno){
		this.passno = passno;
	}

	public String getPassno(){
		return passno;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setVisitDate(String visitDate){
		this.visitDate = visitDate;
	}

	public String getVisitDate(){
		return visitDate;
	}

	public void setEmployeename(String employeename){
		this.employeename = employeename;
	}

	public String getEmployeename(){
		return employeename;
	}

	public void setUpdateMeetingStatus(String updateMeetingStatus){
		this.updateMeetingStatus = updateMeetingStatus;
	}

	public String getUpdateMeetingStatus(){
		return updateMeetingStatus;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
	public String toString(){
		return
				"ListItem{" +
						"visit_purpose = '" + visitPurpose + '\'' +
						",intime = '" + intime + '\'' +
						",actionby = '" + actionby + '\'' +
						",department_name = '" + departmentName + '\'' +
						",deptId = '" + deptId + '\'' +
						",outtime = '" + outtime + '\'' +
						",meetingwith = '" + meetingwith + '\'' +
						",remark = '" + remark + '\'' +
						",mobileno = '" + mobileno + '\'' +
						",pic = '" + pic + '\'' +
						",visitor_name = '" + visitorName + '\'' +
						",noofvisitors = '" + noofvisitors + '\'' +
						",companyname = '" + companyname + '\'' +
						",addOutTime = '" + addOutTime + '\'' +
						",passno = '" + passno + '\'' +
						",id = '" + id + '\'' +
						",visit_date = '" + visitDate + '\'' +
						",employeename = '" + employeename + '\'' +
						",update_meeting_status = '" + updateMeetingStatus + '\'' +
						",status = '" + status + '\'' +
						"}";
	}
}