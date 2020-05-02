package ch.rhj.jruby.gem;

import java.time.Instant;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specification {

	@JsonProperty("name")
	private String name;

	@JsonProperty("version")
	private Version version;

	@JsonProperty("date")
	private String date;

	@JsonProperty("files")
	private String[] files;

	public String name() {

		return name;
	}

	public Version version() {

		return version;
	}

	public Instant date() {

		return Instant.parse(date.substring(0, 10) + "T" + date.substring(11, 19) + "Z");
	}

	public Stream<String> files() {

		return Stream.of(files);
	}
}
