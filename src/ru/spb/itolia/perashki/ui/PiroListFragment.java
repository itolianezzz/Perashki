package ru.spb.itolia.perashki.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import ru.spb.itolia.perashki.R;

/**
 * Created with IntelliJ IDEA.
 * User: itolia
 * Date: 06.10.12
 * Time: 15:24
 */
public class PiroListFragment extends SherlockFragment {

    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piro_list_view, container, false);
        list = (ListView) view.findViewById(R.id.piro_list);

        return view;
    }
}
