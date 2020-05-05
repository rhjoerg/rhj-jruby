package ch.rhj.jruby.gem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import ch.rhj.io.IO;
import ch.rhj.util.Yaml;

public class GemsTests {

	@Test
	public void testSpecification() {

		Specification specification = Yaml.read(IO.readResource("travis.metadata"), Specification.class);

		assertEquals(12, specification.dependencies().count());
	}

	@Test
	public void testResolve() {

		Logger.getLogger("ch.rhj.io").setLevel(Level.FINE);

		Path directory = Paths.get("target", "test-data", "GemsTests", "rubygems");
		Gems gems = Gems.instance();

		assertTrue(gems.resolve("travis", "1.9.1.travis.1208.9"));
		gems.store(directory, true);
	}
}
