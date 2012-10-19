package ru.spb.itolia.perashki.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.ParamTypes;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.util.IShowedFragment;
import ru.spb.itolia.perashki.util.PiroLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 06.10.12
 * Time: 15:24
 */
public class PiroListFragment extends SherlockFragment implements IShowedFragment {
    private static final String TAG = "Perashki.PiroListFragment";
    protected Map params;
    private ProgressBar progress;
    private ProgressBar loadMorePirosProgress;
    private TextView loadMorePirosText;
    private TextView noPiros;
    private ListView list;
    private View loadMoreView;
    private String type;
    private int current_page = 1;
    private int last_page;
    private List<Piro> piros;
    private PiroAdapter mAdapter;
    private LoadPirosTask pirosTask;

    public PiroListFragment() {
        Log.v(TAG, "PirolistFragment empty constructor: " + this.toString());
    }

    public PiroListFragment(String type) {
        Log.v(TAG, "PiroListFragment instantiated! " + this.toString());
        this.type = type;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume called!");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
        Log.v(TAG, "OnSaveInstansceState!");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause called!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "OnCreateView is called!");
        if(savedInstanceState != null){
            Log.v(TAG, "Bundle here!");
            type = savedInstanceState.getString(ParamTypes.PIROTYPE);
        }
        params  = new HashMap<String, String>();
        View view = inflater.inflate(R.layout.piro_list_view, container, false);
        list = (ListView) view.findViewById(R.id.piro_list);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);
        noPiros = (TextView) view.findViewById(R.id.no_piros_text_view);
        loadMoreView = View.inflate(getActivity(), R.layout.load_more_view, null);
        loadMorePirosProgress = (ProgressBar) loadMoreView.findViewById(R.id.load_more_progress);
        loadMorePirosText = (TextView) loadMoreView.findViewById(R.id.load_more_text);
        loadMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
               new LoadMorePirosTask().execute(params);
            }
        });
        list.setOnItemClickListener(new
AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Piro piroToShare = mAdapter.getItem(position);
                sharePiro(piroToShare);

            }
        });
        if (type.equals(ParamTypes.GOOD) & current_page < 2) {
            populateView();
        }
        return view;
    }

    private void sharePiro(Piro piroToShare) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = piroToShare.getText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, PiroLoader.HOST + piroToShare.getId()); //TODO fix wrong URL
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_label)));
    }

    @Override
    public void onShowedFragment() {
        if (current_page < 2 & list.getAdapter() == null) {
            populateView();
        }
    }

    public void populateView() {
        Log.v(TAG, "populateView called");
        params.put(ParamTypes.PIROTYPE, type);
        pirosTask = new LoadPirosTask();
        pirosTask.execute(params);

    }

    public void setParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    protected class LoadPirosTask extends AsyncTask<Map<String, String>, Void, List<Piro>> {


        protected void onPreExecute() {
            noPiros.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }

        @Override
        protected List<Piro> doInBackground(Map<String, String>... parameters) {
            Map<String, String> params = parameters[0];
            if(isConnectedToInternet()) {
                params.put(ParamTypes.PAGE, Integer.toString(current_page));
                piros = loadPiros(params);
                if(last_page < 1) {
                    last_page = PiroLoader.getPages();
                }
                return piros;
            } else {
                piros = new ArrayList<Piro>();
                return piros;
            }
        }

        @Override
        protected void onPostExecute(List<Piro> piros) {
            Log.v(TAG, "onPostExecute() called: " + PiroListFragment.this.type);
            progress.setVisibility(View.GONE);
            if(!piros.isEmpty()) {
                progress.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
                if(current_page < last_page){
                    list.addFooterView(loadMoreView);
                }
                mAdapter = new PiroAdapter(getSherlockActivity(), piros);
                list.setAdapter(mAdapter);
            } else {
                showConnectionProblemsPopup();
                noPiros.setVisibility(View.VISIBLE);
            }
       }
    }

    protected class LoadMorePirosTask extends AsyncTask<Map<String, String>, Void, List<Piro>> {

        protected void onPreExecute() {
            loadMorePirosText.setVisibility(View.GONE);
            loadMorePirosProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Piro> doInBackground(Map<String, String>... parameters) {
            Map<String, String> params = parameters[0];
            params.put(ParamTypes.PIROTYPE, type);
            List<Piro> pirosToAdd = new ArrayList<Piro>();
            if(!isConnectedToInternet()) {
                current_page -= 1;
                return pirosToAdd;
            } else {
                Log.v(TAG, "Params before loadmorepiros: " + params.size());
                params.put(ParamTypes.PAGE, Integer.toString(current_page += 1));
                pirosToAdd  = loadPiros(params);
                return pirosToAdd;
            }
        }

        @Override
        protected void onPostExecute(List<Piro> pirosToAdd) {
            super.onPostExecute(pirosToAdd);
            int currentPosition = list.getFirstVisiblePosition();
            if(!pirosToAdd.isEmpty()) {
                loadMorePirosText.setText(R.string.load_more_label);
                loadMorePirosText.setVisibility(View.VISIBLE);
                piros.addAll(pirosToAdd);
                mAdapter = new PiroAdapter(getSherlockActivity(), piros);
                list.setAdapter(mAdapter);
            } else {
                showConnectionProblemsPopup();
                loadMorePirosText.setText(R.string.load_piros_fail);
                loadMorePirosText.setVisibility(View.VISIBLE);
            }
            loadMorePirosProgress.setVisibility(View.GONE);
            list.setSelectionFromTop(currentPosition + 1, 0);
        }
    }

    public List<Piro> loadPiros(Map<String, String> params) {
        List<Piro> piros = null;
        try {
            piros = PiroLoader.getPiros(params);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return piros;
    }

    public Boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else
            return false;
    }

    public void showConnectionProblemsPopup() {
        Toast.makeText(getActivity(), getResources().getString(R.string.connection_problems_toast), Toast.LENGTH_SHORT).show();
    }
}
