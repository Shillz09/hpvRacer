package edu.wvu.hpvracer;

import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;

public class AboutUs extends Activity {

	private WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_about_us);

        String url = "file:///android_asset/about.html";

        webview = (WebView) findViewById(R.id.myWebView);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.loadUrl(url);
    }
	

}
