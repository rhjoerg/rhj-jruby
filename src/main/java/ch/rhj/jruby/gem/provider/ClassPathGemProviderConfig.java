package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import ch.rhj.io.IO;
import ch.rhj.jruby.AbstractConfig;
import ch.rhj.util.Singleton;

public class ClassPathGemProviderConfig extends AbstractConfig {

	public static final String PREFIX = "classpath";

	private static final Singleton<ClassPathGemProviderConfig> instance = singleton(ClassPathGemProviderConfig::new);

	private final List<Path> locations;

	private ClassPathGemProviderConfig() {

		super(GemProviderConfig.instance().properties(PREFIX));

		locations = properties //
				.stringPropertyNames().stream() //
				.map(n -> properties.getProperty(n)) //
				.map(n -> IO.classLoaderPath(n)) //
				.filter(p -> p != null) //
				.filter(IO::exists) //
				.collect(toList());

		Arrays.asList("rubygems", "META-INF/rubygems").stream() //
				.map(IO::classLoaderPath) //
				.filter(p -> p != null) //
				.filter(IO::exists) //
				.forEach(locations::add);
	}

	public Stream<Path> locations() {

		return locations.stream();
	}

	public static ClassPathGemProviderConfig instance() {

		return instance.get();
	}
}
