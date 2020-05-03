package ch.rhj.jruby;

import static ch.rhj.util.Singleton.singleton;

import ch.rhj.io.Props;
import ch.rhj.util.Singleton;

public class Config extends AbstractConfig {

	public final static String PROPERTIES_NAME = "rhj-jruby.properties";
	public final static String PREFIX = "ch.rhj.jruby";

	private final static Singleton<Config> instance = singleton(Config::new);

	private Config() {

		super(Props.subProperties(Props.load(PROPERTIES_NAME, System.getProperties()), PREFIX));
	}

	public static Config instance() {

		return instance.get();
	}
}
