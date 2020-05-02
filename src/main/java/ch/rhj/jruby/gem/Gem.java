package ch.rhj.jruby.gem;

import static org.apache.commons.lang3.Functions.asConsumer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ch.rhj.io.Gzip;
import ch.rhj.io.IO;
import ch.rhj.io.Tar;

public class Gem {

	private static final Consumer<byte[]> DEFAULT_BYTES_CONSUMER = b -> {
	};

	private byte[] gemTarBytes;
	private byte[] metadataGzBytes;
	private byte[] dataTarGzBytes;

	private Specification specification;
	private Map<String, byte[]> files;

	public Gem(byte[] bytes) {

		this.gemTarBytes = bytes.clone();
	}

	private void extractGem() throws IOException {

		if (gemTarBytes == null)
			return;

		Map<String, Consumer<byte[]>> consumers = Map.of(//
				"metadata.gz", b -> metadataGzBytes = b, //
				"data.tar.gz", b -> dataTarGzBytes = b);

		Tar.extract(gemTarBytes, s -> true, (s, b) -> consumers.getOrDefault(s, DEFAULT_BYTES_CONSUMER).accept(b));

		gemTarBytes = null;
	}

	private void extractMetadata() throws IOException {

		extractGem();

		if (metadataGzBytes == null)
			return;

		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

		specification = objectMapper.readValue(Gzip.extract(metadataGzBytes), Specification.class);
		metadataGzBytes = null;
	}

	public Specification specification() throws IOException {

		extractMetadata();

		return specification;
	}

	private void extractData() throws IOException {

		extractGem();

		if (dataTarGzBytes == null)
			return;

		files = new TreeMap<>();

		Tar.extract(Gzip.extract(dataTarGzBytes), s -> true, (n, b) -> files.put(n, b));
		dataTarGzBytes = null;
	}

	public byte[] file(String name) throws IOException {

		extractData();

		return files.get(name);
	}

	public void install(Path targetDirectory) throws IOException {

		specification().files().forEach(asConsumer(name -> IO.write(file(name), targetDirectory.resolve(name), true)));
	}
}
