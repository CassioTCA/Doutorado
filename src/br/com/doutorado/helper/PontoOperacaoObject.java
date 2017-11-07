package br.com.doutorado.helper;

public class PontoOperacaoObject {
	
	private String country;
	private String powerRange;
	private String load;
	private String annualOperatingHours;
	
	public PontoOperacaoObject(String country, String powerRange, String load, String annualOperatingHours) {
		super();
		this.country = country;
		this.powerRange = powerRange;
		this.load = load;
		this.annualOperatingHours = annualOperatingHours;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPowerRange() {
		return powerRange;
	}

	public void setPowerRange(String powerRange) {
		this.powerRange = powerRange;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getAnnualOperatingHours() {
		return annualOperatingHours;
	}

	public void setAnnualOperatingHours(String annualOperatingHours) {
		this.annualOperatingHours = annualOperatingHours;
	}
}
