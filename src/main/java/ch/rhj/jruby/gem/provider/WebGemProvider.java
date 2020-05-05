package ch.rhj.jruby.gem.provider;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import ch.rhj.io.IO;
import ch.rhj.jruby.gem.Gem;
import ch.rhj.util.Ex;

public class WebGemProvider implements GemProvider {

	public final static int ORDER = ClassPathGemProvider.ORDER + 1000;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	@Override
	public int order() {

		return ORDER;
	}

	@Override
	public Gem gem(String name, String version) {

		for (String pattern : WebGemProviderConfig.instance().patterns()) {

			URI uri = Ex.supply(() -> URI.create(pattern.replace("NAME", name).replace("VERSION", version)));
			HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
			byte[] bytes = IO.read(httpClient, request);

			if (bytes != null)
				return new Gem(bytes);
		}

		return null;
	}
}
