package com.practice.apigetway.exception;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ErrorResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int status;

	private String errorCode;

	private String errorDescription;

	private String path;
	
	private long timestamp;
	
	public ErrorResponseDTO(int status, String errorCode, String errorDescription, String path) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.path = path;
		this.timestamp = System.currentTimeMillis();
	}
	
	public ErrorResponseDTO(int status, String errorCode, String errorDescription) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.path = StringUtils.EMPTY;
		this.timestamp = System.currentTimeMillis();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ErrorResponseDTO [status=" + status + ", errorCode=" + errorCode + ", errorDescription="
				+ errorDescription + ", path=" + path + ", timestamp=" + timestamp + "]";
	}

}
