package com.practice.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "aws.cognito"
)
public class AwsPropertyConfiguration {
	
	// credentials
	private String clientId;
    private String userPoolId;
    private String region;
    private String accessKey;
    private String secretKey;
    
   
    // aws api's
    private String jwkUrl;
    private String poolUrl;
    
    // secure-un-secure apis
    private List<String> unAuthenticatedPath;
    private List<String> authenticatedPath;
    
    // custom attributes
    private String userNameField = "username";
    private int connectionTimeout = 2000;
    private int readTimeout = 2000;
    private String httpHeader = "Authorization";

    public void setJwkUrl(String jwkUrl) {
        this.jwkUrl = jwkUrl;
    }
    public String getJwkUrl() {
        return String.format(this.jwkUrl, this.region, this.userPoolId);
    }
  
	public String getPoolUrl() {
  		return String.format(this.poolUrl, this.region, this.userPoolId);
	}
	
	public void setPoolUrl(String poolUrl) {
		this.poolUrl = poolUrl;
	}
	public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUserNameField() {
        return userNameField != null ? this.userNameField : "username";
    }

    public void setUserNameField(String userNameField) {
        this.userNameField = userNameField;
    }

    public int getConnectionTimeout() {
        return connectionTimeout > 0 ? this.connectionTimeout : 2000 ;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout > 0 ? this.readTimeout : 2000;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getHttpHeader() {
        return httpHeader != null ? this.httpHeader : "Authorization" ;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

	public List<String> getUnAuthenticatedPath() {
		return unAuthenticatedPath;
	}
	public void setUnAuthenticatedPath(List<String> unAuthenticatedPath) {
		this.unAuthenticatedPath = unAuthenticatedPath;
	}

	public List<String> getAuthenticatedPath() {
		return authenticatedPath;
	}

	public void setAuthenticatedPath(List<String> authenticatedPath) {
		this.authenticatedPath = authenticatedPath;
	}
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String[] getCommaSeperatedAuthenticatedApiString() {
		if (null != authenticatedPath && !authenticatedPath.isEmpty()) {
			String[]  authenticatedApiArray = new String[authenticatedPath.size()];
			authenticatedPath.toArray( authenticatedApiArray );
			return authenticatedApiArray;
		}
		return new String[0];
	}
	
	public String[] getCommaSeperatedUnAuthenticatedApiString() {
		if (null != unAuthenticatedPath && !unAuthenticatedPath.isEmpty()) {
			String[] unAuthenticatedApiArray = new String[unAuthenticatedPath.size()];
			unAuthenticatedPath.toArray( unAuthenticatedApiArray );
			return unAuthenticatedApiArray;
		}
		return new String[0];
	}
}
