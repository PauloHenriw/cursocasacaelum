package br.com.caelum.ingresso.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImagemCapa {

	@JsonProperty("Poster")
	String url;
	
	public String getUrl() {
		return url;
	}

	
	public 	void setUrl(String url) {
		this.url = url;
	}
}
