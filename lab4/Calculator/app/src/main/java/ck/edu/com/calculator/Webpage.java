package ck.edu.com.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Webpage extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = findViewById(R.id.myWebView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }});

        Intent intentThatStartedThisActivity = getIntent();

        // we shouldn't assume that every intent has a data in it thus getting the extra
        // with an if statement
        // checks if intent has an extra data called EXTRA_TEXT
        if (intentThatStartedThisActivity.hasExtra("URL")) {
            String queryEntered = intentThatStartedThisActivity.getStringExtra("URL");
            webView.loadUrl(queryEntered);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.back_calculator) {
            Context context = Webpage.this;
            Class destinationActivity = MainActivity.class;
            Intent startChildActivityintent = new Intent(context, destinationActivity);
            // getting text entered and passing along as an extra under the name of EXTRA_TEXT
            startActivity(startChildActivityintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
