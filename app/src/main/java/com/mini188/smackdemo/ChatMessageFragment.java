package com.mini188.smackdemo;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mini188.smackdemo.adapter.MessageAdapter;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiexb on 2016/04/07.
 */
public class ChatMessageFragment extends Fragment {
    private EditText _msgEdit;
    private ImageButton _sendButton;
    protected ListView _messagesView;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.chat_message_fragment, container, false);
        view.setOnClickListener(null);

        _msgEdit = (EditText) view.findViewById(R.id.textinput);
        _sendButton = (ImageButton) view.findViewById(R.id.textSendButton);

        _sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _messagesView = (ListView) view.findViewById(R.id.messages_view);
        _messagesView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        _messagesView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        List<Message> msgList = new ArrayList<Message>();
        //_messagesView.setAdapter(new MessageAdapter(getActivity(), msgList));
        return view;
    }
}
