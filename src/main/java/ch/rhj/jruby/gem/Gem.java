package ch.rhj.jruby.gem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ch.rhj.io.Gzip;
import ch.rhj.io.Tar;

public class Gem {

	public final String name;
	public final String version;

	private byte[] gemTarBytes;
	private byte[] metadataGzBytes;
	private byte[] dataTarGzBytes;

	public Gem(String name, String version, byte[] bytes) {

		this.name = name;
		this.version = version;
		this.gemTarBytes = bytes.clone();
	}

	private void extractGemPart(String name, byte[] bytes) {

		if ("metadata.gz".equals(name))
			this.metadataGzBytes = bytes;

		if ("data.tar.gz".equals(name))
			this.dataTarGzBytes = bytes;
	}

	private void extractGem() throws IOException {

		if (gemTarBytes == null)
			return;

		ByteArrayInputStream input = new ByteArrayInputStream(gemTarBytes);
		Predicate<String> filter = s -> true;

		Tar.extract(input, filter, this::extractGemPart);

		gemTarBytes = null;
	}

	private void extractMetadata() throws IOException {

		extractGem();

		ByteArrayInputStream gzInput = new ByteArrayInputStream(metadataGzBytes);
		byte[] jsonBytes = Gzip.extract(gzInput);
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		JsonNode root = objectMapper.readTree(jsonBytes);

		System.out.println(root);
	}

	public void test() throws Exception {

		extractGem();
		extractMetadata();
	}
}
