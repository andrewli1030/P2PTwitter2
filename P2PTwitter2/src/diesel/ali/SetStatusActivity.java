package diesel.ali;

import java.util.Date;

import org.alljoyn.bus.sample.chat.ChatApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetStatusActivity extends Activity  {

	private Button privateButton;
	private Button publicButton;
	private EditText statusEditText;

	private StatusHistoryDataSource statusHistoryDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_status);

		statusHistoryDataSource = new StatusHistoryDataSource(this);
		statusHistoryDataSource.open();

		statusEditText = (EditText) findViewById(R.id.status);

		privateButton = (Button) findViewById(R.id.private_button);
		publicButton = (Button) findViewById(R.id.public_button);
		privateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						FriendsListActivity.class);
				intent.putExtra("Status", statusEditText.getEditableText()
						.toString());
				statusEditText.setText("");
				startActivity(intent);
				
				
			}
		});

		publicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(),
						PublicStatusesActivity.class);

				Status publicStatus = new Status(P2PTwitterActivity.SENDER,
												P2PTwitterActivity.PUBLIC,
												statusEditText.getEditableText().toString(),
												(new Date()).getTime());

				mChatApplication.newLocalUserMessage(publicStatus);
				statusEditText.setText("");
				startActivity(intent);

			}
		});
		
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();
	}
	
	private ChatApplication mChatApplication = null;
}
