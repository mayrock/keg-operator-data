package edu.thu.keg.mdap.restful.jaxbcr;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import edu.thu.keg.mdap.restful.jerseyclasses.JDataset;
import edu.thu.keg.mdap.restful.jerseyclasses.JDatasetName;
import edu.thu.keg.mdap.restful.jerseyclasses.JFieldName;
import edu.thu.keg.mdap.restful.jerseyclasses.JGeograph;
import edu.thu.keg.mdap.restful.jerseyclasses.JStatistic;

@Provider
public class MyJAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private Class<?>[] types = { Object.class, JDatasetName.class,
			JDataset.class, JGeograph.class, JStatistic.class, JFieldName.class };

	public MyJAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(),
				types);
		System.out.println("��Ҫ����Ů��,Ҫ����Ů������!");
	}

	@Override
	public JAXBContext getContext(Class<?> objectType) {

		System.out.println("�õ�С����");
		for (Class<?> c : types) {
			if (c.equals(objectType)) {
				return context;
			}
		}
		return null;
	}
}
