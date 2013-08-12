package com.bazaarvoice.content.tester;

import org.testng.annotations.Test;

import com.bazaarvoice.sdk.config.BVClientConfig;
import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.sdk.model.BVParameters;
import com.bazaarvoice.sdk.model.ContentType;
import com.bazaarvoice.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.BVManagedUIContent;
import com.bazaarvoice.seo.sdk.BVUIContent;

/**
 * This test case tests the basic bazaarvoice managed UI content.
 * The test case focus is to ensure that the contents are loaded from
 * file and from http mechanism based on various configuration.
 * Note that this class has test cases for various configurations.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent1_Test {

//	@Test
	public void testSEOContentFromFile() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "True");
		bvConfig.addProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT, "/seo_local_files");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html");
		bvParameters.setBaseURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/");
		bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/reviews/product/2/data-gen-696yl2lg1kurmqxn88fqif5y2.htm");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
//		bvParameters.setItemId("01586"); //works when loading from locals files.
		bvParameters.setSubjectId("data-gen-696yl2lg1kurmqxn88fqif5y2");
		
		String theUIContent = uiContent.searchContent(bvParameters);
		
		System.out.println(theUIContent);
	}
	
	/**
	 * TODO: We need to have a valid HTTP product to start sending.
	 */
//	@Test
	public void testSEOContentFromHTTP_withOutIntegrationScript() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "");
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "False");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html");
		bvParameters.setBaseURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/");
		bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/reviews/product/2/data-gen-696yl2lg1kurmqxn88fqif5y2.htm");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
//		bvParameters.setItemId("01586"); //works when loading from locals files.
		bvParameters.setSubjectId("data-gen-696yl2lg1kurmqxn88fqif5y2");
		
		String theUIContent = uiContent.searchContent(bvParameters);
		
		System.out.println(theUIContent);
	}
	
}
