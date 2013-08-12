package com.bazaarvoice.seo.sdk.validation;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.model.BVParameters;

/**
 * Validator implementation class for BVParameters.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVParameterValidator implements BVValidator {

	/**
	 * Validator method to validate all the attributes in bvParameter object
	 * 
	 * @throws BVSdkException	if there are any attributes which are either null or invalid.
	 */
	@Override
	public void validate(BVParameters bvParams) throws BVSdkException {
		if (bvParams == null) {
			throw new BVSdkException("ERR0011");
		}
		
		if (StringUtils.isBlank(bvParams.getUserAgent())) {
			throw new BVSdkException("ERR0017");
		}
		
		if (StringUtils.isBlank(bvParams.getSubjectId())) {
			throw new BVSdkException("ERR0014");
		}
		
		if (bvParams.getSubjectType() == null) {
			throw new BVSdkException("ERR0016");
		}
		
		if (bvParams.getContentType() == null) {
			throw new BVSdkException("ERR0015");
		}
		
		try {
			URL url = new URL(bvParams.getBaseURI());
		} catch (MalformedURLException e) {
			throw new BVSdkException("ERR0012", e);
		}
		
		try {
			URL url = new URL(bvParams.getPageURI());
		} catch (MalformedURLException e) {
			throw new BVSdkException("ERR0013", e);
		}
	}
	
}
