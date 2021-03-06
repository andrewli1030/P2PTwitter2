package diesel.ali;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Status {

	private User recipient;
	private User sender;
	private String statusText;
	private Long time;

	/*
	 * public Status(User sender, String statusText) { this.setSender(sender);
	 * this.setStatusText(statusText); }
	 */

	public Status(User sender, User recipient, String statusText, Long time) {
		this.setSender(sender);
		this.setRecipient(recipient);
		this.setStatusText(statusText);
		this.setTime(time);
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		return "["+dateFormat.format(new Date(this.time))+"] ("+this.sender.toString() + "): " + this.statusText;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String convertStatusToString(String divider) {
		String stringStatus = this.sender + divider + this.recipient + divider
				+ this.statusText + divider + this.time;
		return stringStatus;

	}
}
