package ck.edu.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Calculator extends AppCompatActivity implements CalculatorFragment.OnFragmentInteractionListener {
    private CalculatorFragment calculatorFragment;
    private TextViewFragment textViewFragment;
    private LinearLayout myLinearLayout;
    // private firstFragment first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            textViewFragment = (TextViewFragment) getSupportFragmentManager().getFragment(savedInstanceState, "text");
            calculatorFragment = (CalculatorFragment) getSupportFragmentManager().getFragment(savedInstanceState, "calc");
        } else {
            textViewFragment = new TextViewFragment();
            calculatorFragment = new CalculatorFragment();
        }

        myLinearLayout= (LinearLayout) findViewById(R.id.linearLayoutFrame);

        FragmentTransaction fgt =
                getSupportFragmentManager().beginTransaction();
        fgt.replace(R.id.myFrame1, textViewFragment, "text");
        fgt.replace(R.id.myFrame2, calculatorFragment, "calc");
        fgt.commit();

        if (getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels) {
            myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            myLinearLayout.setOrientation(LinearLayout.VERTICAL);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "text", textViewFragment);
        getSupportFragmentManager().putFragment(outState, "calc", calculatorFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculator, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editText) {
            Context context = Calculator.this;
            Class destinationActivity = MainActivity.class;
            Intent startChildActivityIntent = new Intent(context, destinationActivity);
            startActivity(startChildActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculate(View view){
        calculatorFragment.calculate(view);
    }

    public void updateText(View view) {
        calculatorFragment.updateText(view);
    }

    @Override
    public void onTextChange(String query) {
        if (textViewFragment != null) {
            textViewFragment.setTextViewQuery(query);
        } else {
            Toast.makeText(getApplicationContext(), "fragment 2  is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResultCalculated(String result, String query) {
        if (textViewFragment != null) {
            reOrder();
            textViewFragment.setResultView(result);
            textViewFragment.setFormerInput(query);
        } else {
            Toast.makeText(getApplicationContext(), "fragment 2  is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void reOrder() {

        int childcount = myLinearLayout.getChildCount();
        // create array
        View[] children = new View[childcount];

        // get children of linearlayout
        for (int i=0; i < childcount; i++){
            children[i] = myLinearLayout.getChildAt(i);
        }

        //now remove all children
        myLinearLayout.removeAllViews();

        //and resort, first position
        myLinearLayout.addView(children[1]);
        //second position
        myLinearLayout.addView(children[0]);
        //etc.
    }

}
