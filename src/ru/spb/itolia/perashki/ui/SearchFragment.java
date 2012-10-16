package ru.spb.itolia.perashki.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.ParamTypes;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.util.IShowedFragment;
import ru.spb.itolia.perashki.util.PiroLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 10.10.12
 * Time: 19:22
 */
public class SearchFragment extends BaseFragment implements IShowedFragment {
    private static final String TAG = "Perashki.SearchFragment";
    private EditText searchStringEdit;
    private Button searchButton;
    private ListView resultsList;
    private ProgressBar searchProgress;
    private Map params;
    List<Piro> list;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piro_search_view, container, false);
        searchStringEdit = (EditText) view.findViewById(R.id.search_string_edit);
        searchButton = (Button) view.findViewById(R.id.search_button);
        resultsList = (ListView) view.findViewById(R.id.search_results_list);
        searchProgress = (ProgressBar) view.findViewById(R.id.search_progress);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchStringEdit.getText().toString().trim().isEmpty()) {
                    performSearch();
                }
            }
        });
        return view;
    }

    protected void performSearch() {
        new SearchPirosTask().execute();
    }

    @Override
    public void onShowedFragment() {
        if(!resultsList.isShown()) {
            searchStringEdit.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    @Override
    public void setParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause()");
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchStringEdit.getWindowToken(), 0);
    }

    private class SearchPirosTask extends AsyncTask<Void, Void, List<Piro>> {   //TODO perform search AsyncTask

        @Override
        protected void onPreExecute(){
            String searchString = searchStringEdit.getText().toString();
            searchProgress.setVisibility(View.VISIBLE);
            searchButton.setEnabled(false);
            searchStringEdit.setEnabled(false);
            resultsList.setVisibility(View.GONE);
            params = new HashMap<String, String>();
            params.put(ParamTypes.TEXT, searchString);
            params.put(ParamTypes.PIROTYPE, ParamTypes.ALL);
        }

        @Override
        protected List<Piro> doInBackground(Void... parameters) {

            try {
                list = PiroLoader.getPiros(params);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(List<Piro> pirosToAdd){
            searchProgress.setVisibility(View.GONE);
            resultsList.setVisibility(View.VISIBLE);
            searchStringEdit.setEnabled(true);
            searchButton.setEnabled(true);
            PiroAdapter adapter = new PiroAdapter(getActivity(), list);
            resultsList.setAdapter(adapter);

        }
    }
}
