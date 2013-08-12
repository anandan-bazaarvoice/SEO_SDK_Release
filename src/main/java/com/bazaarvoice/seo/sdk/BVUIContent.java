package com.bazaarvoice.seo.sdk;

import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.model.BVParameters;

/**
 * 
 * @author Anandan Narayanaswam
 *
 */
public interface BVUIContent {

	String searchContent(BVParameters bvQueryParams);
	
	String searchContent(BVConfiguration bvConfig, BVParameters bvQueryParams);
	
}
