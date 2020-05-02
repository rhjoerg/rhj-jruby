package ch.rhj.jruby.gem;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import ch.rhj.io.IO;

public class Experiments {

	@Test
	@Disabled // works, but is slow
	public void experiment1() throws Exception {

		RubyInstanceConfig cfg = new RubyInstanceConfig();
		ArrayList<String> loadPaths = new ArrayList<>(cfg.getLoadPaths());

		loadPaths.add("uri:classloader:/ruby.libs");
		cfg.setLoadPaths(loadPaths);

		Ruby rb = Ruby.newInstance(cfg);

		IRubyObject o1 = rb.evalScriptlet("require 'rhj_mini_gem'");
		IRubyObject o2 = rb.evalScriptlet("RhjMiniGem::WhoIs.awesome?");

		assertTrue(o1.isTrue());
		assertTrue(o2.isNil());
	}

	@Test
	@Disabled // doesn't work
	public void experiment2() throws Exception {

		RubyInstanceConfig cfg = new RubyInstanceConfig();
		ArrayList<String> loadPaths = new ArrayList<>(cfg.getLoadPaths());

		loadPaths.add("uri:classloader:/ruby.gems");
		cfg.setLoadPaths(loadPaths);

		Ruby rb = Ruby.newInstance(cfg);

		IRubyObject o1 = rb.evalScriptlet("require 'rhj_mini_gem'");
		IRubyObject o2 = rb.evalScriptlet("RhjMiniGem::WhoIs.awesome?");

		assertTrue(o1.isTrue());
		assertTrue(o2.isNil());
	}

	@Test
	@Disabled // works, but is slow
	public void experiment3() throws Exception {

		FileSystem vfs = Jimfs.newFileSystem(Configuration.unix());
		Path folder = vfs.getPath("ruby.gems");
		Path file = folder.resolve("rhj_mini_gem.rb");

		IO.write(IO.readResource("ruby.libs/rhj_mini_gem.rb"), file, true);

		RubyInstanceConfig cfg = new RubyInstanceConfig();
		ArrayList<String> loadPaths = new ArrayList<>(cfg.getLoadPaths());

		String uri = "uri:" + folder.toUri().toASCIIString();

		uri = uri.substring(0, uri.length() - 1);
		assertFalse(uri.endsWith("/"));

		loadPaths.add("uri:" + folder.toUri().toASCIIString());
		cfg.setLoadPaths(loadPaths);

		Ruby rb = Ruby.newInstance(cfg);

		IRubyObject o1 = rb.evalScriptlet("require 'rhj_mini_gem'");
		IRubyObject o2 = rb.evalScriptlet("RhjMiniGem::WhoIs.awesome?");

		assertTrue(o1.isTrue());
		assertTrue(o2.isNil());
	}
}
