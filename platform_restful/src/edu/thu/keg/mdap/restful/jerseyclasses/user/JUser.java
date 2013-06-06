package edu.thu.keg.mdap.restful.jerseyclasses.user;

import javax.xml.bind.annotation.XmlRootElement;

import edu.thu.keg.mdap.management.user.User;

@XmlRootElement
public class JUser {
	boolean status;
	User user;

	public boolean getStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
