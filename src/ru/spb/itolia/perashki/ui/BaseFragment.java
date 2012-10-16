package ru.spb.itolia.perashki.ui;

import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.util.IShowedFragment;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 11.10.12
 * Time: 21:55
 */
public class BaseFragment extends SherlockFragment implements IShowedFragment {
    @Override
    public void onShowedFragment() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setParams(Map<String, String> params) {

    }

    public void populateView(){

    }
}
