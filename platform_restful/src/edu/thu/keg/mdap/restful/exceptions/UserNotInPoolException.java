package edu.thu.keg.mdap.restful.exceptions;

public class UserNotInPoolException extends Exception {
	private static final long serialVersionUID = 2518079930070277143L;

	public UserNotInPoolException(String msg) {
		super(msg);
	}

	public UserNotInPoolException() {
		super();
	}
}
