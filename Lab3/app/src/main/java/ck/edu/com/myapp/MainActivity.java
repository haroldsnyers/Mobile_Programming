package ck.edu.com.myapp;

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

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    private TextView textViewQuery;
    private TextView result;

    private Button equal;

    private Handler handler;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewQuery = findViewById(R.id.input);
        result = findViewById(R.id.result);

        equal = findViewById(R.id.btnequals);


        progressBar = findViewById(R.id.progressBar1);
        handler = new Handler();

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
        if (id == R.id.action_settings) {
            return true;
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

        if (q.contains(newQuery) && query.length() > 0) {
            String lastChar = String.valueOf(query.charAt(query.length() - 1));
            if (q.contains(lastChar)) {
                textViewQuery.setText(String.format("%s", query));
            } else {
                textViewQuery.setText(String.format("%s%s", query, newQuery));
                equal.setEnabled(false);
            }
        } else {
            textViewQuery.setText(String.format("%s%s", query, newQuery));
            equal.setEnabled(true);
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
        String query = String.format("%s", textViewQuery.getText());
        progressBar.setProgress(0);
        String q = "+-x/";

        if (q.contains(String.valueOf(query.charAt(0)))) {
            String formerResult = result.getText().toString();
            query = formerResult + query;
            Log.d("QUERY", query);
            Log.d("FRESULT", formerResult);
        }

        new CalculatorTask().execute(query);
    }


    public class CalculatorTask extends AsyncTask<String, Integer, Float> {
        Deque<String> stackop = new ArrayDeque<>();
        Deque<Float> stackva = new ArrayDeque<>();

        @Override
        protected Float doInBackground(String... strings) {
            float result = 0.0f;
            try {
                result = sortQuery(strings[0]);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // We were cancelled; stop sleeping!
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


        @Override
        protected void onPostExecute(Float resultValue) {
            result.setText(String.format("%f", resultValue));
            textViewQuery.setText("");
        }

        private float sortQuery(String query) {
            Deque<Character> stackvaPro = new ArrayDeque<>();

            String q = "+-x/";

            int length = query.length();

            for (int i = 0; i < length; i++) {
                char c = query.charAt(i);
                String s = String.valueOf(c);
                if (!q.contains(s))
                    stackvaPro.push(c);
                else if (q.contains(s)) {
                    stackop.push(s);
                    addNumber(stackva, stackvaPro);
                }
                if (i == length -1) {
                    addNumber(stackva, stackvaPro);
                }
                publishProgress((int) ((i / (float) length + 1) * 100));
            }
            for(String s : stackop) {
                Log.d("STATE", s);

            }
            for(float s : stackva) {
                Log.d("STAT", Float.toString(s));
            }
            return calculator(stackop, stackva, length);
        }

        /**
         * @param stackva final queue of real numbers
         * @param stackvaPro provisional queue of numbers
         */
        private void addNumber(Deque<Float> stackva, Deque<Character> stackvaPro) {
            Deque<Character> stackvaProv = new ArrayDeque<Character>();
            int n = 0;
            for (char item: stackvaPro) {
                stackvaProv.push(stackvaPro.pop());
                n += 1;
            }
            char newArr[] = new char[n];
            for (int j = 0; j < n; j++) {
                newArr[j] = stackvaProv.pop();
            }
            String b = new String(newArr);
            float value = Float.parseFloat(b);
            stackva.push(value);
        }

        /**
         * @param stackop queue of the different operations
         * @param stackva queue of the different numbers (1 digit numbers)
         * @return result of the query
         */
        private float calculator(Deque<String> stackop, Deque<Float> stackva, int length) {
            String opeDM = "/x";
            Deque<Float> stackvaPM = new ArrayDeque<>();
            Deque<String> stackopPM = new ArrayDeque<>();

            for(String s : stackop) {
                if (stackvaPM.peek() == null) {
                    stackvaPM.push(stackva.pop());
                }
                if (opeDM.contains(s)) {
                    String ope = stackop.pop();
                    float val = stackvaPM.pop();
                    if (ope.equals("x")) {
                        stackvaPM.push(val * stackva.pop());
                    } else {
                        stackvaPM.push(val / stackva.pop());
                    }
                } else {
                    stackopPM.push(stackop.pop());
                    stackvaPM.push(stackva.pop());
                }
            }
            publishProgress((int) ((length / (float) length + 1) * 100));
            float result = 0;
            int first = 0;
            for(String s : stackopPM) {
                Log.d("STATES", s);

            }
            for(float s : stackvaPM) {
                Log.d("STATESS", Float.toString(s));
            }
            // case no plus +/- operator, then only one value in queue, which is the final result
            if (stackopPM.peek() == null) {
                result = stackvaPM.pop();
            } else {
                // sum up the last numbers left
                for(String s : stackopPM) {
                    if (first == 0) {
                        result = stackvaPM.pop();
                    }
                    if (s.equals("+")) {
                        result = result + stackvaPM.pop();
                    } else {
                        result = result - stackvaPM.pop();
                    }
                    first = 1;
                }
            }
            publishProgress((int) ((length + 1 / (float) length + 1) * 100));
            return result;
        }
    }
}
