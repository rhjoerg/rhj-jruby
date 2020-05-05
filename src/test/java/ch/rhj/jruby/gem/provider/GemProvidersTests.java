package ch.rhj.jruby.gem.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class GemProvidersTests {

	@Test
	public void testProviders() {

		GemProviders providers = GemProviders.instance();

		assertNotNull(providers.gem("rhj_mini_gem", "0.0.1"));
		assertNotNull(providers.gem("faraday", "1.0.0"));
	}
}
