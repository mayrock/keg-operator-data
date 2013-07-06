package edu.thu.keg.mdap.restful.exceptions;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class UserNotInPoolException extends Exception {
	private static final long serialVersionUID = 2518079930070277143L;
	public static String BAIDU_URL = "http://www.baidu.com/";

	public UserNotInPoolException(HttpServletResponse http, String url,
			String msg) {
		super(msg);
		try {
			http.sendRedirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public UserNotInPoolException(HttpServletResponse http, String msg) {
		super(msg);
		try {
			http.sendRedirect(BAIDU_URL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public UserNotInPoolException(String msg) {
		super(msg);

	}

	public UserNotInPoolException() {
		super();
	}
}
