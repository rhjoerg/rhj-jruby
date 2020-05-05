package ch.rhj.jruby.gem;

import static ch.rhj.util.Singleton.singleton;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import ch.rhj.jruby.Config;
import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;
import ch.rhj.util.SysProps;

public class GemsConfig {

	public final static String PREFIX = "gems";

	private static final Singleton<GemsConfig> instance = singleton( //
			() -> Config.instance().child(PREFIX, GemsConfig::new));

	private final Cfg cfg;
	private final Path defaultLibDirectory;

	private GemsConfig(Cfg cfg) {

		this.cfg = cfg;
		this.defaultLibDirectory = Jimfs.newFileSystem(Configuration.unix()).getPath("/");
	}

	private Path directory(String key) {

		String path = cfg.get(key);

		return path == null ? null : Paths.get(path);
	}

	public Path gemDirectory() {

		Path directory = directory("gem-directory");

		return directory == null ? defaultGemDirector() : directory;
	}

	public Path defaultGemDirector() {

		Path directory = directory("default-gem-directory");

		return directory == null ? SysProps.workingDir().resolve("rubygems") : directory;
	}

	public Path libDirectory() {

		String path = cfg.get("lib-directory");

		return path == null ? defaultLibDirectory() : Paths.get(path);
	}

	public Path defaultLibDirectory() {

		return defaultLibDirectory;
	}

	public static GemsConfig instance() {

		return instance.get();
	}
}
