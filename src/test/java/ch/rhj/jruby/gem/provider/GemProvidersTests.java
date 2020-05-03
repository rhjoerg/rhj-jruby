package ch.rhj.jruby.gem.provider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import ch.rhj.jruby.gem.Gem;

public class GemProvidersTests {

	@Test
	public void testProviders() {

		Gem gem = GemProviders.instance().gem("rhj_mini_gem", "0.0.1");

		assertNotNull(gem);
	}
}
