package ru.spb.itolia.perashki.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.ParamTypes;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.util.IShowedFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 10.10.12
 * Time: 19:22
 */
public class SearchFragment extends PiroListFragment implements IShowedFragment {
    private static final String TAG = "Perashki.SearchFragment";
    private EditText searchStringEdit;
    private Button searchButton;
    private ProgressBar searchProgress;

    public SearchFragment() {
        params = new HashMap<String, String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piro_search_view, container, false);
        params  = new HashMap<String, String>();
        piros = new ArrayList<Piro>();
        searchStringEdit = (EditText) view.findViewById(R.id.search_string_edit);
        searchButton = (Button) view.findViewById(R.id.search_button);
        list = (ListView) view.findViewById(R.id.search_results_list);
        noPiros = (TextView) view.findViewById(R.id.no_piros_text_view);
        searchProgress = (ProgressBar) view.findViewById(R.id.search_progress);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchStringEdit.getText().toString().trim().isEmpty()) {
                    last_page = 0;
                    piros.clear();
                    populateView();
                }
            }
        });
        loadMoreView = View.inflate(getActivity(), R.layout.load_more_view, null);
        loadMorePirosProgress = (ProgressBar) loadMoreView.findViewById(R.id.load_more_progress);
        loadMorePirosText = (TextView) loadMoreView.findViewById(R.id.load_more_text);
        loadMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                current_page += 1;
                params.put(ParamTypes.PIROTYPE, ParamTypes.ALL);
                new LoadMorePirosTask().execute(params);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Piro piroToShare = mAdapter.getItem(position);
                sharePiro(piroToShare);
            }
        });

        return view;
    }

    @Override
    public void populateView() {
        current_page = 1;
        piros.clear();
        new SearchPirosTask().execute(params);
    }

    @Override
    public void onShowedFragment() {
        if(!list.isShown()) {
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

    private class SearchPirosTask extends LoadPirosTask {

        @Override
        protected void onPreExecute(){
            String searchString = searchStringEdit.getText().toString();
            noPiros.setVisibility(View.GONE);
            searchProgress.setVisibility(View.VISIBLE);
            searchButton.setEnabled(false);
            searchStringEdit.setEnabled(false);
            list.setVisibility(View.GONE);
            params.put(ParamTypes.TEXT, searchString);
            params.put(ParamTypes.PIROTYPE, ParamTypes.ALL);
        }

        @Override
        protected void onPostExecute(List<Piro> pirosToAdd){
            searchProgress.setVisibility(View.GONE);
            if(!pirosToAdd.isEmpty()) {
                list.setVisibility(View.VISIBLE);
                fixFooter();
                piros.addAll(pirosToAdd);
                mAdapter = new PiroAdapter(getActivity(), piros);
                list.setAdapter(mAdapter);
            } else if(last_page == 0) {
                noPiros.setVisibility(View.VISIBLE);
                showNoPirosToast();
            } else {
                showConnectionProblemsPopup();
                noPiros.setVisibility(View.VISIBLE);
            }
            searchStringEdit.setEnabled(true);
            searchButton.setEnabled(true);
        }
    }

    protected class LoadMorePirosTask extends LoadPirosTask {

        protected void onPreExecute() {
            loadMorePirosText.setVisibility(View.GONE);
            loadMorePirosProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(List<Piro> pirosToAdd) {
            //super.onPostExecute(pirosToAdd);
            int currentPosition = list.getFirstVisiblePosition();
            int top = list.getChildAt(0).getTop();
            if(!pirosToAdd.isEmpty()) {
                loadMorePirosText.setText(R.string.load_more_label);
                loadMorePirosText.setVisibility(View.VISIBLE);
                fixFooter();
                piros.addAll(pirosToAdd);
                mAdapter = new PiroAdapter(getSherlockActivity(), piros);
                list.setAdapter(mAdapter);
            } else {
                showConnectionProblemsPopup();
                loadMorePirosText.setText(R.string.load_piros_fail);
                loadMorePirosText.setVisibility(View.VISIBLE);
            }
            loadMorePirosProgress.setVisibility(View.GONE);
            list.setSelectionFromTop(currentPosition, top);
        }
    }

    private void showNoPirosToast() {
        Toast.makeText(getActivity(), getResources().getString(R.string.no_piros_found_toast), Toast.LENGTH_SHORT).show();
    }
}
