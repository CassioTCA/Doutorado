package br.com.doutorado.helper;

public class EnergyCostObject {
	
	private String country;
	private String eletricityIndustry;
	private String eletricityHouseholds;
	private String currency;
	
	public EnergyCostObject(String country, String eletricityIndustry, String eletricityHouseholds, String currency) {
		super();
		this.country = country;
		this.eletricityIndustry = eletricityIndustry;
		this.eletricityHouseholds = eletricityHouseholds;
		this.currency = currency;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEletricityIndustry() {
		return eletricityIndustry;
	}

	public void setEletricityIndustry(String eletricityIndustry) {
		this.eletricityIndustry = eletricityIndustry;
	}

	public String getEletricityHouseholds() {
		return eletricityHouseholds;
	}

	public void setEletricityHouseholds(String eletricityHouseholds) {
		this.eletricityHouseholds = eletricityHouseholds;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
