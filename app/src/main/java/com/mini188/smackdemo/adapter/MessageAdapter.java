package com.mini188.smackdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mini188.smackdemo.ChatActivity;
import com.mini188.smackdemo.R;

import org.jivesoftware.smack.packet.Message;

import java.util.List;

/**
 * Created by xiexb on 2016/04/07.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private Activity _activity;

    public  MessageAdapter(ChatActivity activity, List<Message> messageList){
        super(activity, 0, messageList);
        _activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Message message = getItem(position);
        if ("xiexb@zrtc".equals(message.getTo())) {
            view = _activity.getLayoutInflater().inflate(R.layout.message_received, parent, false);
        }
        else {
            view = _activity.getLayoutInflater().inflate(R.layout.message_sent, parent, false);
        }
        TextView message_body = (TextView) view.findViewById(R.id.message_body);
        message_body.setText(message.getBody());
        return view;
    }
}
