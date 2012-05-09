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
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class P2PTwitterActivity extends TabActivity implements Observer {
    public static final User PUBLIC = new User("Public");
	public static final User SENDER = new User("AndrewLi");
	/** Called when the activity is first created. */
	
	private ChatApplication mChatApplication = null;
    
	private Button mJoinButton;
	
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
			}
		});
		
        
    }
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}