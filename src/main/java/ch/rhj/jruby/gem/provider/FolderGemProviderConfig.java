package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import ch.rhj.jruby.AbstractConfig;
import ch.rhj.util.Singleton;
import ch.rhj.util.SysProps;

public class FolderGemProviderConfig extends AbstractConfig {

	public static final String PREFIX = "folder";

	private static final Singleton<FolderGemProviderConfig> instance = singleton(FolderGemProviderConfig::new);

	private final List<Path> locations;

	private FolderGemProviderConfig() {

		super(GemProviderConfig.instance().properties(PREFIX));

		Path workingDir = SysProps.workingDir();
		List<String> names = properties.stringPropertyNames().stream().map(n -> properties.getProperty(n)).collect(toList());

		locations = names.stream() //
				.map(n -> Paths.get(n)) //
				.filter(p -> p.isAbsolute()) //
				.collect(toList());

		names.stream() //
				.map(n -> Paths.get(n)) //
				.filter(p -> !p.isAbsolute()) //
				.map(p -> workingDir.resolve(p)) //
				.forEach(p -> locations.add(p));

		locations.add(workingDir);
	}

	public Stream<Path> locations() {

		return locations.stream();
	}

	public static FolderGemProviderConfig instance() {

		return instance.get();
	}
}
