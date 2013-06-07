/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author myc
 *
 */
public class LocalizedMessage {
	private HashMap<Locale, String> messages;
	private Locale defaultLocale;
	
	public LocalizedMessage() {
		messages = new HashMap<Locale, String>(2);
		defaultLocale = Locale.CHINESE;
	}
	
	public LocalizedMessage(Locale defaultLocale) {
		messages = new HashMap<Locale, String>(2);
		this.defaultLocale = defaultLocale;
	}
	
	public String getMessage(Locale locale) {
		return messages.get(locale);
	}
	
	public String getMessage() {
		return  getMessage(getDefaultLocale() );
	}
	
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}
	
	public void setMessage(String message) {
		this.messages.put(getDefaultLocale(), message);
	}
	
	public void setMessage(Locale locale, String message) {
		this.messages.put(locale, message);
	}
}
