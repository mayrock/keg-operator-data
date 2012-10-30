package Map;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

public class GetMsg {
	private String result;
	

	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String execute()throws IOException{
		// TODO Auto-generated method stub

		ActionContext ac = ActionContext.getContext();
		 HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);  
		System.out.println("MSG="+this.result);
		HttpServletResponse response = ServletActionContext.getResponse();
		
		//String msg = (String)request.getParameter("result");
		
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("data", "0");
		response.getWriter().write("wuchao");
	
		return null;
	}
}
