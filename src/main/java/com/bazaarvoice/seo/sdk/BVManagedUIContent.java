package com.bazaarvoice.seo.sdk;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.config.BVClientConfig;
import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.config.BVCoreConfig;
import com.bazaarvoice.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.sdk.model.BVParameters;
import com.bazaarvoice.sdk.model.ContentType;
import com.bazaarvoice.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.validation.BVParameterValidator;
import com.bazaarvoice.seo.sdk.validation.BVValidator;
import com.bazaarvoice.util.BVSdkMessageUtil;
import com.bazaarvoice.util.BVSdkUtilty;

/**
 * Implementation class for BVUIContent.
 * This class is the default implementation class to get Bazaarvoice content.
 * Based on the configurations that are set, the actual contents will be retrieved.
 * 
 * Following are the test classes/cases that this class bound to:
 * make sure test cases are not affected by the changes.
 * Refer to individual test case for an explanation.
 * Most of the test case are use case based and implementation oriented.
 * 
 * 1. BVManagedUIContentTest.java
 * 2. BVManagedUIContent1_Test.java
 * 3. 
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent implements BVUIContent {

	private final static Logger _logger = LoggerFactory.getLogger(BVManagedUIContent.class);
	private static final String PATH_SEPARATOR = "/";
	private static final String BVREVEAL = "bvreveal";
	private static final String DBG_BVREVEAL = "bvreveal=debug";
	private static final String JS_DISPLAY_MSG = "JavaScript-only Display";
	private static final String INCLUDE_PAGE_URI = "{INSERT_PAGE_URI}";
	
	private BVConfiguration _bvConfiguration;
	private BVValidator bvParamValidator = new BVParameterValidator();
	
	/**
	 * Default constructor.
	 * Loads all default configuration within.
	 * 
	 */
	public BVManagedUIContent() {
		this._bvConfiguration = new BVSdkConfiguration();
	}
	
	/**
	 * Constructor with BVConfiguration argument.
	 * 
	 * @param bvConfiguration	The configuration/settings that has to be supplied for BVcontent to work.
	 */
	public BVManagedUIContent(BVConfiguration bvConfiguration) {
		if (bvConfiguration == null) {
			throw new BVSdkException("ERR0007");
		}
		
		this._bvConfiguration = bvConfiguration;
	}
	
	@Override
	public String searchContent(BVParameters bvParameters) {
		bvParamValidator.validate(bvParameters);
		
		long startTime = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		String bvRootFolder = _bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		String version = _bvConfiguration.getProperty(BVCoreConfig.VERSION.getPropertyName());
		boolean isBotDetection = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.BOT_DETECTION.getPropertyName()));
		String queryString = BVSdkUtilty.getQueryString(bvParameters.getPageURI());
		int page = BVSdkUtilty.getPageNumber(queryString);
		boolean isSdkEnabled = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.SEO_SDK_ENABLED.getPropertyName()));
		
		if (!isSdkEnabled && !queryString.contains(BVREVEAL)) {
			_logger.info(BVSdkMessageUtil.getMessage("MSG0003"));
			return "";
		}
		
		try {
			//includes integration script if one is enabled.
			includeIntegrationCode(sb, bvParameters.getContentType(), bvParameters.getSubjectType(), bvParameters.getSubjectId());
			
			/*
			 * Hit only when botDetection is disabled or if the queryString is appended with bvreveal or if it matches any 
			 * crawler pattern that is configured at the client configuration. 
			 */
			if (!isBotDetection || queryString.contains(BVREVEAL) || showUserAgentSEOContent(bvParameters.getUserAgent())) {
				getBvContent(sb, bvRootFolder, bvParameters, page);
				if (queryString.contains(DBG_BVREVEAL)) {
					boolean isStaging = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.STAGING.getPropertyName()));
					addDebugMsg(sb, bvParameters, queryString, isBotDetection, isStaging);
				}
			} else {
				sb.append(composeLog(bvRootFolder, version, JS_DISPLAY_MSG));
			}
        } catch (BVSdkException e) {
            return composeLog(bvRootFolder, version, "Error: " + e.getMessage());
        }
		
		long endTime = System.currentTimeMillis();
		
		//Used to output the total time taken to get the contents, also displays client and api information
		sb.append(composeLog(bvRootFolder, version, "timer " + Long.toString(endTime - startTime) + "ms"));
		
		return sb.toString();
	}

	private void addDebugMsg(StringBuilder sb, BVParameters bvParameters, String queryString, boolean isBotDetection, boolean isStaging) {
		sb.append("\n    userAgent: ").append(bvParameters.getUserAgent())
    	.append("\n    baseURL: ").append(bvParameters.getBaseURI())
        .append("\n    queryString: ").append(queryString)
        .append("\n    contentType: ").append(bvParameters.getContentType())
        .append("\n    subjectType: ").append(bvParameters.getSubjectType())
        .append("\n    subjectId: ").append(bvParameters.getSubjectId())
        .append("\n    staging: ").append(Boolean.toString(isStaging))
        .append("\n    pattern: ")
        .append(_bvConfiguration.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName()))
        .append("\n    detectionEnabled: ").append(isBotDetection).append("\n");
	}

	private void getBvContent(StringBuilder sb, String deploymentZoneId, BVParameters bvParameters, int page) {
		String path = deploymentZoneId + PATH_SEPARATOR + bvParameters.getContentType().uriValue() + 
				PATH_SEPARATOR + bvParameters.getSubjectType().uriValue() + PATH_SEPARATOR + page + PATH_SEPARATOR + bvParameters.getSubjectId() + ".htm";
		
        if (isContentFromFile()) {
        	sb.append(loadContentFromFile(path));
        } else {
        	sb.append(loadContentFromHttp(path));
        }
       
        BVSdkUtilty.replaceString(sb, INCLUDE_PAGE_URI, bvParameters.getBaseURI() + (bvParameters.getBaseURI().contains("?") ? "&" : "?"));
	}

	private boolean showUserAgentSEOContent(String userAgent) {
		if (userAgent == null) {
			return false;
		}
		
        String crawlerAgentPattern = _bvConfiguration.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName());
        Pattern pattern = Pattern.compile(crawlerAgentPattern, Pattern.CASE_INSENSITIVE);

        return (pattern.matcher(userAgent).matches() || userAgent.toLowerCase().contains("google"));
    }
	
	private void includeIntegrationCode(StringBuilder sb, ContentType contentType, SubjectType subjectType, String subjectId) {
		String includeScriptStr = _bvConfiguration.getProperty(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName());
		boolean includeIntegrationScript = Boolean.parseBoolean(includeScriptStr);
		
		if (!includeIntegrationScript) {
			return;
		}
		
        Object[] params = {subjectType.uriValue(), subjectId};
        String integrationScriptValue = _bvConfiguration.getProperty(contentType.getIntegrationScriptProperty());
        String integrationScript = MessageFormat.format(integrationScriptValue, params); 
        
        sb.append(integrationScript);
    }

	private String loadContentFromHttp(String path) {
		boolean isStaging = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.STAGING.getPropertyName()));
		String s3Hostname = isStaging ? _bvConfiguration.getProperty(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName()) : 
			_bvConfiguration.getProperty(BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName());
		
        String cloudKey = _bvConfiguration.getProperty(BVClientConfig.CLOUD_KEY.getPropertyName());
        String urlPath = "/" + cloudKey + "/" + path;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(s3Hostname).setPath(urlPath);
        
        int connectionTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.CONNECT_TIMEOUT.getPropertyName()));
        int socketTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.SOCKET_TIMEOUT.getPropertyName()));
		String content = null;
        
		try {
			URI targetUrl = builder.build();
			content = Request.Get(targetUrl).connectTimeout(connectionTimeout).
					socketTimeout(socketTimeout).execute().returnContent().asString();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return content;
	}

	private String loadContentFromFile(String path) {
		String fileRoot = _bvConfiguration.getProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName());
		if (StringUtils.isBlank(fileRoot)) {
			throw new BVSdkException("ERR0010");
		}
		
		String fullFilePath = fileRoot + "/" + path;
		String content = null;
		try {
			content = BVSdkUtilty.readFile(fullFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content;
	}

	private String composeLog(String deploymentZoneId, String version, String message) {
		return "\n<!-- BVSEO|dz:" + deploymentZoneId + "|sdk: v" + version + "-j|" + message + " -->\n";
	}
	
	@Override
	public String searchContent(BVConfiguration bvConfig, BVParameters bvParameters) {
		if (bvConfig == null) {
			throw new BVSdkException("ERR0007");
		}
		
		return searchContent(bvParameters);
	}
	
	private boolean isContentFromFile() {
		boolean loadFromFile = Boolean.parseBoolean(_bvConfiguration.
				getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()));
		return loadFromFile;
	}
}
