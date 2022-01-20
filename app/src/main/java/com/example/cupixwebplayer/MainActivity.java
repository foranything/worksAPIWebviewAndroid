package com.example.cupixwebplayer;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

//    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // enable full screen

        setContentView(R.layout.activity_main);

        final WebView webView = findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebContentsDebuggingEnabled(true); // mobile webView debugging in Chrome
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("console log", "[" + cm.messageLevel() + "]" + cm.message() + " LN:" + cm.lineNumber() + " SRC: " + cm.sourceId());
                return true;
            }
        });

        webView.loadUrl("http://cupix.local.cupix.works:4200/sv/rvqkch");
//        webView.loadUrl("http://10.0.2.2:4200");

//        class CupixEventListener {
//            @JavascriptInterface
//            public void update(String eventType, String eventName, String arg) {
//                Log.d("event log", "[CUPIX-EVENT][" + eventType + "] " +  eventName + ": " + arg);
//            }
//        }
//        webView.addJavascriptInterface(new CupixEventListener(), "cupixEventListener");

        class CupixEventListenerV2 {
            @JavascriptInterface
            public void update(String sender, String type, String args, String ver, String playerId, String caller) {
                Log.d("event log", "[CUPIX-EVENT-V" + ver + "]" + sender + " - " + type + ": " + args+ ": " + playerId + ": " + caller);
            }
        }
        webView.addJavascriptInterface(new CupixEventListenerV2(), "cupixEventListenerV2");
        final int[] uuid = {0};
        if (webView != null) {
            final Button button = findViewById(R.id.send_event_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject event = new JSONObject();
                        event.put("header", "CUPIXWORKS_API");
                        event.put("uuid", String.valueOf(uuid[0]));
                        event.put("timestamp", new Date());
                        event.put("operationType", "APP_API_START");
                        uuid[0] = uuid[0] + 1;
                        webView.evaluateJavascript("window.postMessage(" + event.toString() + ", '*')", null);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }

            });
            final Button button2 = findViewById(R.id.send_event_button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View view) {
                    try {
                        JSONObject args = new JSONObject();
                        args.put("token", "Mi6F6bioooAjGrS7nP1DPqPgYoJniEw8");
                        JSONObject event = new JSONObject();
                        event.put("header", "CUPIXWORKS_API");
                        event.put("uuid", String.valueOf(uuid[0]));
                        event.put("timestamp", new Date());
                        event.put("operationType", "SIGNIN");
                        event.put("operationArgs", args);
                        uuid[0] = uuid[0] + 1;
                        webView.evaluateJavascript("window.postMessage(" + event.toString() + ", '*')", null);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            final Button button3 = findViewById(R.id.send_event_button3);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View view) {
                    try {
                        JSONObject args = new JSONObject();
                        args.put("siteViewKey", "rvqkch");
                        JSONObject event = new JSONObject();
                        event.put("header", "CUPIXWORKS_API");
                        event.put("uuid", String.valueOf(uuid[0]));
                        event.put("timestamp", new Date());
                        event.put("operationType", "GO_SITEVIEW");
                        event.put("operationArgs", args);
                        uuid[0] = uuid[0] + 1;
                        webView.evaluateJavascript("window.postMessage(" + event.toString() + ", '*')", null);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }
    }
}
