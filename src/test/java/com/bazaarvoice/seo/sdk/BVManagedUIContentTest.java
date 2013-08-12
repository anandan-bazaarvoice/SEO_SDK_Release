package com.bazaarvoice.seo.sdk;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.config.BVClientConfig;
import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.sdk.model.BVParameters;
import com.bazaarvoice.util.BVSdkMessageUtil;

/**
 * Test class for BVManagedUIContent implementation class.
 * This test case focuses more from an implementation point of view.
 * There may be few test cases which can be a use case scenario.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContentTest {
	
	/**
	 * Test case for passing a valid constructor argument
	 */
	@Test
	public void testValidConstructorArgument() {
		String errorMessage = null;
		BVConfiguration bvConfig = null;
		BVUIContent bvUIContent = null;
		
		/*
		 * When null is passed to the argument constructor
		 */
		try {
			bvUIContent = new BVManagedUIContent(bvConfig);
			fail("There should have been an exception when null was passed");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0007"), "Error messages do not match.");
		
		/*
		 * Positive scenario when valid configuration is passed
		 */
		try {
			bvConfig = new BVSdkConfiguration();
			bvUIContent = new BVManagedUIContent(bvConfig);
		} catch (BVSdkException e) {
			fail("There should have been an exception when null was passed");
		}
		
		/*
		 * When null is passed to the bvUIContent.
		 */
		errorMessage = null;
		try {
			bvUIContent = new BVManagedUIContent();
			bvUIContent.searchContent(null, new BVParameters());
			fail("Unexpected behaviour should have thrown an exception here.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0007"), "Error messages do not match.");
	}

	/**
	 * Test case to check if null query param is supplied
	 * to BVManagedUIContent.
	 */
	@Test
	public void testSearchContentNullBVQueryParams() {
		BVUIContent bvUIContent = new BVManagedUIContent();
		String bvContent = null;
		String errorMessage = null;
		
		try {
			bvContent = bvUIContent.searchContent(null);
			fail("This block should thow an exception.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0011"), "Message are not same please verify.");
		assertNull(bvContent, "BVContent should be null.");
	}
	
	/**
	 * Test case implementation to check if integration script are including based on the configuration.
	 * This can be considered as one of the use case scenario.
	 * In the test case most of the parameters in bvConfiguration will be disabled and only
	 * configuration for integration script will be enabled. Search contents are retrieved
	 * from localFiles by setting the LOAD_SEO_FILES_LOCALLY to true and
	 * LOCAL_SEO_FILE_ROOT to point to a location in test resource where seo contents reside. 
	 * 
	 * Test covers: Contents like
	 * Reviews
	 * Question
	 * 
	 * For Subjects
	 * Product
	 * Category
	 * 
	 */
	@Test
	public void testIncludeIntegrationScript() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.BOT_DETECTION, "True");
		bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, "12325");
		bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "afgedbd");
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "True");
		bvConfiguration.addProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT, "/seo_local_files");
		bvConfiguration.addProperty(BVClientConfig.STAGING, "True");
		
		bvConfiguration.addProperty(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE, "True");
		
	}
}
