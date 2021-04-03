package in.cbslgroup.ezeepeafinal.model.response.sharedfolder;

import com.google.gson.annotations.SerializedName;

public class SharedFolderListItem {

	@SerializedName("slId")
	private String slId;

	@SerializedName("shared_date")
	private String sharedDate;

	@SerializedName("sl_name")
	private String slName;

	@SerializedName("share_with")
	private String shareWith;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("Id")
	private String id;

	@SerializedName("share_by")
	private String shareBy;

	@SerializedName("first_name")
	private String firstName;

	public String getSlId(){
		return slId;
	}

	public String getSharedDate(){
		return sharedDate;
	}

	public String getSlName(){
		return slName;
	}

	public String getShareWith(){
		return shareWith;
	}

	public String getLastName(){
		return lastName;
	}

	public String getId(){
		return id;
	}

	public String getShareBy(){
		return shareBy;
	}

	public String getFirstName(){
		return firstName;
	}

	@Override
 	public String toString(){
		return 
			"ListItem{" + 
			"slId = '" + slId + '\'' + 
			",shared_date = '" + sharedDate + '\'' + 
			",sl_name = '" + slName + '\'' + 
			",share_with = '" + shareWith + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",share_by = '" + shareBy + '\'' + 
			",first_name = '" + firstName + '\'' + 
			"}";
		}
}