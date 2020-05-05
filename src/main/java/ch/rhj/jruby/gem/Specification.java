package ch.rhj.jruby.gem;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import ch.rhj.util.Versions;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Specification implements Comparable<Specification> {

	public final static Comparator<Specification> NAME_COMPARATOR = (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.name(), o2.name());
	public final static Comparator<Specification> VERSION_COMPARATOR = (o1, o2) -> o1.version().compareTo(o2.version());
	public final static Comparator<Specification> COMPARATOR = NAME_COMPARATOR.thenComparing(VERSION_COMPARATOR);

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Version implements Comparable<Version> {

		public final static Comparator<Version> COMPARATOR = (o1, o2) -> o1.compareTo(o2);

		@JsonProperty("version")
		private String version;

		protected Version() {
		}

		public Version(String version) {

			this.version = version;
		}

		public String version() {

			return version;
		}

		public boolean satisfies(String version) {

			return Versions.compare(version, this.version) <= 0;
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

		@Override
		public String toString() {

			return version;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Requirement {

		@JsonProperty("requirements")
		private JsonNode[] requirements;

		@JsonIgnore
		private Version version;

		@JsonIgnore
		private Version minVersion;

		@JsonIgnore
		private Version maxVersion;

		private void initVersions() {

			for (JsonNode req : requirements) {

				String op = req.get(0).asText();
				String v = req.get(1).get("version").asText();

				if ("~>".equals(op)) {

					this.version = new Version(v);
					continue;
				}

				if (">=".equals(op)) {

					this.minVersion = new Version(v);
					continue;
				}

				if ("<".equals(op)) {

					this.maxVersion = new Version(v);
					continue;
				}

				throw new RuntimeException(String.format("unknown operator '%1$s'", op));
			}

			if (minVersion == null)
				minVersion = version;

			if (maxVersion == null)
				maxVersion = new Version(Integer.toString(Integer.MAX_VALUE));
		}

		public Version minVersion() {

			initVersions();

			return minVersion;
		}

//		public Version version() {
//
//			return Stream.of(requirements) //
//					.map(r -> r.get(1).get("version").asText()) //
//					.map(Version::new) //
//					.max(Version.COMPARATOR).get();
//		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Dependency {

		@JsonProperty("name")
		private String name;

		@JsonProperty("type")
		private String type;

		@JsonProperty("requirement")
		private Requirement requirement;

		public String name() {

			return name;
		}

		public String type() {

			return type;
		}

		public Version minVersion() {

			return requirement.minVersion();
		}

		@Override
		public String toString() {

			return name();
		}
	}

	@JsonProperty("name")
	private String name;

	@JsonProperty("version")
	private Version version;

	@JsonProperty("date")
	private String date;

	@JsonProperty("files")
	private String[] files;

	@JsonProperty("dependencies")
	private Dependency[] dependencies;

	public String name() {

		return name;
	}

	public Version version() {

		return version;
	}

	public String fullName() {

		return name + "-" + version.version() + ".gem";
	}

	public Instant date() {

		return Instant.parse(date.substring(0, 10) + "T" + date.substring(11, 19) + "Z");
	}

	public Stream<String> files() {

		return Stream.of(files);
	}

	public Stream<Dependency> dependencies() {

		return Stream.of(dependencies);
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
