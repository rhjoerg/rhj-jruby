package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ch.rhj.io.IO;
import ch.rhj.jruby.Config;
import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;
import ch.rhj.util.SysProps;

public class FolderGemProviderConfig {

	public static final String PREFIX = "folder";

	private static final Singleton<FolderGemProviderConfig> instance = singleton( //
			() -> Config.instance().child(PREFIX, FolderGemProviderConfig::new));

	private final Cfg cfg;

	private FolderGemProviderConfig(Cfg cfg) {

		this.cfg = cfg;
	}

	public List<Path> locations() {

		List<Path> locations = new ArrayList<>();
		Path workingDir = SysProps.workingDir();
		List<Path> paths = cfg.values().stream().map(n -> Paths.get(n)).collect(toList());

		paths.stream().filter(p -> p.isAbsolute()).filter(IO::exists).forEach(locations::add);
		paths.stream().filter(p -> !p.isAbsolute()).map(workingDir::resolve).filter(IO::exists).forEach(locations::add);
		locations.add(workingDir);

		return locations;
	}

	public static FolderGemProviderConfig instance() {

		return instance.get();
	}
}
