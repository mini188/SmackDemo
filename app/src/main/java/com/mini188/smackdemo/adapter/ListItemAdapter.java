package com.mini188.smackdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mini188.smackdemo.R;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;

/**
 * Created by xiexb on 2016/04/06.
 */
public class ListItemAdapter extends ArrayAdapter<RosterEntry> {
    private Activity _activity;

    public ListItemAdapter(Activity activity, List<RosterEntry> objects) {
        super(activity, 0, objects);

        this._activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RosterEntry item = getItem(position);

        if (view == null) {
             view = inflater.inflate(R.layout.contract, null);
        }

        TextView tvName = (TextView) view.findViewById(R.id.contact_display_name);
        TextView tvJid = (TextView) view.findViewById(R.id.contact_jid);
        tvName.setText(item.getName());
        tvJid.setText(item.getUser());

        return view;
    }
}
