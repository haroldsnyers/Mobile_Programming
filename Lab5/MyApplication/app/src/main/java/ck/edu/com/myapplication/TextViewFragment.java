package ck.edu.com.myapplication;

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
import android.widget.TextView;

// here starts the fragment_first class code
public class TextViewFragment extends Fragment {

    private TextView textViewQuery;
    private TextView resultView;
    private TextView formerInput;

    public TextViewFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment_first using the provided parameters.
     *
     * @return A new instance of fragment_first firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextViewFragment newInstance() {
        return new TextViewFragment();
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
        View view = inflater.inflate(R.layout.fragment_textview, container,
                false);
        textViewQuery = view.findViewById(R.id.input);
        formerInput = view.findViewById(R.id.formerInput);
        resultView = view.findViewById(R.id.result);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        //mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }

    public void setTextViewQuery(String string) {
        textViewQuery.setText(string);
    }

    public void setResultView (String string) {
        resultView.setText(string);
    }

    public void setFormerInput(String string) {
        formerInput.setText(string);
    }

}