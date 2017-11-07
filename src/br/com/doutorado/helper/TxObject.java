package br.com.doutorado.helper;

public class TxObject {
	
	private String tx;
	private String country;
	
	public TxObject(String tx, String country) {
		this.tx = tx;
		this.country = country;
	}

	public String getTx() {
		return tx;
	}

	public String getCountry() {
		return country;
	}
}
