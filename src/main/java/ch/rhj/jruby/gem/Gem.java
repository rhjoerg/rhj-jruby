package ch.rhj.jruby.gem;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import ch.rhj.io.Gzip;
import ch.rhj.io.IO;
import ch.rhj.io.Tar;
import ch.rhj.util.Yaml;

public class Gem implements Comparable<Gem> {

	public final static String METADATA_GZ_KEY = "metadata.gz";
	public final static String DATA_TAR_GZ_KEY = "data.tar.gz";

	public final static Comparator<Gem> COMPARATOR = (o1, o2) -> compare(o1, o2);

	private final byte[] bytes;
	private final Map<String, byte[]> files = new TreeMap<>();

	private Specification specification;

	public Gem(byte[] bytes) {

		Map<String, byte[]> contents = new TreeMap<>();

		this.bytes = bytes.clone();
		Tar.extract(bytes, s -> true, (s, b) -> contents.put(s, b));
		specification = Yaml.read(Gzip.extract(contents.get(METADATA_GZ_KEY)), Specification.class);
		Tar.extract(Gzip.extract(contents.get(DATA_TAR_GZ_KEY)), s -> true, (n, b) -> files.put(n, b));
	}

	public Specification specification() {

		return specification;
	}

	public byte[] file(String name) {

		return files.get(name);
	}

	private String fixUri(String uri) {

		if (uri.startsWith("jimfs:"))
			uri = "uri:" + uri;

		return uri;
	}

	public String install(Path directory, boolean replace) {

		Path subdirectory = directory.resolve(specification().name());

		specification().files().forEach(name -> IO.write(file(name), subdirectory.resolve(name), replace));

		return fixUri(subdirectory.resolve("lib").toUri().toString());
	}

	public String store(Path directory, boolean replace) {

		Path target = directory.resolve(specification.fullName());

		IO.write(bytes, target, replace);

		return fixUri(target.toUri().toString());
	}

	@Override
	public int hashCode() {

		return specification().hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		Gem other = Gem.class.cast(obj);

		return specification().equals(other.specification());
	}

	private static int compare(Gem o1, Gem o2) {

		return o1.specification().compareTo(o2.specification());
	}

	@Override
	public int compareTo(Gem other) {

		return compare(this, other);
	}
}
