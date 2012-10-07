package ru.spb.itolia.perashki.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListFragment;
import ru.spb.itolia.perashki.PiroLoader;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.adapters.PiroAdapter;
import ru.spb.itolia.perashki.beans.Piro;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 06.10.12
 * Time: 15:24
 */
public class PiroListFragment extends SherlockListFragment {

    private ListView list;

    public PiroListFragment() {
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piro_list_view, container, false);
        //list = (ListView) view.findViewById(R.id.list);
        LoadPirosTask pirosTask = new LoadPirosTask();
        pirosTask.execute();
        return view;
    }

    private class LoadPirosTask extends AsyncTask<Void, Void, List<Piro>> {

        @Override
        protected List<Piro> doInBackground(Void... params) {
            List<Piro> piros = null;
            try {
                piros = PiroLoader.getGood();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return piros;
        }

        @Override
        protected void onPostExecute(List<Piro> piros){
            PiroAdapter mAdapter = new PiroAdapter(getActivity(), piros);
            setListAdapter(mAdapter);
        }
    }

}
