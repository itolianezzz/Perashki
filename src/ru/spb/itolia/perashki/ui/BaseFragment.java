package ru.spb.itolia.perashki.ui;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.R;
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
 * Date: 11.10.12
 * Time: 21:55
 */
public class BaseFragment extends SherlockFragment implements IShowedFragment {
    private static final String TAG = "Perashki.BaseFragment";
    private Map params = new HashMap<String, String>();
    private String type;

    @Override
    public void onShowedFragment() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParams(Map<String, String> params) {

    }

    public void populateView(){

    }

    public Boolean isConnectedToInternet() {
        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    public List<Piro> loadPiros(Map<String, String> params) {
        List<Piro> piros = null;
        try {
            Log.v(TAG, "type is: " + type);
            piros = PiroLoader.getPiros(params);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return piros;
    }

    public void showConnectionProblemsPopup() {
        Toast.makeText(getActivity(), getResources().getString(R.string.connection_problems_toast), Toast.LENGTH_SHORT).show();
    }
}
