package edu.wvu.hpvracer;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class CloudRaceData extends Activity {
	private WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_race_data);

        String url = "http://50weasels.com/HCI/showRaceDataTable.php";

        webview = (WebView) findViewById(R.id.myWebView);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.loadUrl(url);
    }
	
}
