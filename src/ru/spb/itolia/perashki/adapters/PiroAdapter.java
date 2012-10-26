package ru.spb.itolia.perashki.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import ru.spb.itolia.perashki.R;
import ru.spb.itolia.perashki.beans.Piro;
import ru.spb.itolia.perashki.util.PiroLoader;

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
        private TextView piro_author;
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
        holder.piro_author = (TextView) rowView.findViewById(R.id.author_name);
        final Piro p = piros.get(position);
        holder.piro_text.setText(p.getText());
        holder.piro_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePiro(p);
            }
        });
        holder.piro_author.setText(p.getAuthor());
        holder.piro_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthorInfoDialog(p.getAuthor());
            }
        });
        return rowView;
    }

    private void showAuthorInfoDialog(final String author) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.author_info_dialog, null);
        builder.setView(view);
        builder.setTitle(R.string.auhtor_info);
        class LoadAuthorInfoTask extends AsyncTask<Void, Void, String> {
            ProgressBar progress;
            TextView info_text;

            @Override
            protected void onPreExecute() {
                progress = (ProgressBar) view.findViewById(R.id.author_info_progress);
                info_text = (TextView) view.findViewById(R.id.load_author_info);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    return PiroLoader.getAuthorInfo(author);
                } catch (Exception e) {
                    return new String(getContext().getResources().getString(R.string.load_info_failed));
                }
            }

            @Override
            protected void onPostExecute(String author_info) {
                if(author_info.isEmpty()){
                    progress.setVisibility(View.GONE);
                    info_text.setVisibility(View.VISIBLE);
                } else {
                    progress.setVisibility(View.GONE);
                    info_text.setVisibility(View.VISIBLE);
                    info_text.setMaxLines(15);
                    info_text.setVerticalScrollBarEnabled(true);
                    info_text.setMovementMethod(new ScrollingMovementMethod());
                    info_text.setText(Html.fromHtml(author_info));
                }
            }
        }
        LoadAuthorInfoTask task = new LoadAuthorInfoTask();
        task.execute();
        builder.create().show();
    }



    protected void sharePiro(Piro piroToShare) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = piroToShare.getText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, PiroLoader.HOST + "piro/" + piroToShare.getId());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        getContext().startActivity(Intent.createChooser(sharingIntent, getContext().getResources().getString(R.string.share_label)));
    }
}
