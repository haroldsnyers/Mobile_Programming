package ck.edu.com.myfirstapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Harold
 * @version 1.01
 * @since 2020-01-28
 */
public class MainActivity extends AppCompatActivity {
    private TextView textViewQuery;
    private TextView result;

    private Button equal;

    private Deque<String> stackop = new ArrayDeque<>();
    private Deque<Float> stackva = new ArrayDeque<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewQuery = findViewById(R.id.input);
        result = findViewById(R.id.result);

        LinearLayout layout = findViewById(R.id.layoutCalc);

        Button btnEqual = new Button(this);
        btnEqual.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        btnEqual.setText(R.string.equal);
        btnEqual.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                calculate(v);
            }
        });
        btnEqual.setId(R.id.btnequals);
        btnEqual.setEnabled(false);

        layout.addView(btnEqual);

        equal = findViewById(R.id.btnequals);
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
        String query = String.format("%s", textViewQuery.getText());

        String q = "+-x/";

        if (q.contains(String.valueOf(query.charAt(0)))) {
            String formerResult = result.getText().toString();
            if (String.valueOf(formerResult.charAt(0)).equals("-")) {
                formerResult = "0" + formerResult;
            }
            query = formerResult + query;
        }


        Float resultValue = sortQuery(query);
        result.setText(String.format("%f", resultValue));
        textViewQuery.setText("");
    }

    /**
     * @param query String containing the mathematical query to calculate
     * @return result value after calculation
     */
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
        }
        return calculator(stackop, stackva);
    }


    /**
     * @param stackva final queue of real numbers
     * @param stackvaPro provisional queue of numbers
     */
    private void addNumber(Deque<Float> stackva, Deque<Character> stackvaPro) {
        Deque<Character> stackvaProv = new ArrayDeque<>();
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
        Float value = Float.parseFloat(b);
        stackva.push(value);
    }


    /**
     * @param stackop queue of the different operations
     * @param stackva queue of the different numbers (1 digit numbers)
     * @return result of the query
     */
    private float calculator(Deque<String> stackop, Deque<Float> stackva) {
        String opeDM = "/x";
        Deque<Float> stackvaPM = new ArrayDeque<>();
        Deque<String> stackopPM = new ArrayDeque<>();

        // calculate the different multiply/divide operators and add result to another queue
        // to apply the plus/minus operators after that
        for(String s : stackop) {
            if (stackvaPM.peek() == null) {
                stackvaPM.push(stackva.pop());
            }
            if (opeDM.contains(s)) {
                String ope = stackop.pop();
                Float val = stackvaPM.pop();
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
        Float result = 0.0f;
        int first = 0;
//        for(String s : stackopPM) {
//            Log.d("STATES", s);
//
//        }
//        for(Float s : stackvaPM) {
//            Log.d("STATESS", Float.toString(s));
//        }
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
        return result;
    }


    // old calculator not taking in consideration order of mathematical operations
    public void oldCalculate (View v) {
        String query = String.format("%s", textViewQuery.getText());
        String q = "+-x/";
        Integer int1 = 0;
        Integer int2 = 0;
        String quer = "";
        for (int i = 0; i < query.length(); i++){
            char c = query.charAt(i);
            String s = String.valueOf(c);
            //Process char
            if (!q.contains(s)) {
                if (int1 == 0){
                    int1 = Integer.parseInt(s);
                } else {
                    int2 = Integer.parseInt(s);
                }
            } else {
                quer = s;
            }
            Integer int3 = 0;
            if (int2 != 0) {
                if (quer.equals("/")) {
                    int3 = int1 / int2;
                } else if (quer.equals("x")) {
                    int3 = int1 * int2;
                } else if (quer.equals("+")) {
                    int3 = int1 + int2;
                } else if (quer.equals("-")) {
                    int3 = int1 - int2;
                }
                result.setText(String.valueOf(int3));
                int1 = int3;
                int2 = 0;
            }
        }
        textViewQuery.setText("");
    }
}
