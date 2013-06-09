package edu.thu.keg.mdap.restful.jaxbcr;

import java.util.ArrayList;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import edu.thu.keg.mdap.management.favorite.Favorite;
import edu.thu.keg.mdap.restful.dataset.DsGetFunctions;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JColumn;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JGeograph;
import edu.thu.keg.mdap.restful.jerseyclasses.dataset.JStatistic;
import edu.thu.keg.mdap.restful.jerseyclasses.management.JFavorite;
import edu.thu.keg.mdap.restful.jerseyclasses.user.JUser;

@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private Class<?>[] types = { JSONObject.class, Integer.class, String.class,
			Object.class, Boolean.class, ArrayList.class, JDatasetName.class,
			JDataset.class, JGeograph.class, JStatistic.class,
			JFieldName.class, JColumn.class, Favorite.class, JUser.class };
	private static Logger log = Logger.getLogger(MyJAXBContextResolver.class);

	public MyJAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
				types);
		log.info("不要叫我大王,要叫我女王大人!");
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {

		for (Class<?> c : types) {
			if (c.equals(objectType)) {
				log.info("用到小弟吳超了");
				return context;
			}
		}
		return null;
	}
}
