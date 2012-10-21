package ru.spb.itolia.perashki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.beans.Piro;

import java.util.List;

/**
* Created with IntelliJ IDEA.
* User: itolia
* Date: 07.10.12
* Time: 12:14
*/
public class PiroAdapter extends ArrayAdapter<Piro> {
    private Context context;
    private List<Piro> piros;

    public PiroAdapter(Context context, List<Piro> piros) {
        super(context, R.layout.piro_list_item, R.id.piro_text, piros);
        this.context = context;
        this.piros = piros;
    }

    class ViewHolder {
        private TextView piro_text;
        private TextView number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.piro_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.piro_text = (TextView) rowView.findViewById(R.id.piro_text);
        holder.number = (TextView) rowView.findViewById(R.id.piro_number);
        Piro p = piros.get(position);
        holder.piro_text.setText(p.getText());
        holder.number.setText(Integer.toString(position+1));
        return rowView;
    }
}
