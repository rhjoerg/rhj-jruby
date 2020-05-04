package ch.rhj.jruby;

import static ch.rhj.util.Singleton.singleton;

import java.util.function.Function;

import ch.rhj.io.IO;
import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;

public class Config {

	public final static String PROPERTIES_NAME = "rhj-jruby.properties";
	public final static String PREFIX = "ch.rhj.jruby";

	private final static Singleton<Config> instance = singleton(Config::new);

	private final Cfg cfg;

	private Config() {

		cfg = Cfg.cfg(PREFIX, true, IO.allProperties(PROPERTIES_NAME));
	}

	public <T> T child(String prefix, Function<Cfg, T> factory) {

		return factory.apply(cfg.sub(prefix));
	}

	public static Config instance() {

		return instance.get();
	}
}
