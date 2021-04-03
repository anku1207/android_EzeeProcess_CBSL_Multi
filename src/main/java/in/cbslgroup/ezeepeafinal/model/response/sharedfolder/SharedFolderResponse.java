package in.cbslgroup.ezeepeafinal.model.response.sharedfolder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SharedFolderResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("error")
	private String error;

	@SerializedName("list")
	private List<SharedFolderListItem> list;

	public String getMsg(){
		return msg;
	}

	public String getError(){
		return error;
	}

	public List<SharedFolderListItem> getList(){
		return list;
	}

	@Override
 	public String toString(){
		return 
			"SharedFolderResponse{" + 
			"msg = '" + msg + '\'' + 
			",error = '" + error + '\'' + 
			",list = '" + list + '\'' + 
			"}";
		}
}