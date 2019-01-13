package com.GlobalPayments.HazelcastPOC.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token implements Serializable{
	@Id
	@Column(name = "pan_Token")
	private String panToken;
	@Column(name = "merchant_Token")
	private String merchantToken;
	@Column(name = "global_Token")
	private String globalToken;
	
	
	public String getPanToken() {
		return panToken;
	}
	public void setPanToken(String panToken) {
		this.panToken = panToken;
	}
	public String getMerchantToken() {
		return merchantToken;
	}
	public void setMerchantToken(String merchantToken) {
		this.merchantToken = merchantToken;
	}
	public String getGlobalToken() {
		return globalToken;
	}
	public void setGlobalToken(String globalToken) {
		this.globalToken = globalToken;
	}
	public Token(String panToken, String merchantToken, String globalToken) {
		super();
		this.panToken = panToken;
		this.merchantToken = merchantToken;
		this.globalToken = globalToken;
	}
	public Token() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Token [panToken=" + panToken + ", merchantToken=" + merchantToken + ", globalToken=" + globalToken
				+ "]";
	}
	
	

}
