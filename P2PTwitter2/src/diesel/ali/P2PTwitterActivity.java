package diesel.ali;

import java.util.Iterator;
import java.util.List;

import org.alljoyn.bus.sample.chat.ChatApplication;
import org.alljoyn.bus.sample.chat.Observable;
import org.alljoyn.bus.sample.chat.Observer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class P2PTwitterActivity extends TabActivity implements Observer {
	public static final User PUBLIC = new User("Public");
	public static final User HISTORY = new User("History");
	public static User SENDER = null;
	/** Called when the activity is first created. */

	private ChatApplication mChatApplication = null;

	private Button mJoinButton;
	private EditText username;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, PublicActivity.class);
		spec = tabHost.newTabSpec("public").setIndicator("Public")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FriendsActivity.class);
		spec = tabHost.newTabSpec("friends").setIndicator("Friends")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SetStatusActivity.class);
		spec = tabHost.newTabSpec("setstatus").setIndicator("Set Status")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTabByTag("public");

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 70;
		}
        
        mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();
		mChatApplication.addObserver(this);
        
		username = (EditText) findViewById(R.id.username);
		username.setText("Set Username then click Join");
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				username.setText("");
				username.setOnClickListener(null);				
			}
		});
		
		username.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
					 SENDER = new User(v.getText().toString());
				 }
				return true;
			}
		});
		
		mJoinButton = (Button) findViewById(R.id.useJoin);
		mJoinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<String> ch = mChatApplication.getFoundChannels();
				boolean found = false;
				Log.i("TAGP", "Looking for public channel");
				for (Iterator<String> i = ch.iterator(); i.hasNext();) {
					String channelName = i.next();
					Log.i("TAGP", "Found "+channelName+" and t/f: "+"org.alljoyn.bus.samples.chat.public".equals(channelName));
					if (channelName
							.equals("org.alljoyn.bus.samples.chat.public")) {
						Log.i("TAGP", "Found public channel");
						mChatApplication.useSetChannelName("public");
						mChatApplication.useJoinChannel();
						found = true;
					}
				}

				if (!found) {
					Log.i("TAGP", "Creating public channel");
					mChatApplication.hostSetChannelName("public");
					mChatApplication.hostInitChannel();
					mChatApplication.hostStartChannel();
					mChatApplication.useSetChannelName("public");
					mChatApplication.useJoinChannel();
				}
				SENDER = new User(username.getEditableText().toString());
				PublicStatusesActivity.synced = false;
			}
		});
		
        
    }

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}
}