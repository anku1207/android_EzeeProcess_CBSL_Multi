package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SanctionClaimResponse {

	@SerializedName("msg")
	private String msg;


	public String getSanctionedChk() {
		return sanctionedChk;
	}

	public void setSanctionedChk(String sanctionedChk) {
		this.sanctionedChk = sanctionedChk;
	}

	@SerializedName("sanctioned_chk")
	private String sanctionedChk;

	@SerializedName("company_Id")
	private String companyId;

	@SerializedName("error")
	private String error;

	@SerializedName("b_l")
	private List<BLItem> bL;

	@SerializedName("advance")
	private String advance;

	@SerializedName("net_pay")
	private String netPay;

	@SerializedName("l_c")
	private List<LCItem> lC;

	@SerializedName("to_date")
	private String toDate;

	@SerializedName("misc_attach")
	private List<MiscAttachItem> miscAttach;

	@SerializedName("tour_ticket_id")
	private String tourTicketId;

	@SerializedName("misc")
	private MiscItem misc;

	@SerializedName("from_date")
	private String fromDate;

	@SerializedName("department_id")
	private String departmentId;

	@SerializedName("purpose_tour")
	private String purposeTour;

	@SerializedName("b_l_attach")
	private List<BLAttachItem> bLAttach;

	@SerializedName("department_name")
	private String departmentName;

	@SerializedName("d_o_t")
	private List<DOTItem> dOT;

	@SerializedName("entitlement")
	private String entitlement;

	@SerializedName("d_o_t_attach")
	private List<DOTAttachItem> dOTAttach;

	@SerializedName("devision_id")
	private String devisionId;

	@SerializedName("l_c_att")
	private List<LCAttItem> lCAtt;

	@SerializedName("site_name")
	private String siteName;

	@SerializedName("total_claim")
	private String totalClaim;

	@SerializedName("task_complete")
	private String taskComplete;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("grade")
	private String grade;

	@SerializedName("site_id")
	private String siteId;

	@SerializedName("name")
	private String name;

	@SerializedName("designation")
	private String designation;

	@SerializedName("devision_name")
	private String devisionName;

	@SerializedName("emp_id")
	private String empId;

	public String getMsg(){
		return msg;
	}

	public String getCompanyId(){
		return companyId;
	}

	public String getError(){
		return error;
	}

	public List<BLItem> getBL(){
		return bL;
	}

	public String getAdvance(){
		return advance;
	}

	public String getNetPay(){
		return netPay;
	}

	public List<LCItem> getLC(){
		return lC;
	}

	public String getToDate(){
		return toDate;
	}

	public List<MiscAttachItem> getMiscAttach(){
		return miscAttach;
	}

	public String getTourTicketId(){
		return tourTicketId;
	}

	public MiscItem getMisc(){
		return misc;
	}

	public String getFromDate(){
		return fromDate;
	}

	public String getDepartmentId(){
		return departmentId;
	}

	public String getPurposeTour(){
		return purposeTour;
	}

	public List<BLAttachItem> getBLAttach(){
		return bLAttach;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public List<DOTItem> getDOT(){
		return dOT;
	}

	public String getEntitlement(){
		return entitlement;
	}

	public List<DOTAttachItem> getDOTAttach(){
		return dOTAttach;
	}

	public String getDevisionId(){
		return devisionId;
	}

	public List<LCAttItem> getLCAtt(){
		return lCAtt;
	}

	public String getSiteName(){
		return siteName;
	}

	public String getTotalClaim(){
		return totalClaim;
	}

	public String getTaskComplete(){
		return taskComplete;
	}

	public String getUserId(){
		return userId;
	}

	public String getCompanyName(){
		return companyName;
	}

	public String getGrade(){
		return grade;
	}

	public String getSiteId(){
		return siteId;
	}

	public String getName(){
		return name;
	}

	public String getDesignation(){
		return designation;
	}

	public String getDevisionName(){
		return devisionName;
	}

	public String getEmpId(){
		return empId;
	}
}