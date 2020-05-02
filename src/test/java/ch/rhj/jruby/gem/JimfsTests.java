package ch.rhj.jruby.gem;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import ch.rhj.io.IO;

public class JimfsTests {

	@Test
	public void test() throws Exception {

		String expected = "hello, world!";

		FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
		Path foo = fs.getPath("/foo");

		Files.createDirectory(foo);

		Path hello = foo.resolve("hello.txt");

		Files.writeString(hello, expected);

		String uri = hello.toUri().toASCIIString();

		assertTrue(uri.startsWith("jimfs://"));
		assertTrue(uri.endsWith("/foo/hello.txt"));

		try (InputStream input = URI.create(uri).toURL().openStream()) {

			assertEquals(expected, new String(IO.read(input), UTF_8));
		}
	}
}
