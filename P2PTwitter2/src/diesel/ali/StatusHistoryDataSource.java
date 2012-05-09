package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StatusHistoryDataSource extends DataSource {

	public StatusHistoryDataSource(Context context) {
		super(context);
	}

	public boolean insertStatus(Status status) {
		return insertStatus(status.getSender(), status.getRecipient(),
				status.getStatusText(), status.getTime());
	}

	public boolean insertStatus(User sender, User recipient, String statusText,
			Long time) {
		if (statusText.length() > 0) {
			Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.COL_SENDER + ", " + DatabaseHelper.COL_RECIPIENT + ", " + DatabaseHelper.COL_STATUS + ", " + DatabaseHelper.COL_TIME + ", FROM " + DatabaseHelper.TABLE_STATUS_HISTORY +
					" WHERE " + DatabaseHelper.COL_SENDER + "=? AND " + DatabaseHelper.COL_RECIPIENT + "=? AND " + DatabaseHelper.COL_STATUS + "=? AND " + DatabaseHelper.COL_TIME + "=?",
					new String[] {sender.toString(), recipient.toString(), statusText, ""+time});
			
			if(cursor.moveToNext())
				return true;
			
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COL_SENDER, sender.getUsername());
			values.put(DatabaseHelper.COL_RECIPIENT, recipient.getUsername());
			values.put(DatabaseHelper.COL_STATUS, statusText);
			values.put(DatabaseHelper.COL_TIME, time);
			database.insert(DatabaseHelper.TABLE_STATUS_HISTORY, null, values);
			return true;
		}
		return false;
	}
	
	public void clearTables()
	{
		database.execSQL("DELETE FROM "+DatabaseHelper.TABLE_STATUS_HISTORY);
		database.execSQL("DELETE FROM "+DatabaseHelper.TABLE_USERS);
	}

	public List<Status> getStatusHistory(User user) {
		List<Status> statuses = new ArrayList<Status>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_STATUS_HISTORY,
				null, DatabaseHelper.COL_RECIPIENT + " =?" + " OR "
						+ DatabaseHelper.COL_SENDER + " =?", new String[] {
						user.getUsername(), user.getUsername() }, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Status status = createStatus(cursor);
			statuses.add(status);
			cursor.moveToNext();
		}
		cursor.close();
		return statuses;
	}

	public List<Status> getAllFriendStatuses(String[] friendsUserNames) {
		List<Status> statuses = new ArrayList<Status>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_STATUS_HISTORY,
				null, /*
					 * DatabaseHelper.COL_RECIPIENT +
					 * inOperator(friendsUserNames) + " OR " +
					 */
				DatabaseHelper.COL_SENDER + inOperator(friendsUserNames),
				friendsUserNames, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Status status = createStatus(cursor);
			statuses.add(status);
			cursor.moveToNext();
		}
		cursor.close();
		return statuses;
	}

	private String inOperator(String[] friendsUserNames) {
		StringBuffer in = new StringBuffer(" IN(");
		for (int i = 0; i < friendsUserNames.length - 1; i++) {
			in.append("?,");
		}
		in.append("?)");
		return in.toString();
	}

	private Status createStatus(Cursor cursor) {
		User sender = new User(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_SENDER)));
		User recipient = new User(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_RECIPIENT)));
		String statusText = cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.COL_STATUS));
		Long time = cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.COL_TIME));
		return new Status(sender, recipient, statusText, time);

	}

	public Long getLatestTime() {
		Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.COL_TIME
				+ " FROM " + DatabaseHelper.TABLE_STATUS_HISTORY + " ORDER BY "
				+ DatabaseHelper.COL_TIME + " DESC LIMIT 1", new String[] {});
		if (!cursor.moveToNext()) {
			return new Long(0);
		} else
			return cursor.getLong(0);
	}

	public ArrayList<Status> getHistoryPast(Long time) {
		Cursor cursor = database.query(DatabaseHelper.TABLE_STATUS_HISTORY,
				null, DatabaseHelper.COL_TIME + ">?",
				new String[] { "" + time }, null, null, DatabaseHelper.COL_TIME + " DESC");
		if (!cursor.moveToNext())
			return null;
		ArrayList<Status> statuses = new ArrayList<Status>();
		statuses.add(createStatus(cursor));
		while(cursor.moveToNext())
			statuses.add(createStatus(cursor));
		return statuses;
	}
}
