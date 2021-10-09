package com.practice.exception;

/**
 * Responsible to manage exception at the time of processing request via filter chain.
 * 
 * @author manish.luste
 */
public class CongitoJWTException extends Exception {

	private static final long serialVersionUID = 1L;
	private final int errorCode;
	private final String errorMessage;

	public CongitoJWTException(String errorMessage, int code) {
		super(errorMessage);
		this.errorCode = code;
		this.errorMessage = errorMessage;
	}

	public CongitoJWTException(String errorMessage, Throwable e, int errorCode) {
		super(errorMessage, e);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String printDetail() {
		return "UnAuthorizedException [errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}

}
