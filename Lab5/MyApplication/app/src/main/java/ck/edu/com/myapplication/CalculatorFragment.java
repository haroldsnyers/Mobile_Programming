package ck.edu.com.myapplication;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Deque;

// here starts the fragment_first class code
public class CalculatorFragment extends Fragment {

    private String queryText = "";
    private String formerQueryText;
    private String resultText;

    private Button equal;

    private ProgressBar progressBar;

    private OnFragmentInteractionListener mListener;
    public CalculatorFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment_first using the provided parameters.
     *
     * @return A new instance of fragment_first firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance() {
        return new CalculatorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment_first
        View view = inflater.inflate(R.layout.fragment_calculator, container,
                false);
        equal = view.findViewById(R.id.btnequals);
        equal.setEnabled(false);

        progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment_first to allow an interaction in this fragment_first to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTextChange(String editText);

        void onResultCalculated(String result, String query);
    }

    /**
     * @param v
     *
     * update in real time mathematical query
     *
     * Called by every button on calculator except equal
     */
    public void updateText(View v){
        String query = queryText;
        String newQuery = String.valueOf(((Button) v).getText());

        String q = "+-x/";

        // verify that query entered is mathematically correct
        if (q.contains(newQuery) && query.length() > 0) {
            String lastChar = String.valueOf(query.charAt(query.length() - 1));
            if (q.contains(lastChar)) {
                queryText = query;
            } else {
                queryText = queryText + newQuery;
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
            queryText = query + newQuery;
        }
        if (mListener != null) {
            mListener.onTextChange(queryText);
        }
    }

    /**
     *
     * Places at first the numbers and operations in 2 different stacks
     * When stacks are completed, it calculates the value of query
     * At last updates the result text
     *
     * Called by equal button
     */
    public void calculate(View view) {
        String query = getQuery();
        // instantiate and execute calculatorTask
        new CalculatorTask().execute(query);
    }

    public String getQuery() {
        String query = String.format("%s", queryText);
        String q = "+-x/";

        if (q.contains(String.valueOf(query.charAt(0)))) {
            String formerResult = resultText;
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
            return answer;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


        @Override
        protected void onPostExecute(Float resultValue) {
            progressBar.setVisibility(View.INVISIBLE);
            resultText = String.valueOf(resultValue);
            formerQueryText = queryText;
            mListener.onResultCalculated(resultText, formerQueryText);
            queryText = "";
            mListener.onTextChange(queryText);
        }
    }
}
