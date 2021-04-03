package in.cbslgroup.ezeepeafinal.model.response;

import com.google.gson.annotations.SerializedName;

public class DOTAdvanceSanction {

	@SerializedName("mode")
	private String mode;

	@SerializedName("travel_id")
	private String travelId;

	@SerializedName("sanctioned_amt")
	private Object sanctionedAmt;

	@SerializedName("purpose")
	private String purpose;

	@SerializedName("t_o_t")
	private Object tOT;

	@SerializedName("d_o_t")
	private String dOT;

	@SerializedName("destination")
	private String destination;

	@SerializedName("s_no")
	private String sNo;

	@SerializedName("arf_id")
	private String arfId;

	@SerializedName("source")
	private String source;

	@SerializedName("class")
	private String jsonMemberClass;

	@SerializedName("advance_req")
	private String advanceReq;

	public String getMode(){
		return mode;
	}

	public String getTravelId(){
		return travelId;
	}

	public Object getSanctionedAmt(){
		return sanctionedAmt;
	}

	public String getPurpose(){
		return purpose;
	}

	public Object getTOT(){
		return tOT;
	}

	public String getDOT(){
		return dOT;
	}

	public String getDestination(){
		return destination;
	}

	public String getSNo(){
		return sNo;
	}

	public String getArfId(){
		return arfId;
	}

	public String getSource(){
		return source;
	}

	public String getJsonMemberClass(){
		return jsonMemberClass;
	}

	public String getAdvanceReq(){
		return advanceReq;
	}
}