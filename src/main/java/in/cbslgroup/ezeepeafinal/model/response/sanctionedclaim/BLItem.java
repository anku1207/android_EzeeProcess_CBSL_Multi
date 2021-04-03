package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import com.google.gson.annotations.SerializedName;

public class BLItem{

	@SerializedName("lodging")
	private String lodging;

	@SerializedName("lb_date")
	private String lbDate;

	@SerializedName("boarding_remarks")
	private String boardingRemarks;

	@SerializedName("claim_ticket")
	private String claimTicket;

	@SerializedName("lb_sno")
	private String lbSno;

	@SerializedName("bl_id")
	private String blId;

	@SerializedName("claim_id")
	private String claimId;

	@SerializedName("lb_s_amt")
	private String lbSAmt;

	@SerializedName("boarding")
	private String boarding;

	@SerializedName("lb_c_amt")
	private String lbCAmt;

	public String getLodging(){
		return lodging;
	}

	public String getLbDate(){
		return lbDate;
	}

	public String getBoardingRemarks(){
		return boardingRemarks;
	}

	public String getClaimTicket(){
		return claimTicket;
	}

	public String getLbSno(){
		return lbSno;
	}

	public String getBlId(){
		return blId;
	}

	public String getClaimId(){
		return claimId;
	}

	public String getLbSAmt(){
		return lbSAmt;
	}

	public String getBoarding(){
		return boarding;
	}

	public String getLbCAmt(){
		return lbCAmt;
	}
}