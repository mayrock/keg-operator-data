package jaxbcr;

import javax.ws.rs.ext.*;
import javax.xml.bind.JAXBContext;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import edu.thu.keg.mdap.restful.jerseyclasses.JDatasetName;

@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {
	private JAXBContext context;
	private Class<?>[] types = { JDatasetName.class};

	public MyJAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
				types);
	}
	public JAXBContext getContext(Class<?> objectType) {
		for (Class<?> c : types) {
			if (c.equals(objectType)) {
				return context;
			}
		}
		return null;
	}
}