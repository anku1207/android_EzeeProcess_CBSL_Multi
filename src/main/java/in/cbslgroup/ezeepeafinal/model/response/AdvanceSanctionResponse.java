package in.cbslgroup.ezeepeafinal.model.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AdvanceSanctionResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("arf_id")
	private String arfId;

	@SerializedName("company_id")
	private String companyId;

	@SerializedName("date")
	private String date;

	@SerializedName("total_adv_req")
	private String totalAdvReq;

	@SerializedName("other_expense")
	private String otherExpense;

	@SerializedName("error")
	private String error;

	@SerializedName("date_of_travel")
	private String dateOfTravel;

	@SerializedName("l_per_day")
	private String lPerDay;

	@SerializedName("f_charge")
	private String fCharge;

	@SerializedName("branch_name")
	private String branchName;

	@SerializedName("bank_name")
	private String bankName;

	@SerializedName("l_conveyance")
	private String lConveyance;

	@SerializedName("empid")
	private String empid;

	@SerializedName("l_charge")
	private String lCharge;

	@SerializedName("client_name_event")
	private String clientNameEvent;

	@SerializedName("noofdays")
	private String noofdays;

	@SerializedName("telephone_charge")
	private String telephoneCharge;

	@SerializedName("d_o_t")
	private List<DOTAdvanceSanction> dOT;

	@SerializedName("ifsccode")
	private String ifsccode;

	@SerializedName("calc_est_cost")
	private String calcEstCost;

	@SerializedName("otherexpenseremark")
	private String otherexpenseremark;

	@SerializedName("grade")
	private String grade;

	@SerializedName("company_name")
	private String companyName;

	@SerializedName("accountno")
	private String accountno;

	@SerializedName("guesthouse_available")
	private String guesthouseAvailable;

	@SerializedName("expected_date_of_return")
	private String expectedDateOfReturn;

	@SerializedName("fullname")
	private String fullname;

	@SerializedName("designation")
	private String designation;

	@SerializedName("f_per_day")
	private String fPerDay;

	@SerializedName("cardbankname")
	private String cardbankname;

	@SerializedName("cardno")
	private String cardno;

	@SerializedName("receivetype")
	private String receivetype;


	public String getCardbankname() {
		return cardbankname;
	}

	public void setCardbankname(String cardbankname) {
		this.cardbankname = cardbankname;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getReceivetype() {
		return receivetype;
	}

	public void setReceivetype(String receivetype) {
		this.receivetype = receivetype;
	}



	public String getMsg(){
		return msg;
	}

	public String getDate(){
		return date;
	}

	public String getTotalAdvReq(){
		return totalAdvReq;
	}

	public String getOtherExpense(){
		return otherExpense;
	}

	public String getError(){
		return error;
	}

	public String getDateOfTravel(){
		return dateOfTravel;
	}

	public String getLPerDay(){
		return lPerDay;
	}

	public String getFCharge(){
		return fCharge;
	}

	public String getBranchName(){
		return branchName;
	}

	public String getBankName(){
		return bankName;
	}

	public String getLConveyance(){
		return lConveyance;
	}

	public String getEmpid(){
		return empid;
	}

	public String getLCharge(){
		return lCharge;
	}

	public String getClientNameEvent(){
		return clientNameEvent;
	}

	public String getNoofdays(){
		return noofdays;
	}

	public String getTelephoneCharge(){
		return telephoneCharge;
	}

	public List<DOTAdvanceSanction> getDOT(){
		return dOT;
	}

	public String getIfsccode(){
		return ifsccode;
	}

	public String getToAndFroFare(){
		return calcEstCost;
	}

	public String getOtherexpenseremark(){
		return otherexpenseremark;
	}

	public String getGrade(){
		return grade;
	}

	public String getCompanyName(){
		return companyName;
	}

	public String getAccountno(){
		return accountno;
	}

	public String getGuesthouseAvailable(){
		return guesthouseAvailable;
	}

	public String getExpectedDateOfReturn(){
		return expectedDateOfReturn;
	}

	public String getFullname(){
		return fullname;
	}

	public String getDesignation(){
		return designation;
	}

	public String getFPerDay(){
		return fPerDay;
	}

	public String getArfId() {
		return arfId;
	}

	public void setArfId(String arfId) {
		this.arfId = arfId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
}