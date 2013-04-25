package edu.thu.keg.mdap.init;

import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap_impl.PlatformImpl;



/**
 * 启动服务器时候的初始化server
 * @author Yuan Bozhi
 *
 */
@WebListener("Socket Init")
public class ServerInitial implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("destroy");
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0 ) {
		ServletContext sc=arg0.getServletContext();
		String P_Config = ResourceBundle.getBundle("platform_initial")
				.getString("PlatformImpl_CONFIG");
		Platform p = new PlatformImpl(arg0.getServletContext().getRealPath("/WEB-INF/"+P_Config));
		sc.setAttribute("platform", p);
		System.out.println("启动服务器");
		
	}
}
