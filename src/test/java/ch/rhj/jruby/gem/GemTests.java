package ch.rhj.jruby.gem;

import static org.apache.commons.lang3.Functions.asFunction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import ch.rhj.io.IO;

public class GemTests {

	private final static Path TEST_DIRECTORY = Paths.get("target", "test-data", "GemTests");

	@Test
	public void testMetadata() throws Exception {

		Gem gem = null;
		Specification specification;

		gem = new Gem(IO.readResource("ruby.gems/rhj_mini_gem-0.0.1.gem"));
		specification = gem.specification();

		assertEquals("rhj_mini_gem", specification.name());
		assertEquals("0.0.1", specification.version().version());
		assertEquals(1588377600000L, specification.date().toEpochMilli());
		assertEquals(1L, specification.files().count());
		specification.files().map(asFunction(gem::file)).forEach(b -> assertNotNull(b));

		gem = new Gem(IO.readResource("ruby.gems/travis-1.9.1.travis.1208.9.gem"));
		specification = gem.specification();

		assertEquals("travis", specification.name());
		assertEquals("1.9.1.travis.1208.9", specification.version().version());
		assertEquals(1588032000000L, specification.date().toEpochMilli());
		assertEquals(187L, specification.files().count());
		specification.files().map(asFunction(gem::file)).forEach(b -> assertNotNull(b));
	}

	@Test
	public void testInstall() throws Exception {

		Gem gem = new Gem(IO.readResource("ruby.gems/rhj_mini_gem-0.0.1.gem"));
		Path directory = TEST_DIRECTORY.resolve("testInstall/rhj_mini_gem");
		Path file = directory.resolve("lib/rhj_mini_gem.rb");

		gem.install(directory);
		assertTrue(IO.exists(file));
	}
}
