package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import com.google.gson.annotations.SerializedName;

public class DOTItem{

	@SerializedName("travel_id")
	private String travelId;

	@SerializedName("claim_ticket")
	private String claimTicket;

	@SerializedName("arrival_time")
	private String arrivalTime;

	@SerializedName("departure_date")
	private String departureDate;

	@SerializedName("claim_id")
	private String claimId;

	@SerializedName("traveling_remarks")
	private String travelingRemarks;

	@SerializedName("departure_place")
	private String departurePlace;

	@SerializedName("arrival_place")
	private String arrivalPlace;

	@SerializedName("claim_amt")
	private String claimAmt;

	@SerializedName("mode")
	private String mode;

	@SerializedName("sno")
	private String sno;

	@SerializedName("santioned_amt")
	private String santionedAmt;

	@SerializedName("departure_time")
	private String departureTime;

	@SerializedName("arrival_date")
	private String arrivalDate;

	@SerializedName("class")
	private String jsonMemberClass;

	public String getTravelId(){
		return travelId;
	}

	public String getClaimTicket(){
		return claimTicket;
	}

	public String getArrivalTime(){
		return arrivalTime;
	}

	public String getDepartureDate(){
		return departureDate;
	}

	public String getClaimId(){
		return claimId;
	}

	public String getTravelingRemarks(){
		return travelingRemarks;
	}

	public String getDeparturePlace(){
		return departurePlace;
	}

	public String getArrivalPlace(){
		return arrivalPlace;
	}

	public String getClaimAmt(){
		return claimAmt;
	}

	public String getMode(){
		return mode;
	}

	public String getSno(){
		return sno;
	}

	public String getSantionedAmt(){
		return santionedAmt;
	}

	public String getDepartureTime(){
		return departureTime;
	}

	public String getArrivalDate(){
		return arrivalDate;
	}

	public String getJsonMemberClass(){
		return jsonMemberClass;
	}
}