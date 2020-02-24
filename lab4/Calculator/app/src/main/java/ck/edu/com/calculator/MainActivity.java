package ck.edu.com.calculator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    private TextView textViewQuery;
    private TextView result;

    private String queryText;
    private String resultTetx;

    private Button equal;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewQuery = findViewById(R.id.input);
        result = findViewById(R.id.result);

        equal = findViewById(R.id.btnequals);
        equal.setEnabled(false);

        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.results) {
            Context context = MainActivity.this;
            Class destinationActivity = DisplayOpAndresult.class;
            Intent startChildActivityintent = new Intent(context, destinationActivity);
            // getting text entered and passing along as an extra under the name of EXTRA_TEXT
            startChildActivityintent.putExtra("QUERY", queryText);
            startChildActivityintent.putExtra("RESULT", resultTetx);
            startActivity(startChildActivityintent);
            return true;
        }
        else if (id == R.id.intent) {
            Intent sendIntent = new Intent();
            sendIntent.setAction("hereismyapp");

            // Verify that the intent will resolve to an activity
            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                Toast.makeText(getApplicationContext(), " New intent", Toast.LENGTH_SHORT).show();
                startActivity(sendIntent);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @param v
     *
     * update in real time mathematical query
     *
     * Called by every button on calculator except equal
     */
    public void updateText(View v){
        String query = textViewQuery.getText().toString();
        String newQuery = String.valueOf(((Button) v).getText());

        String q = "+-x/";

        // verify that query entered is mathematically correct
        if (q.contains(newQuery) && query.length() > 0) {
            String lastChar = String.valueOf(query.charAt(query.length() - 1));
            if (q.contains(lastChar)) {
                textViewQuery.setText(String.format("%s", query));
            } else {
                textViewQuery.setText(String.format("%s%s", query, newQuery));
                equal.setEnabled(false);
            }
        } else {
            Boolean correct = false;
            for (int i=0; i < query.length(); i++) {
                String CurrentChar = String.valueOf(query.charAt(i));
                if (q.contains(CurrentChar)) {
                    correct = true;
                }
            }
            if (correct) {
                equal.setEnabled(true);
            } else {
                equal.setEnabled(false);
            }
            textViewQuery.setText(String.format("%s%s", query, newQuery));

        }
    }

    /**
     * @param v
     *
     * Places at first the numbers and operations in 2 different stacks
     * When stacks are completed, it calculates the value of query
     * At last updates the result text
     *
     * Called by equal button
     */
    public void calculate(View v) {
        String query = getQuery();
        // instantiate and execute calculatorTask
        new CalculatorTask().execute(query);
    }

    public String getQuery() {
        String query = String.format("%s", textViewQuery.getText());
        String q = "+-x/";

        if (q.contains(String.valueOf(query.charAt(0)))) {
            String formerResult = result.getText().toString();
            if (String.valueOf(formerResult.charAt(0)).equals("-")) {
                formerResult = "0" + formerResult;
            }
            query = formerResult + query;
        }
        return query;
    }

    public class CalculatorTask extends AsyncTask<String, Integer, Float> {
        Deque<String> stackop = new ArrayDeque<>();
        Deque<Float> stackva = new ArrayDeque<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Float doInBackground(String... strings) {
            queryText = strings[0];
            float answer = 0;
            try (Socket socket = new Socket("10.0.2.2", 9876)){
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // sending data to server
                try {
                    dos.writeUTF(queryText);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // We were cancelled; stop sleeping!
                }
                try {
                    answer = dis.readFloat();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // We were cancelled; stop sleeping!
                }

            } catch (UnknownHostException exception) {
                System.out.print(exception);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultTetx = String.valueOf(answer);
            return answer;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


        @Override
        protected void onPostExecute(Float resultValue) {
            progressBar.setVisibility(View.INVISIBLE);
            result.setText(String.format("%f", resultValue));
            textViewQuery.setText("");
        }
    }
}

