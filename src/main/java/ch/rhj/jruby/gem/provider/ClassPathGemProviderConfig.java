package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;
import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import ch.rhj.io.IO;
import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;

public class ClassPathGemProviderConfig {

	public static final String PREFIX = "classpath";

	private static final Singleton<ClassPathGemProviderConfig> instance = singleton( //
			() -> GemProviderConfig.instance().child(PREFIX, ClassPathGemProviderConfig::new));

	private final Cfg cfg;

	private ClassPathGemProviderConfig(Cfg cfg) {

		this.cfg = cfg;
	}

	public List<Path> locations() {

		Stream<String> cfgNames = cfg.values().stream();
		Stream<String> defNames = Stream.of("rubygems", "META-INF/rubygems");

		return Stream.concat(cfgNames, defNames) //
				.map(IO::classLoaderPath) //
				.filter(p -> p != null).filter(IO::exists) //
				.collect(toList());
	}

	public static ClassPathGemProviderConfig instance() {

		return instance.get();
	}
}
