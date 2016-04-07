package com.mini188.smackdemo;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mini188.smackdemo.XmppService.XmppConnectionService;
import com.mini188.smackdemo.adapter.ListItemAdapter;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.Roster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AbstractXMPPConnection _conn;
    private ChatManager _chatMgr;


    //联系人
    private List<RosterEntry> contacts = new ArrayList<>();

    private ViewPager _viewPager;
    private MyListFragment _contactsListFragment = new MyListFragment();
    private MyListFragment _conferencesListFragment = new MyListFragment();

    private void onTabChanged() {
        invalidateOptionsMenu();
    }

    private void initTabs(){
        //使用tab页
        _viewPager = (ViewPager) findViewById(R.id.conversation_view_pager);

        _viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (getActionBar() != null) {
                    getActionBar().setSelectedNavigationItem(position);
                }
            }
        });

        _viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return _contactsListFragment;
                } else {
                    return _conferencesListFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        Roster roster = Roster.getInstanceFor(_conn);
        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry r: entries){
            contacts.add(r);
        }

        _contactsListFragment.setListAdapter(new ListItemAdapter(this, contacts));
        _contactsListFragment.setOnListItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatActivity = new Intent(MainActivity.this, ChatActivity.class);
                chatActivity.setAction(Intent.ACTION_VIEW);
                RosterEntry r = (RosterEntry) parent.getItemAtPosition(position);

                chatActivity.putExtra(ChatActivity.JID, r.getUser());
                startActivity(chatActivity);
            }
        });
        _viewPager.setCurrentItem(0);

        TextView tab1_tv = (TextView) findViewById(R.id.tab1_tv);
        tab1_tv.setText(R.string.app_contracts);
        TextView tab2_tv = (TextView) findViewById(R.id.tab2_tv);
        tab2_tv.setText(R.string.app_conferences);
    }

    private ActionBar.TabListener _tabListener = new ActionBar.TabListener() {

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            return;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            _viewPager.setCurrentItem(tab.getPosition());
            onTabChanged();
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            return;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        _conn = XmppConnectionService.getInstance().getXmppConnection();
        _chatMgr  = XmppConnectionService.getInstance().getChatManager();

        _chatMgr.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                XmppConnectionService.getInstance().addChat(chat.getParticipant(), chat);
            }
        });
        initTabs();
    }


    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class MyListFragment extends ListFragment {
        private AdapterView.OnItemClickListener mOnItemClickListener;
        private int mResContextMenu;

        public void setContextMenu(final int res) {
            this.mResContextMenu = res;
        }

        @Override
        public void onListItemClick(final ListView l, final View v, final int position, final long id) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(l, v, position, id);
            }
        }

        public void setOnListItemClickListener(AdapterView.OnItemClickListener l) {
            this.mOnItemClickListener = l;
        }

        @Override
        public void onViewCreated(final View view, final Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            registerForContextMenu(getListView());
            getListView().setFastScrollEnabled(true);
        }

        @Override
        public void onCreateContextMenu(final ContextMenu menu, final View v,
                                        final ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
        }

        @Override
        public boolean onContextItemSelected(final MenuItem item) {
            return true;
        }
    }

}
