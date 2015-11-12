package com.sc.model;

public class NFC {

	private String nfcId;
	private String cardId;
	
	public NFC() {}
	public NFC(String nfcId, String cardId) {
		super();
		this.nfcId = nfcId;
		this.cardId = cardId;
	}
	public String getNfcId() {
		return nfcId;
	}
	public void setNfcId(String nfcId) {
		this.nfcId = nfcId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	@Override
	public String toString() {
		return "NFC [nfcId=" + nfcId + ", cardId=" + cardId + "]";
	}
	
	
}
