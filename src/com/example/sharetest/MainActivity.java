package com.example.sharetest;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.js.ShareSDKUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.PlatformListFakeActivity.OnShareButtonClickListener;

public class MainActivity extends Activity{
	
	private WebView webView;

	protected boolean isJumpOutOfAppSharing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ShareSDK.initSDK(this);
		
		Button button = (Button) findViewById(R.id.btn);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showShare();
			}
		});

		prepareView();
	}
	
	/** 原生调用原生分享 */
	protected void showShare() {
		ShareSDK.initSDK(MainActivity.this);
		OnekeyShare oks = new OnekeyShare();
		oks.setOnShareButtonClickListener(new OnShareButtonClickListener() {

			@Override
			public void onClick(View v, List<Object> checkPlatforms) {
				isJumpOutOfAppSharing = true;
			}
		});
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("test title");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("test title url");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("test text");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("test url");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("test comment");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("test site url");

		// 启动分享GUI
		oks.show(MainActivity.this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void prepareView() {

		webView = (WebView) findViewById(R.id.webview);
		
		WebViewClient wvClient = new WebViewClient();
		
		webView.setWebViewClient(wvClient);
		webView.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		
		// js调用原生分享初始化
		ShareSDKUtils.prepare(webView, wvClient);
		
		webView.loadUrl("file:///android_asset/Sample.html");
	}
	
	@Override
	protected void onResume() {
		if (isJumpOutOfAppSharing) {
			isJumpOutOfAppSharing = false;
		}
		super.onResume();
	}
}
