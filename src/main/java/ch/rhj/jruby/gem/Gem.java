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

	private byte[] gemTarBytes;

	private final Map<String, byte[]> gemContents = new TreeMap<>();
	private final Map<String, byte[]> files = new TreeMap<>();

	private Specification specification;

	public Gem(byte[] bytes) {

		this.gemTarBytes = bytes.clone();
	}

	private void extractGem() {

		if (gemTarBytes == null)
			return;

		Tar.extract(gemTarBytes, s -> true, (s, b) -> gemContents.put(s, b));

		gemTarBytes = null;
	}

	private void extractMetadata() {

		extractGem();

		byte[] contents = gemContents.get(METADATA_GZ_KEY);

		if (contents == null)
			return;

		specification = Yaml.read(Gzip.extract(contents), Specification.class);
		gemContents.remove(METADATA_GZ_KEY);
	}

	private void extractData() {

		extractGem();

		byte[] contents = gemContents.get(DATA_TAR_GZ_KEY);

		if (contents == null)
			return;

		Tar.extract(Gzip.extract(contents), s -> true, (n, b) -> files.put(n, b));
		gemContents.remove(DATA_TAR_GZ_KEY);
	}

	public Specification specification() {

		extractMetadata();

		return specification;
	}

	public byte[] file(String name) {

		extractData();

		return files.get(name);
	}

	public Path install(Path directory) {

		Path subdirectory = directory.resolve(specification().name());

		specification().files().forEach(name -> IO.write(file(name), subdirectory.resolve(name), true));

		return subdirectory;
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
