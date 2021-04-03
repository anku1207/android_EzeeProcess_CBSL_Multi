package in.cbslgroup.ezeepeafinal.model.response;

import com.google.gson.annotations.SerializedName;

public class AdvanceSanctionSubmitResponse {

	@SerializedName("msg")
	private String msg;

	@SerializedName("error")
	private String error;

	public String getMsg(){
		return msg;
	}

	public String getError(){
		return error;
	}
}