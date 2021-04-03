package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import com.google.gson.annotations.SerializedName;

public class MiscItem{

	@SerializedName("claim_ticket")
	private String claimTicket;

	@SerializedName("ticket_santioned")
	private String ticketSantioned;

	@SerializedName("other_expense_santioned")
	private String otherExpenseSantioned;

	@SerializedName("other_remarks")
	private String otherRemarks;

	@SerializedName("telephone_santioned")
	private String telephoneSantioned;

	@SerializedName("internet_santioned")
	private String internetSantioned;

	@SerializedName("claim_id")
	private String claimId;

	@SerializedName("ticket_claim")
	private String ticketClaim;

	@SerializedName("telephone_remarks")
	private String telephoneRemarks;

	@SerializedName("telephone_claim")
	private String telephoneClaim;

	@SerializedName("internet_remarks")
	private String internetRemarks;

	@SerializedName("other_expense_claim")
	private String otherExpenseClaim;

	@SerializedName("misc_id")
	private String miscId;

	@SerializedName("internet_claim")
	private String internetClaim;

	@SerializedName("agent_remarks")
	private String agentRemarks;

	public String getClaimTicket(){
		return claimTicket;
	}

	public String getTicketSantioned(){
		return ticketSantioned;
	}

	public String getOtherExpenseSantioned(){
		return otherExpenseSantioned;
	}

	public String getOtherRemarks(){
		return otherRemarks;
	}

	public String getTelephoneSantioned(){
		return telephoneSantioned;
	}

	public String getInternetSantioned(){
		return internetSantioned;
	}

	public String getClaimId(){
		return claimId;
	}

	public String getTicketClaim(){
		return ticketClaim;
	}

	public String getTelephoneRemarks(){
		return telephoneRemarks;
	}

	public String getTelephoneClaim(){
		return telephoneClaim;
	}

	public String getInternetRemarks(){
		return internetRemarks;
	}

	public String getOtherExpenseClaim(){
		return otherExpenseClaim;
	}

	public String getMiscId(){
		return miscId;
	}

	public String getInternetClaim(){
		return internetClaim;
	}

	public String getAgentRemarks(){
		return agentRemarks;
	}
}