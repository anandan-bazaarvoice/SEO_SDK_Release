package com.bazaarvoice.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.sdk.BVSdkException;

/**
 * Utiltiy class to read and get the message from resource bundle.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public final class BVSdkMessageUtil {

	private static ResourceBundle _rsrcBundle;
	
	static {
		Locale locale = Locale.getDefault();
		_rsrcBundle = ResourceBundle.getBundle("message", locale);
	}
	
	private BVSdkMessageUtil() {
		
	}
	
	/**
	 * Gets the message from resource bundle.
	 * Pass the message code including error code and you should get a message
	 * that is in the resource bundle message_en_US.properties file.
	 * Gives back the same message code if it is not configured in resource bundle.
	 * 
	 * throws {@link MissingResourceException}
	 * @param code
	 * @return message from resource bundle.
	 */
	public static String getMessage(String code) {
		if (StringUtils.isBlank(code)) {
			throw new BVSdkException("ERR0001");
		}
		
		String message = null;
		try {
			message = _rsrcBundle.getString(code);
		} catch (RuntimeException ex) {
			message = code;
		}
		
		return message;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singleton instance.");
	}
}
