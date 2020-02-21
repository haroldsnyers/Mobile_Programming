package ck.edu.com.calculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class DisplayOpAndresult extends AppCompatActivity {

    private TextView mDisplayText;
    private TextView mDisplayQuery;

    private EditText eSearchQuery;

    private RadioGroup radioGroup;

    private RadioButton selectedRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_op_and_result);

        mDisplayText = findViewById(R.id.display);
        mDisplayQuery = findViewById(R.id.displayQuery);

        eSearchQuery = findViewById(R.id.queryURL);

        radioGroup = findViewById(R.id.type);

        Intent intentThatStartedThisActivity = getIntent();

        // we shouldn't assume that every intent has a data in it thus getting the extra
        // with an if statement
        // checks if intent has an extra data called EXTRA_TEXT
        if (intentThatStartedThisActivity.hasExtra("QUERY")) {
            String queryEntered = intentThatStartedThisActivity.getStringExtra("QUERY");
            mDisplayQuery.setText(queryEntered);
        }
        if (intentThatStartedThisActivity.hasExtra("RESULT")) {
            String queryEntered = intentThatStartedThisActivity.getStringExtra("RESULT");
            mDisplayText.setText(queryEntered);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_activity) {
            Context context = DisplayOpAndresult.this;
            Class destinationActivity = MainActivity.class;
            Intent startChildActivityintent = new Intent(context, destinationActivity);
            // getting text entered and passing along as an extra under the name of EXTRA_TEXT
            startActivity(startChildActivityintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchWebview(View v) {

        if(radioGroup.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(getApplicationContext(), "Please select type of search", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            selectedRadioButton = (RadioButton)findViewById(selectedId);
            Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();

            String query = eSearchQuery.getText().toString();

            if (selectedRadioButton.getId() == R.id.query) {
                query = "https://www.google.com/search?q=" + query;
            }

            Context context = DisplayOpAndresult.this;
            Class destinationActivity = Webpage.class;
            Intent startChildActivityintent = new Intent(context, destinationActivity);
            // getting text entered and passing along as an extra under the name of EXTRA_TEXT
            startChildActivityintent.putExtra("URL", query);
            startActivity(startChildActivityintent);
        }
    }
}
