package com.bazaarvoice.util;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.config.BVClientConfig;
import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.sdk.config.BVCoreConfig;

/**
 * TODO: remove this class
 * Test case for the BVSdkUtility class.
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkUtilityTest {

	/**
	 * Test case for the validateConfiguration method.
	 */
//	@Test
	public void testValidateConfiguration() {
		BVConfiguration bvConfig = null;
		String errorMessage = null;
		
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0007"), 
				"Error messages are different please verify.");
		
		bvConfig = new BVSdkConfiguration();
		/*
		 * When one of the server property is returning null.
		 */
		errorMessage = null;
		when(bvConfig.getProperty(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName())).thenReturn(null);
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0008"), 
				"Error messages are different please verify.");
		
		/*
		 * When one of the server property is returning empty.
		 */
		bvConfig = Mockito.mock(BVConfiguration.class);
		errorMessage = null;
		when(bvConfig.getProperty(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName())).thenReturn("");
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0008"), 
				"Error messages are different please verify.");
		
		/*
		 * When one of the client property is null.
		 */
		bvConfig = Mockito.mock(BVConfiguration.class);
		errorMessage = null;
		when(bvConfig.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName())).thenReturn(null);
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0008"), 
				"Error messages are different please verify.");
		
		/*
		 * When one of the client property is empty.
		 */
		bvConfig = Mockito.mock(BVConfiguration.class);
		errorMessage = null;
		when(bvConfig.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName())).thenReturn("");
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0009"), 
				"Error messages are different please verify.");
		
		
		/*
		 * special case for LOCAL_SEO_FILE_ROOT client property is empty and LOAD_SEO_FILES_LOCALLY is false.
		 */
		bvConfig = Mockito.mock(BVConfiguration.class);
		errorMessage = null;
		when(bvConfig.getProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName())).thenReturn(null);
		when(bvConfig.getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName())).thenReturn("false");
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0009"), 
				"Error messages are different please verify.");
		
		/*
		 * special case for LOCAL_SEO_FILE_ROOT client property is empty and LOAD_SEO_FILES_LOCALLY is true.
		 */
		bvConfig = Mockito.mock(BVConfiguration.class);
		errorMessage = null;
		when(bvConfig.getProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName())).thenReturn(null);
		when(bvConfig.getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName())).thenReturn("false");
		try {
			BVSdkUtilty.validateConfiguration(bvConfig);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0009"), 
				"Error messages are different please verify.");
	}
	
	
}
