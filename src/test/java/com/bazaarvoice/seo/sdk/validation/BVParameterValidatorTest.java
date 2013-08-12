package com.bazaarvoice.seo.sdk.validation;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.model.BVParameters;
import com.bazaarvoice.sdk.model.ContentType;
import com.bazaarvoice.sdk.model.SubjectType;
import com.bazaarvoice.util.BVSdkMessageUtil;

/**
 * Test case implementation for BVParameterValidator.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVParameterValidatorTest {
	
	/**
	 * test case for validate method in BVParameterValidator.
	 * This test case starts from failure scenario to till success scenario.
	 */
	@Test
	public void testValidation() {
		String errorMessage = null;
		BVParameters bvParams = null;
		
		BVValidator bvValidator = new BVParameterValidator();
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0011"), "Error Messages are different.");
		
		errorMessage = null;
		bvParams = new BVParameters();
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0017"), "Error Messages are different.");
		
		bvParams.setUserAgent("google");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0014"), "Error Messages are different.");
		
		bvParams.setSubjectId("ProductA");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0016"), "Error Messages are different.");
		
		bvParams.setSubjectType(SubjectType.PRODUCT);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0015"), "Error Messages are different.");
		
		bvParams.setContentType(ContentType.REVIEWS);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");
		
		bvParams.setBaseURI("acdd");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");
		
		bvParams.setBaseURI(null);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");
		
		bvParams.setBaseURI("htt://example.com/1234");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");
		
		bvParams.setBaseURI("http://example.com/1234");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0013"), "Error Messages are different.");
		
		bvParams.setPageURI("acdd");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0013"), "Error Messages are different.");
		
		bvParams.setPageURI(null);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0013"), "Error Messages are different.");
		
		bvParams.setPageURI("htt://example.com/1234?id=123&name=xyz&product=abcd");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0013"), "Error Messages are different.");
		
		bvParams.setPageURI("https://example.com/1234?id=123&name=xyz&product=abcd");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
			fail("It should not throw any exception at this point.");
		}
		assertNull(errorMessage, "There should not be any error in the errorMessage at this point");
	}
	
}
