package com.bazaarvoice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.bazaarvoice.sdk.BVSdkException;
import com.bazaarvoice.sdk.config.BVClientConfig;
import com.bazaarvoice.sdk.config.BVConfiguration;
import com.bazaarvoice.sdk.config.BVCoreConfig;

/**
 * Bazaarvoice utility class.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public final class BVSdkUtilty {
	
	/**
	 * TODO: Remove this method.
	 * Utility method to validate the configuration.
	 * @param bvConfiguration 	the configuration that is used to access BazaarVoice content.
	 */
	public static void validateConfiguration(BVConfiguration bvConfiguration) {
		if (bvConfiguration == null) {
			throw new BVSdkException("ERR0007");
		}
		
		/*
		 * Lets validate the server configurations
		 */
		for (BVCoreConfig bvServerConfig : BVCoreConfig.values()) {
			String propValue = bvConfiguration.getProperty(bvServerConfig.getPropertyName());
			if (StringUtils.isBlank(propValue)) {
				throw new BVSdkException("ERR0008");
			}
		}
		
		/*
		 * Now we will validate client configurations
		 */
		for (BVClientConfig bvClientConfig : BVClientConfig.values()) {
			String propValue = bvConfiguration.getProperty(bvClientConfig.getPropertyName());
			if (StringUtils.isBlank(propValue)) {
				if (bvClientConfig.equals(BVClientConfig.LOCAL_SEO_FILE_ROOT) &&
						!Boolean.parseBoolean(bvConfiguration.
								getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()))) {
					continue;
				}
				throw new BVSdkException("ERR0009");
			}
		}
	}
	
	public static int getPageNumber(String queryString) {
        if (queryString != null && queryString.length() > 0) {
            List<NameValuePair> parameters = URLEncodedUtils.parse (queryString, Charset.forName("UTF-8"));
            for(NameValuePair parameter : parameters) {
                if (parameter.getName().equals("bvrrp") || parameter.getName().equals("bvqap") || parameter.getName().equals("bvsyp")) {
                    final Pattern p = Pattern.compile("^[^/]+/\\w+/\\w+/(\\d+)/[^/]+\\.htm$");
                    return matchPageNumber(p, parameter.getValue());
                } else if (parameter.getName().equals("bvpage")) {
                    final Pattern p = Pattern.compile("^\\w+/(\\d+)$");
                    return matchPageNumber(p, parameter.getValue());
                }
            }
        }
        return 1;
    }
	
	private static int matchPageNumber(Pattern pattern, String value) {
        Matcher m = pattern.matcher(value);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        } else {
            return 1;
        }
    }
	
	public static String getQueryString(String uri) {
        final URI _uri;
        try {
            _uri = new URI(uri);
        } catch (Exception ex) {
//            _log.warn("Unable to parse URL: " + uri);
            return null;
        }
        return _uri.getQuery();
    }
	
	public static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.forName("UTF-8").decode(bb).toString();
        }
        finally {
            stream.close();
        }
    }
	
	/**
	 * Utility method to replace the string from StringBuilder.
	 * @param sb			StringBuilder object.
	 * @param toReplace		The String that should be replaced.
	 * @param replacement	The String that has to be replaced by.
	 * 
	 */
	public static void replaceString(StringBuilder sb, String toReplace, String replacement) {
		int index = -1;
		while ((index = sb.lastIndexOf(toReplace)) != -1) {
			sb.replace(index, index + toReplace.length(), replacement);
		}
	}
	
}
