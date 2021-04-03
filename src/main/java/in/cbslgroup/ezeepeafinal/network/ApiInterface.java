package in.cbslgroup.ezeepeafinal.network;


import in.cbslgroup.ezeepeafinal.model.response.DefaultResponse;
import in.cbslgroup.ezeepeafinal.model.response.AdvanceSanctionResponse;
import in.cbslgroup.ezeepeafinal.model.response.AdvanceSanctionSubmitResponse;
import in.cbslgroup.ezeepeafinal.model.response.sanctionedclaim.SanctionClaimResponse;
import in.cbslgroup.ezeepeafinal.model.response.sharedfolder.SharedFolderResponse;
import in.cbslgroup.ezeepeafinal.utils.ApiUrl;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {


    /**
     *  AdvanceSanctionedActivity Methods
     */

    /**
     * @param action   this is the action of the method given
     * @param userid   userid of the specific user
     * @param tickedId ticket id of the specific form
     * @return getting the sanctioned advance form .
     * @apiNote This method is used for getting the Sanctioned Advance Form Details.
     */


    @FormUrlEncoded
    @POST(ApiUrl.ADV_REQ_FORM)
    Call<AdvanceSanctionResponse> getAdvanceSanctionedDetails(@Field("action") String action,
                                                              @Field("userid") String userid,
                                                              @Field("ticket_id") String tickedId);


    @FormUrlEncoded
    @POST(ApiUrl.ADV_REQ_FORM)
    Call<AdvanceSanctionSubmitResponse> submitAdvanceSanctionForm(

            @Field("action") String action,
            @Field("ticketId") String ticketId,
            @Field("arf_id") String arf_id,
            @Field("company") String companId,
            @Field("fullname") String fullname,
            @Field("grade") String grade,
            @Field("traveldate") String traveldate,
            @Field("nclient") String nclient,
            @Field("tdate") String tdate,
            @Field("designation") String designation,
            @Field("employeeid") String employeeid,
            @Field("returndate") String returndate,
            @Field("noofdays") String noofdays,
            @Field("bankname") String bankname,
            @Field("branchname") String branchname,
            @Field("ifsccode") String ifsccode,
            @Field("accountno") String accountno,
            @Field("guesthouse") String guesthouse,
            @Field("to_fro_fare") String to_fro_fare,
            @Field("lodging_charge") String lodging_charge,
            @Field("per_day_for") String per_day_for,
            @Field("totl") String totl,
            @Field("food_charge") String food_charge,
            @Field("per_day_for_food") String per_day_for_food,
            @Field("total_bod") String total_bod,
            @Field("local_convyance1") String local_convyance1,
            @Field("local_convyance") String local_convyance,
            @Field("telephone1") String telephone1,
            @Field("telephone") String telephone,
            @Field("otherexpense1") String otherexpense1,
            @Field("otherexpenseremark") String otherexpenseremark,
            @Field("otherexpense") String otherexpense,
            @Field("Total1") String Total1,
            @Field("Total") String Total,
            @Field("userID") String userid,
            @Field("sno") String sno,
            @Field("purpose") String purpose,
            @Field("arequired") String arequired,
            @Field("travelId") String travelId,
            @Field("doftravel") String dotTravel

    );

    /**
     *  SanctionedClaimActivity Methods
     */

    @FormUrlEncoded
    @POST(ApiUrl.CLAIM_FORM)
    Call<SanctionClaimResponse> getSanctionedClaimDetails(@Field("action") String action,
                                                          @Field("ticket_id") String tickedId,
                                                          @Field("claim_id") String claimid);

    @FormUrlEncoded
    @POST(ApiUrl.CLAIM_FORM)
    Call<DefaultResponse> sumbitSanctionedClaimForm(@Field("action") String action,
                                                    @Field("ticket_id") String tickedId,
                                                    @Field("claim_id") String claimid,
                                                    @Field("preclaimid") String preclaimid,
                                                    @Field("sanctioned") String sanctioned,
                                                    @Field("fullname") String fullname,
                                                    @Field("grade") String grade,
                                                    @Field("designation") String designation,
                                                    @Field("emp_id") String emp_id,
                                                    @Field("sno") String sno,
                                                    @Field("sanamt") String sanamt,
                                                    @Field("travel_id") String travel_id,
                                                    @Field("sno2") String sno2,
                                                    @Field("sanamt2") String sanamt2,
                                                    @Field("bl_id") String bl_id,
                                                    @Field("msctele") String msctele,
                                                    @Field("mscint") String mscint,
                                                    @Field("msctkt") String msctkt,
                                                    @Field("mscoexp") String mscoexp,
                                                    @Field("lcsno") String lcsno,
                                                    @Field("lcsantionamt") String lcsantionamt,
                                                    @Field("lc_id") String lc_id,
                                                    @Field("userID") String userid


    );


    /***************************************************************
     *                  Web Methods For the Share Folder           *
     ***************************************************************/


    @FormUrlEncoded
    @POST(ApiUrl.SHARE_FOLDER)
    Call<SharedFolderResponse> getSharedFolderList(@Field("action") String action , @Field("userid") String userid);

    @FormUrlEncoded
    @POST(ApiUrl.SHARE_FOLDER)
    Call<SharedFolderResponse> getSharedFolderWithMeList(@Field("action") String action  , @Field("userid") String userid);

    @FormUrlEncoded
    @POST(ApiUrl.SHARE_FOLDER)
    Call<DefaultResponse> undoSharedFolder(@Field("action") String action  ,
                                           @Field("id") String id,
                                           @Field("sl_name") String slName,
                                           @Field("slId") String slid,
                                           @Field("userId") String userId
    );



}