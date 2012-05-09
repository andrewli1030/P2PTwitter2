package diesel.ali;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.alljoyn.bus.sample.chat.ChatApplication;
import org.alljoyn.bus.sample.chat.Observable;
import org.alljoyn.bus.sample.chat.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendsOnlineActivity extends ListActivity implements Observer {

	public static List<User> FRIENDS = new ArrayList<User>();
	private ChatApplication mChatApplication = null;
	//private StatusHistoryDataSource statusHistoryDataSource;
	//private UsersDataSource usersDataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	/*	statusHistoryDataSource = new StatusHistoryDataSource(this);
		statusHistoryDataSource.open();*/
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();
		mChatApplication.addObserver(this);

		sendOnlineStatus();
		
		setListAdapter(new ArrayAdapter<User>(this, R.layout.status_item,
				this.getUsers()));
		
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				User recipient = (User) getListView().getItemAtPosition(position);
				Intent intent = new Intent(view.getContext(),
						StatusHistoryActivity.class);
				intent.putExtra("Recipient", recipient.getUsername());
				startActivity(intent);
			}
		});
	}

	private void sendOnlineStatus() {
		Status onlineStatus = new Status(P2PTwitterActivity.SENDER, P2PTwitterActivity.ONLINE, "", new Long(0));
		mChatApplication.newLocalUserMessage(onlineStatus);
	}

	@Override
	public void update(Observable o, Object arg) {
		String qualifier = (String) arg;

		if (qualifier.equals(ChatApplication.APPLICATION_QUIT_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_APPLICATION_QUIT_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.HISTORY_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_HISTORY_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.USE_CHANNEL_STATE_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_CHANNEL_STATE_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.ALLJOYN_ERROR_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_ALLJOYN_ERROR_EVENT);
			mHandler.sendMessage(message);
		}
		if (qualifier.equals(ChatApplication.ONLINE_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_ONLINE_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

	}

	private static final int HANDLE_APPLICATION_QUIT_EVENT = 0;
	private static final int HANDLE_HISTORY_CHANGED_EVENT = 1;
	private static final int HANDLE_CHANNEL_STATE_CHANGED_EVENT = 2;
	private static final int HANDLE_ALLJOYN_ERROR_EVENT = 3;
	private static final int HANDLE_ONLINE_CHANGED_EVENT = 4;
	private static final String TAG = "chat.UseActivity";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_APPLICATION_QUIT_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_APPLICATION_QUIT_EVENT");
				finish();
			}
				break;
			case HANDLE_HISTORY_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_HISTORY_CHANGED_EVENT");
				// updateHistory();
				break;
			}
			case HANDLE_CHANNEL_STATE_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_CHANNEL_STATE_CHANGED_EVENT");
				// updateChannelState();
				break;
			}
			case HANDLE_ALLJOYN_ERROR_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_ALLJOYN_ERROR_EVENT");
				// alljoynError();
				break;
			}
			case HANDLE_ONLINE_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_ALLJOYN_ERROR_EVENT");
				updateOnline();
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		sendOnlineStatus();
		super.onResume();
	}
	
	
	public List<User> getUsers() {
		return FriendsOnlineActivity.FRIENDS;
	}

	private void updateOnline() {
		setListAdapter(new ArrayAdapter<User>(this, R.layout.status_item,
				this.getUsers()));
		((ArrayAdapter<User>) getListAdapter()).notifyDataSetChanged();
	}

}
