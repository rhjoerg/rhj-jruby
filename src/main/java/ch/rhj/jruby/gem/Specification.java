package ch.rhj.jruby.gem;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specification implements Comparable<Specification> {

	public final static Comparator<Specification> NAME_COMPARATOR = (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.name(), o2.name());
	public final static Comparator<Specification> VERSION_COMPARATOR = (o1, o2) -> o1.version().compareTo(o2.version());
	public final static Comparator<Specification> COMPARATOR = NAME_COMPARATOR.thenComparing(VERSION_COMPARATOR);

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

	@Override
	public int hashCode() {

		return name.hashCode() + version.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		Specification other = Specification.class.cast(obj);

		return name.equalsIgnoreCase(other.name) && version.equals(other.version);
	}

	@Override
	public int compareTo(Specification other) {

		return COMPARATOR.compare(this, other);
	}
}
