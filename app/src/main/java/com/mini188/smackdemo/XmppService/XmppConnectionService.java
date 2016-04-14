package com.mini188.smackdemo.XmppService;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StreamOpen;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Xmpp连接服务，用于提供与xmpp服务器的连接、通信等基础功能
 * 单例对象
 */
public class XmppConnectionService {
    private static  XmppConnectionService _xmppService;
    private AbstractXMPPConnection _xmppConnection;
    private String _userName;
    private final Hashtable<String, Chat> _chatList = new Hashtable<String, Chat>();

    private  XmppConnectionService(){
    }

    public static XmppConnectionService getInstance(){
        synchronized (XmppConnectionService.class) {
            if (_xmppService == null){
                _xmppService = new XmppConnectionService();
            }
            return _xmppService;
        }
    }

    public void Connect (String userName, String pwd){
        if (_xmppConnection != null && _xmppConnection.isConnected()) {
            return;
        }

        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setHost("192.168.45.41");
        configBuilder.setPort(5222);
        configBuilder.setUsernameAndPassword(userName, pwd);
        configBuilder.setServiceName("zrtc");
        configBuilder.setCustomSSLContext(getSslContent());
        //configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        _xmppConnection = new XMPPTCPConnection(configBuilder.build());
        new Thread() {
            @Override
            public void run() {
                try {
                    _xmppConnection.connect();
                    _xmppConnection.login();

                    Presence presence = new Presence(Presence.Type.available);
                    _xmppConnection.sendStanza(presence);
                } catch (IOException e) {
                    Log.e("XMPP_CONNLOGIN", e.getMessage());
                } catch (XMPPException e) {
                    Log.e("XMPP_CONNLOGIN", e.getMessage());
                } catch (SmackException e) {
                    Log.e("XMPP_CONNLOGIN", e.getMessage());
                }
            }
        }.start();

        _userName = userName;
    }

    private SSLContext getSslContent(){
        // Create a trust manager that does not validate certificate chains
        // Android use X509 cert
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) throws CertificateException {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }

    public  AbstractXMPPConnection getXmppConnection() {
        return  _xmppConnection;
    }

    public ChatManager getChatManager() {
        return  ChatManager.getInstanceFor(_xmppConnection);
    }

    public  void addChat(String jid, Chat chat){
        synchronized (XmppConnectionService.class){
            _chatList.put(jid, chat);
        }
    }

    public  void removeChat(String jid){
        synchronized (XmppConnectionService.class){
            _chatList.remove(jid);
        }
    }

    public  Chat getChat(String jid){
        synchronized (XmppConnectionService.class){
            if (_chatList.containsKey(jid)){
                return _chatList.get(jid);
            } else {
                return null;
            }
        }
    }

    public String getUserName() {
        return _userName;
    }
}