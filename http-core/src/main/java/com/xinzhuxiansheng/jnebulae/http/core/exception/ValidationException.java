package com.xinzhuxiansheng.jnebulae.http.core.exception;

/**
 * ValidationException
 * @author houyi.wh
 * @date 2017-10-20
 */
public class ValidationException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ValidationException(String s) {
        super(s);
    }

	public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }


}
