package com.mini188.smackdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mini188.smackdemo.XmppService.XmppConnectionService;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout _msgList;
    private ChatManager _chatMgr;
    private Chat _chat;
    public static final String JID = "JID";

    private void addMessage(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = new TextView(ChatActivity.this);
                tv.setText(msg);
                _msgList.addView(tv);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        _msgList = (LinearLayout) findViewById(R.id.messages);
        _chatMgr = XmppConnectionService.getInstance().getChatManager();

        final Button send = (Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText sendText = (EditText) findViewById(R.id.msg);
                String msg = sendText.getText().toString();
                if (!msg.equals("")) {
                    addMessage(sendText.getText().toString());
                    try {
                        _chat.sendMessage(msg);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                    sendText.setText("");
                }
            }
        });

        Intent intent = getIntent();
        createChat(intent);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        createChat(intent);
    }

    private void createChat(Intent intent) {
        _chat = _chatMgr.createChat(intent.getStringExtra(JID));
        _chat.addMessageListener(new ChatMessageListener() {
            @Override
            public void processMessage(Chat chat, Message message) {
                addMessage(message.getBody());
            }
        });
    }
}
