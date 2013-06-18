package edu.thu.keg.mdap.init;

import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import edu.thu.keg.mdap.Platform;
import edu.thu.keg.mdap.management.ManagementPlatform;
import edu.thu.keg.mdap.restful.jaxbcr.MyJAXBContextResolver;
import edu.thu.keg.mdap_impl.PlatformImpl;

/**
 * 启动服务器时候的初始化server
 * 
 * @author Yuan Bozhi
 * 
 */
@WebListener("Socket Init")
public class ServerInitial implements ServletContextListener {
	private static Logger log = Logger.getLogger(ServerInitial.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("Server destroying!");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext sc = arg0.getServletContext();
		String P_Config = ResourceBundle.getBundle("platform_initial")
				.getString("PlatformImpl_CONFIG");
		PlatformImpl p = new PlatformImpl(arg0.getServletContext().getRealPath(
				"/WEB-INF/" + P_Config));
		p.crud();
		sc.setAttribute("platform", p);
		ManagementPlatform mp = new ManagementPlatform();
		sc.setAttribute("managementplatform", mp);
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			// Class.forName("");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("启动MDAP服务器");
	}
}
