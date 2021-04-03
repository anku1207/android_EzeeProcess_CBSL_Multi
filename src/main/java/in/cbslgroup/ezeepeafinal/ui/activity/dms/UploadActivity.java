package in.cbslgroup.ezeepeafinal.ui.activity.dms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shuhart.stepview.StepView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.ui.fragments.upload.DescribesFragment;
import in.cbslgroup.ezeepeafinal.ui.fragments.upload.UploadFileFragment;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;

public class UploadActivity extends AppCompatActivity {

    public static String filePath;
    public static int currentStep = 0;
    public static StepView stepView;
    public static String filetypedynamic;
    public static String fileName;
    public static int noOfPages;
    public static long filesize;
    Fragment fragment = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button btn1, btn2, btn3, btn4, btnChooseFile, btnUpload;

    private void initLocale() {
        String lang = LocaleHelper.getPersistedData(this, null);
        if (lang == null) {

            LocaleHelper.persist(this, "en");
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        //AllowRunTimePermission();

        requestPermission();
        initLocale();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Toolbar toolbar = findViewById(R.id.toolbar_upload);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();


            }
        });


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_upload_root, new DescribesFragment());
        ft.commit();

        stepView = findViewById(R.id.step_view);

        List<String> steps = new ArrayList<>();
        steps.add(getString(R.string.describes));
        steps.add(getString(R.string.upload));
        steps.add(getString(R.string.verify)+"\n" +getString(R.string.and_operator)+"\n"+getString(R.string.complete_capital));
        steps.add(getString(R.string.upload) +"\n"+getString(R.string.in)+"\n"+ getString(R.string.workflow_capital));
        stepView.setSteps(steps);
       /* btnNext =findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                   // displaySelectedScreen(currentStep);
                   // btnBack.setVisibility(View.INVISIBLE);
                    //btnBackVisibilty(currentStep);

                    Log.e("step", String.valueOf(currentStep));

                } else {
                    stepView.done(true);
                }

            }
        });

       btnBack=findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentStep > 0) {
                    currentStep--;
                    btnBackVisibilty(currentStep);
                    Log.e("step", String.valueOf(currentStep));


                }
                stepView.done(false);
                displaySelectedScreen(currentStep);
                stepView.go(currentStep, true);

            }

            }
        );
*/

    }

    /*private void displaySelectedScreen(int currentStep) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (currentStep) {
            case 0:
                fragment = new DescribesFragment();
                break;
            case 1:
                fragment = new UploadFileFragment();
                break;
            case 2:
                fragment = new VerifyAndCompleteFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_upload_root, fragment);
            ft.commit();
        }


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {

            filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Log.e("filepath in activity", filePath);

            UploadFileFragment.ivFileType.setVisibility(View.VISIBLE);
            UploadFileFragment.cvCameraImg.setVisibility(View.GONE);


            File f = new File(filePath);
            filesize = f.length() / 1024;

            Log.e("filesize in upload", String.valueOf(filesize));
            Log.e("pages", String.valueOf(noOfPages));

            String filename = f.getName();
            UploadFileFragment.tvFilePath.setText(filename);
            UploadFileFragment.tvFilePath.setError(null);

            fileName = filename;
            filetypedynamic = filename.substring(filename.lastIndexOf(".") + 1);
            Log.e("filetypedyanmic", filetypedynamic);

            switch (filetypedynamic) {
                case "pdf":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.pdf);

                    break;
                case "jpg":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.jpg);

                    break;
                case "png":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.png);

                    break;
                case "jpeg":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.jpg);

                    break;

                case "tiff":

                    UploadFileFragment.ivFileType.setImageResource(R.drawable.tiff_png);

                    break;
                case "mp4":
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.mp4);

                    break;
                case "3gp":


                default:
                    UploadFileFragment.ivFileType.setImageResource(R.drawable.file);
                    break;
            }



            }


        // handle result of CropImageActivity
        else if (requestCode == 203) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                //Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();

                UploadFileFragment.cvCameraImg.setVisibility(View.VISIBLE);
                UploadFileFragment.ivCameraPreview.setImageURI(result.getUri());

                Log.e("image uri crop", result.getUri().toString());

                String uri = result.getUri().toString();
                String path = uri.substring(7, uri.length());

                Log.e("path crop", path);

                UploadFileFragment.filePath = path;

                filePath = path;

                File f = new File(filePath);
                filesize = f.length() / 1024;

                Log.e("filesize in upload", String.valueOf(filesize));
                Log.e("pages", String.valueOf(noOfPages));

                String filename = f.getName();
                UploadFileFragment.tvFilePath.setText(filename);
                UploadFileFragment.tvFilePath.setError(null);

                fileName = filename;
                filetypedynamic = filename.substring(filename.lastIndexOf(".") + 1);
                Log.e("filetypedyanmic", filetypedynamic);


            }

            // btnChooseFile.setText("PDF is Selected");
            // btnChooseFile.setBackgroundColor(Color.RED);

        }




    }

    public void AllowRunTimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(UploadActivity.this, "READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle(getString(R.string.need_permisions));
        builder.setMessage(getString(R.string.grant_permission_message));
        builder.setPositiveButton(getString(R.string.goto_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

   /* void btnBackVisibilty(int step){

        if(step==1)
        {
            btnBack.setVisibility(View.VISIBLE);

        }
        else if(step==2){

            btnBack.setVisibility(View.VISIBLE);
        }

        else if(step==0) {

            btnBack.setVisibility(View.INVISIBLE);
        }

        else{

            Log.e("step","reached last step");
        }



    }*/





}
