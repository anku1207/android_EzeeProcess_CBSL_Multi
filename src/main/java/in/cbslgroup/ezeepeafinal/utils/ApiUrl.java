package in.cbslgroup.ezeepeafinal.utils;

public class ApiUrl {

    //@Deprecated
   // public static final String BASE_URL = "https://dms.cbslgroup.in/json_api/json/";
    //public static final String ROOT_URL = "http://144.48.78.35/EzeeProcessCBSL/";

    /**
     * This BASE URL is for the api for live server
     */


    //manoj shakya 01-04-21
   // public static final String BASE_URL = "https://dms.cbslgroup.in/api/"; //live link
   // public static final String BASE_URL_ROOT = "https://dms.cbslgroup.in/"; // This BASE URL is for the FTP and other tasks


     /**
     * This BASE URL is for the api for beta server
     */

    //public static final String BASE_URL = "http://144.48.78.35/internal/api/";
    //public static final String BASE_URL_ROOT = "http://144.48.78.35/internal/"; //This BASE URL is for the FTP and other tasks


     //manoj shakya 01-04-21
    public static final String BASE_URL = "http://144.48.78.35/Testing/ezeefile_cbsl/api/";
    public static final String BASE_URL_ROOT = "http://144.48.78.35/Testing/ezeefile_cbsl/"; //This BASE URL is for the FTP and other tasks


    public static final String LOGIN_URL = BASE_URL + "index.php";
    public static final String STORAGE_URL = BASE_URL + "storage.php";
    public static final String DASHBOARD_URL = BASE_URL + "dashboard.php";
    public static final String STORAGE_FILE_URL = BASE_URL + "storagefile.php";
    public static final String GET_METADATA_URL = BASE_URL + "getMetadataList.php";
    public static final String GET_MULTI_METADATA_URL = BASE_URL + "metadatasearch.php";
    public static final String CREATE_NEW_CHILD_URL = BASE_URL + "createNewChild.php";
    public static final String MODIFY_STORAGE_NAME_URL = BASE_URL + "modifyStorage.php";
    public static final String CHECK_MANDATORY_URL = BASE_URL + "checkMandatory.php";
    public static final String UPLOAD_DOC_URL = BASE_URL + "uploadDocument.php";
    public static final String GET_USER_ROLE_PRIVLAGES = BASE_URL + "getRole.php";

    public static final String AUDIT_TRAIL_USERWISE = BASE_URL + "AudittrailUserWise.php";
    public static final String AUDIT_TRAIL_STORAGEWISE = BASE_URL + "AuditTrailStorageWise.php";
    public static final String WORKFLOW_AUDIT_LIST = BASE_URL + "AuditTrailWorkFlow.php";

    public static final String QUICK_SEARCH = BASE_URL + "quickSearch.php";
    public static final String EDIT_PROFILE = BASE_URL + "editProfile.php";
    public static final String GET_USER_DETAILS = BASE_URL + "fetchUserDetails.php";
    public static final String RECYCLE = BASE_URL + "Recycle.php";
    public static final String DELETE_DOC = BASE_URL + "DeleteDoc.php";
    public static final String SHARE_FILES = BASE_URL + "ShareFiles.php";
    public static final String GROUP_MANAGER = BASE_URL + "groupManger.php";
    public static final String ASSIGN_METADATA = BASE_URL + "AssignMetadata.php";
    public static final String MOVE_STORAGE = BASE_URL + "MoveStorage.php";
    public static final String MOVE_COPY_STORAGE = BASE_URL + "MoveCopyStorage.php";
    public static final String STORAGE_LIST_ALLOTED = BASE_URL + "parentChildHirchy.php";
    public static final String CHECKIN_CHECKOUT = BASE_URL + "checkInCheckOut.php";
    public static final String DEL_DOC_VERSION = BASE_URL + "delDocVersion.php";
    public static final String CHECK_BLANK_FOLDER = BASE_URL + "checkBlankStorage.php";
    public static final String MULTI_MOVE_COPY_DEL = BASE_URL + "MultiMoveCopyDelete.php";
    public static final String LOGOUT = BASE_URL + "Logout.php";
    public static final String VERSION = BASE_URL + "Version.php";
    public static final String WORKFLOW_LIST = BASE_URL + "WorkflowList.php";
    public static final String TASK_TRACK_LIST = BASE_URL + "TaskTrackList.php";
    public static final String TASK_TRACK = BASE_URL + "TaskTracking.php";

    public static final String IN_TRAY = BASE_URL + "inTray.php";
    public static final String PROCESS_TASK = BASE_URL + "processTask.php";
    public static final String PROCESS_TASK_APPROVED_REJECT = BASE_URL + "processTaskApproveReject.php";
    public static final String PROCESS_TASK_APPROVED_REJECT_UPLOADING = BASE_URL + "processTaskApproveRejectUploading.php";
    public static final String WORKFLOW_BUILDER = BASE_URL + "getWorkflowFormAttributes.php";
    public static final String INITIATE_WORKFLOW = BASE_URL + "intiateWorkflow.php";
    public static final String CASH_VOUCHER = BASE_URL + "Cashvoucher.php";
    public static final String UPLOAD_DOC_IN_WORKFLOW = BASE_URL + "uploadDocInWorkflow.php";
    public static final String NOTIFICATION_IN_TRAY = BASE_URL + "notificationInTray.php";
    public static final String UPDATE_FIREBASE_TOKEN = BASE_URL + "updateFbToken.php";
    public static final String NEW_VERSION_RELEASE = BASE_URL + "appVersionCheck.php";
    public static final String FTP_UTIL = BASE_URL + "FtpUtil.php";
    public static final String FREQ_QUERY = BASE_URL + "FreqQuery.php";

    //Added in version 2.4 File lock Feature
    public static final String LOCK_FOLDER = BASE_URL + "FolderLock.php";

    //Added in version 2.6 Visitor Pass
    public static final String VISITOR_PASS = BASE_URL + "VisitorPass.php";

    //Added in version 2.7 Adavnce forms
    public static final String ADV_REQ_FORM = BASE_URL + "AdvanceReq.php";
    public static final String CLAIM_FORM = BASE_URL + "ClaimForm.php";
    public static final String SHARE_FOLDER = BASE_URL + "ShareFolder.php";


}
