package ch.rhj.jruby.gem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

	@JsonProperty("version")
	private String version;

	public String version() {

		return version;
	}
}
