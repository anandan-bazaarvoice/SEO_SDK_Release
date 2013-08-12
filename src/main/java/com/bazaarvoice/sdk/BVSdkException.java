package com.bazaarvoice.sdk;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.util.BVSdkMessageUtil;

/**
 * Bazaarvoice sdk exception class.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	public BVSdkException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public BVSdkException(String errorCode, IOException e) {
		super(e);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		if (StringUtils.isNotBlank(errorCode)) {
			return BVSdkMessageUtil.getMessage(errorCode);
		}
		
		return super.getMessage();
	}
	
}
