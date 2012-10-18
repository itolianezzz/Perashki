package ru.spb.itolia.perashki.ui;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.R;
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
 * Date: 11.10.12
 * Time: 21:55
 */
public class BaseFragment extends SherlockFragment implements IShowedFragment {
    private static final String TAG = "Perashki.BaseFragment";
    private Map params = new HashMap<String, String>();
    private String type;
    private List<Piro> piros;
    private int current_page;

    @Override
    public void onShowedFragment() {

    }

    public void setParams(Map<String, String> params) {

    }

    public void populateView(){

    }

    public Boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else
            return false;
    }

    public List<Piro> loadPiros(Map<String, String> params) {
        List<Piro> piros = null;
        try {
            Log.v(TAG, "type is: " + type);
            Log.v(TAG, "Params in loadPiros: " + params.size());
            piros = PiroLoader.getPiros(params);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return piros;
    }

    public void showConnectionProblemsPopup() {
        Toast.makeText(getActivity(), getResources().getString(R.string.connection_problems_toast), Toast.LENGTH_SHORT).show();
    }

    public class LoadPirosBaseTask extends AsyncTask<Map<String, String>, Void, List<Piro>> {

        @Override
        protected void onPreExecute() {
            //To be overriden in child classes
        }

        @Override
        protected List<Piro> doInBackground(Map<String, String>... parameters) {
            Map<String, String> params = parameters[0];
            if(isConnectedToInternet()) {
                params.put(ParamTypes.PAGE, Integer.toString(current_page));
                piros = loadPiros(params);
                return piros;
            } else {
                piros = new ArrayList<Piro>();
                return piros;
            }
        }

        @Override
        protected void onPostExecute(List<Piro> piros) {
            //To be overriden in child classes
        }
    }
}
