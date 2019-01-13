/**
 * 
 */
package com.GlobalPayments.HazelcastPOC.model;

import java.io.Serializable;

/**
 * @author rredd
 *
 */
public class GlobalToken implements Serializable {
	private static final long serialVersionUID = -2198928914280590576L;

	private final String token;

	public GlobalToken(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
