package ch.rhj.jruby.gem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class JimfsTests {

	@Test
	public void test() throws Exception {

		FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
		Path foo = fs.getPath("/foo");

		Files.createDirectory(foo);

		Path hello = foo.resolve("hello.txt");

		Files.writeString(hello, "hello, world!");

		String uri = hello.toUri().toASCIIString();

		assertTrue(uri.startsWith("jimfs://"));
		assertTrue(uri.endsWith("/foo/hello.txt"));
	}
}
