package com.yeran.healthhomecare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.health.R;

public class Webview extends Activity{
	private WebView webview;
	private String url;
	ProgressBar prog;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(com.example.health.R.layout.map_activity_webview);
		prog=(ProgressBar)findViewById(R.id.progress);
		prog.setProgress(0);
		Intent intent = getIntent();
		url = intent.getExtras().getString("HospUrl");
		
		Init();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void Init() {
		webview=(WebView)findViewById(com.example.health.R.id.webview);
		Setting(webview);		
		webview.loadUrl(url);
		
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
	       webview.setWebViewClient(new WebViewClient(){
	           @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
	        	   Setting(view);	
	        	if (url.contains("tel:")) {
	        		//跳转到拨号盘界面
	        		Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_DIAL,uri);
					startActivity(intent);
					
				}else{
		             view.loadUrl(url);
				}
	        	
	            return true;
	        }
	           
//	           @Override
//	           public void onPageFinished(WebView view, String url) {
//					// TODO Auto-generated method stub
//					 super.onPageFinished(view, url);	
//					 prog.setVisibility(View.GONE);
//					 System.out.println("gone");
//				}
	           
	           @Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// TODO Auto-generated method stub
					super.onReceivedError(view, errorCode, description, failingUrl);
					Toast.makeText(Webview.this, "同步失败，请稍候再试", Toast.LENGTH_SHORT)
							.show();
				}
	      	       
	       });
	       
	       webview.setWebChromeClient(new WebChromeClient() {
	    	   public void onProgressChanged(WebView view, int progress) {
	    	     // Activities and WebViews measure progress with different scales.
	    	     // The progress meter will automatically disappear when we reach 100%
	    	     prog.setProgress(progress);
	    	     System.out.println("pross"+progress);
	    	     if (progress==100) {
	    	    	 prog.setVisibility(View.GONE);
					 System.out.println("gone");
				}else{
					System.out.println("have");
					prog.setVisibility(View.VISIBLE);
					
				}
	    	   }
	    	 });
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void Setting(WebView webview){
		//启用支持javascript
				WebSettings settings = webview.getSettings();
				settings.setJavaScriptEnabled(true);
				settings.setJavaScriptCanOpenWindowsAutomatically(true);
				settings.setAppCacheEnabled(true);
				settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
				settings.setLoadWithOverviewMode(true);
				settings.setBuiltInZoomControls(true);
				settings.setSupportZoom(true);
				settings.setSaveFormData(true);
				settings.setLoadsImagesAutomatically(true);
				settings.setBuiltInZoomControls(true); 
				settings.setDomStorageEnabled(true);
				settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); 
				settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				webview.requestFocus();
				webview.setScrollBarStyle(0);
	}
	
//	 @Override
//	    public boolean onKeyDown(int keyCode, KeyEvent event) {
//	        // TODO Auto-generated method stub
//	        if(keyCode==KeyEvent.KEYCODE_BACK)
//	        {
//	            if(webview.canGoBack())
//	            {
//	                webview.goBack();//返回上一页面
//	                return true;
//	            }
//	            else
//	            {
//	                this.finish();//退出程序
//	            }
//	        }
//	        return super.onKeyDown(keyCode, event);
//	    }

}
