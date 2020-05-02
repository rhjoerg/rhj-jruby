package ch.rhj.jruby.gem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.rhj.util.Versions;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Version implements Comparable<Version> {

	@JsonProperty("version")
	private String version;

	public String version() {

		return version;
	}

	@Override
	public int hashCode() {

		return version.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		return version.equals(Version.class.cast(obj).version);
	}

	@Override
	public int compareTo(Version o) {

		return Versions.compare(version, o.version);
	}
}
