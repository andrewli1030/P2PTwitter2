package diesel.ali;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UsersDataSource extends DataSource {

	public UsersDataSource(Context context) {
		super(context);
	}

	public void insertUser(User user) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
		database.insert(DatabaseHelper.TABLE_USERS, null, values);
	}

	public void deleteUser(User user) {
		database.delete(DatabaseHelper.TABLE_USERS, DatabaseHelper.COL_USERNAME
				+ " =?", new String[] { user.getUsername() });
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
				new String[] { DatabaseHelper.COL_USERNAME }, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = new User(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_USERNAME)));
			users.add(user);
			cursor.moveToNext();
		}
		cursor.close();
		return users;
	}

	public String[] getAllUsernames() {
		String[] usernames = new String[getCount()];
		Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
				new String[] { DatabaseHelper.COL_USERNAME }, null, null, null,
				null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			User user = new User(cursor.getString(cursor
					.getColumnIndex(DatabaseHelper.COL_USERNAME)));
			usernames[cursor.getPosition()] = user.getUsername();
			cursor.moveToNext();

		}
		cursor.close();
		return usernames;
	}

	/*
	 * public Set<User> getAllUsernamesInSet() { Set<User >usernames = new
	 * HashSet<User>(); Cursor cursor =
	 * database.query(DatabaseHelper.TABLE_USERS, new String[] {
	 * DatabaseHelper.COL_USERNAME }, null, null, null, null, null);
	 * cursor.moveToFirst(); while (!cursor.isAfterLast()) { User user = new
	 * User(cursor.getString(cursor
	 * .getColumnIndex(DatabaseHelper.COL_USERNAME))); usernames.add(user);
	 * cursor.moveToNext();
	 * 
	 * } cursor.close(); return usernames; }
	 */

	public boolean isFriend(User user) {
		boolean ret;
		Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, null,
				DatabaseHelper.COL_USERNAME + " =?",
				new String[] { user.getUsername() }, null, null, null);
		ret = cursor.getCount() > 0;
		cursor.close();
		return ret;
	}

	public int getCount() {
		Cursor cursor = database.query(DatabaseHelper.TABLE_USERS,
				new String[] { "COUNT(" + DatabaseHelper.COL_USERNAME + ")" },
				null, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getInt(0);

	}
}
