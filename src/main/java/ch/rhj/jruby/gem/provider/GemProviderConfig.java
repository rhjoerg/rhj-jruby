package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;

import java.util.function.Function;

import ch.rhj.jruby.Config;
import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;

public class GemProviderConfig {

	public final static String PREFIX = "gem.provider";

	private final static Singleton<GemProviderConfig> instance = singleton( //
			() -> Config.instance().child(PREFIX, GemProviderConfig::new));

	private final Cfg cfg;

	private GemProviderConfig(Cfg cfg) {

		this.cfg = cfg;
	}

	public <T> T child(String prefix, Function<Cfg, T> factory) {

		return factory.apply(cfg.sub(prefix));
	}

	public static GemProviderConfig instance() {

		return instance.get();
	}
}
