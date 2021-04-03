package in.cbslgroup.ezeepeafinal.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import in.cbslgroup.ezeepeafinal.R;
import in.cbslgroup.ezeepeafinal.utils.LocaleHelper;


public class FaqActivity extends AppCompatActivity {

    WebView webView;
    private String faqUrl = "http://ezeedigitalsolutions.in/faq.php";
    ProgressBar progressBar;
    NestedScrollView nestedScrollViewWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initLocale();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_faq);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaqActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        webView = findViewById(R.id.webview_faq);
        progressBar = findViewById(R.id.progressBar_webview);
        nestedScrollViewWebview = findViewById(R.id.nestedscrollview_webview);

        nestedScrollViewWebview.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().supportZoom();
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {


                if (progressBar.getVisibility()==View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                    nestedScrollViewWebview.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(FaqActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl(faqUrl);


    }


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



}

