package ch.rhj.jruby.gem.provider;

import static ch.rhj.util.Singleton.singleton;

import java.util.Set;

import ch.rhj.util.Cfg;
import ch.rhj.util.Singleton;

public class WebGemProviderConfig {

	public final static String PREFIX = "web";

	private static final Singleton<WebGemProviderConfig> instance = singleton( //
			() -> GemProviderConfig.instance().child(PREFIX, WebGemProviderConfig::new));

	private final Cfg cfg;

	private WebGemProviderConfig(Cfg cfg) {

		this.cfg = cfg;
	}

	public Set<String> patterns() {

		return cfg.values();
	}

	public static WebGemProviderConfig instance() {

		return instance.get();
	}
}
