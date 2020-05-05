package ch.rhj.jruby.gem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import ch.rhj.io.IO;

public class GemTests {

	private final static Path TEST_DIRECTORY = Paths.get("target", "test-data", "GemTests");

	private final Path rubyGemSource = IO.classLoaderPath("rubygems");
	private final Path rhjMiniGemSource = rubyGemSource.resolve("rhj_mini_gem-0.0.1.gem");

	@Test
	public void testMetadata() {

		Gem gem = null;
		Specification specification;

		gem = new Gem(IO.read(rhjMiniGemSource));
		specification = gem.specification();

		assertEquals("rhj_mini_gem", specification.name());
		assertEquals("0.0.1", specification.version().version());
		assertEquals(1588377600000L, specification.date().toEpochMilli());
		assertEquals(1L, specification.files().count());
		specification.files().map(gem::file).forEach(b -> assertNotNull(b));
	}

	@Test
	public void testHashAndEqual() {

		byte[] bytes = IO.read(rhjMiniGemSource);
		Gem gem1 = new Gem(bytes);
		Gem gem2 = new Gem(bytes);

		assertEquals(gem1.hashCode(), gem2.hashCode());
		assertTrue(gem1.equals(gem2));
		assertEquals(0, gem1.compareTo(gem2));
	}

	@Test
	public void testInstall() throws Exception {

		Gem gem = new Gem(IO.read(rhjMiniGemSource));
		Path directory = TEST_DIRECTORY.resolve("testInstall");
		Path file = directory.resolve("rhj_mini_gem/lib/rhj_mini_gem.rb");

		gem.install(directory, true);
		assertTrue(IO.exists(file));
	}
}
