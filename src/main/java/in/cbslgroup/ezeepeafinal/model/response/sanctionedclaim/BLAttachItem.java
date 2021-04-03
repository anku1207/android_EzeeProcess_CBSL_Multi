package in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim;

import com.google.gson.annotations.SerializedName;

public class BLAttachItem{

	@SerializedName("old_doc_name")
	private String oldDocName;

	@SerializedName("doc_extn")
	private String docExtn;

	@SerializedName("doc_id")
	private String docId;

	public String getOldDocName(){
		return oldDocName;
	}

	public String getDocExtn(){
		return docExtn;
	}

	public String getDocId(){
		return docId;
	}
}