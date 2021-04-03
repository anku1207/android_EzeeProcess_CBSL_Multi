package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import com.google.gson.annotations.SerializedName;

public class LCItem{

	@SerializedName("local_remarks")
	private String localRemarks;

	@SerializedName("lc_id")
	private String lcId;

	@SerializedName("claim_ticket")
	private String claimTicket;

	@SerializedName("lc_to")
	private String lcTo;

	@SerializedName("lc_time")
	private String lcTime;

	@SerializedName("lc_camt")
	private String lcCamt;

	@SerializedName("lc_mode")
	private String lcMode;

	@SerializedName("lc_sno")
	private String lcSno;

	@SerializedName("claim_id")
	private String claimId;

	@SerializedName("lc_from")
	private String lcFrom;

	@SerializedName("lc_sanamt")
	private String lcSanamt;

	@SerializedName("lc_date")
	private String lcDate;

	public String getLocalRemarks(){
		return localRemarks;
	}

	public String getLcId(){
		return lcId;
	}

	public String getClaimTicket(){
		return claimTicket;
	}

	public String getLcTo(){
		return lcTo;
	}

	public String getLcTime(){
		return lcTime;
	}

	public String getLcCamt(){
		return lcCamt;
	}

	public String getLcMode(){
		return lcMode;
	}

	public String getLcSno(){
		return lcSno;
	}

	public String getClaimId(){
		return claimId;
	}

	public String getLcFrom(){
		return lcFrom;
	}

	public String getLcSanamt(){
		return lcSanamt;
	}

	public String getLcDate(){
		return lcDate;
	}
}