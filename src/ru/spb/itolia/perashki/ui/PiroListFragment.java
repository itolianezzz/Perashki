package ru.spb.itolia.perashki.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.PiroLoader;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.beans.piroType;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 06.10.12
 * Time: 15:24
 */
public class PiroListFragment extends SherlockFragment {
    private static final String TAG = "Perashki.PiroListFragment";
    private ProgressBar progress;
    private ListView list;
    private piroType type;

    public PiroListFragment(){
        }

    public PiroListFragment(piroType type) {
        Log.v(TAG, "PiroListFragment instantiated! " + this.toString());
        this.type = type;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "OnCreateView is called!");
        View view = inflater.inflate(R.layout.piro_list_view, container, false);
        list = (ListView) view.findViewById(R.id.piro_list);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);
        populateView();
        return view;
    }

    public void populateView(){
        Log.v(TAG, "populateView called");
        LoadPirosTask pirosTask = new LoadPirosTask();
        pirosTask.execute();
    }

    private class LoadPirosTask extends AsyncTask<Integer , Void, List<Piro>> {


        protected void onPreExecute(){
            progress.setIndeterminate(true);
            //dialog = ProgressDialog.show(getSherlockActivity(), "", "Loading. Please wait...", true);  //TODO Define string in strings.xml
        }

        @Override
        protected List<Piro> doInBackground(Integer... params) {
            List<Piro> piros = null;
            try {
                switch (type){
                    case NEW:
                        piros = PiroLoader.getNew();
                        break;
                    case GOOD:
                        piros = PiroLoader.getGood();
                        break;
                    case BEST:
                        piros = PiroLoader.getBest();
                        break;
                    case RANDOM:
                        piros = PiroLoader.getRandom();
                        break;
                    case ALL:
                        piros = PiroLoader.getAll();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return piros;
        }

        @Override
        protected void onPostExecute(List<Piro> piros){
            progress.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            PiroAdapter mAdapter = new PiroAdapter(getSherlockActivity(), piros);
            list.setAdapter(mAdapter);

        }
    }

}
