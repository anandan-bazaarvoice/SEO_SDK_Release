package com.bazaarvoice.util;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.util.BVSdkMessageUtil;

/**
 * Test case for MessageUtil class.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkMessageUtilTest {

	/**
	 * Tests to check if the message classes are able to get the messages
	 * 
	 */
	@Test
	public void testMessages() {
		/*
		 * Null message code
		 */
		String messageCode = null;
		String message = null;
		String errorMessage = null;
		try {
			message = BVSdkMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNull(message, "message should be null here");
		assertNotNull(errorMessage, "There should be an error message in errorMessage");
		
		/*
		 * Empty message code
		 */
		messageCode = "";
		errorMessage = null;
		try {
			message = BVSdkMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNull(message, "message should be null here");
		assertNotNull(errorMessage, "There should be an error message in errorMessage");
		
		/*
		 * Invalid message code
		 */
		messageCode = "INVALID_CODE";
		errorMessage = null;
		try {
			message = BVSdkMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
			fail("There was an error please check the exception which should have not occured.");
		}
		assertSame(message, messageCode, "message should be same as messageCode");
		assertNull(errorMessage, "There should not be an error message in errorMessage");
		
		/*
		 * Valid message code
		 */
		messageCode = "MSG0000";
		errorMessage = null;
		try {
			message = BVSdkMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNotNull(message, "message should not be null.");
		assertNull(errorMessage, "There should not be an error message in errorMessage");
	}
	
	
	/**
	 * Test case ensures that the MessageUtil class is a singleton class.
	 */
	@Test
	public void testSingletonInstance() {
		Object theInstance = null;
		try {
			theInstance = Class.forName(BVSdkMessageUtil.class.getName()).newInstance();
			fail("Singleton class creation failed. ");
		} catch (ClassNotFoundException e) {
			fail("Threw ClassNotFoundException which is incorrect.");
		} catch (InstantiationException e) {
			fail("Threw InstantiationException which is incorrect.");
		} catch (IllegalAccessException e) {
			//There will be an exception here.
		}
		assertNull(theInstance, "theInstance object should be null since it should singleton");
	}
}
