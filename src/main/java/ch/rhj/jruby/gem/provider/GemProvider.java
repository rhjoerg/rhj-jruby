package ch.rhj.jruby.gem.provider;

import ch.rhj.jruby.gem.Gem;

public interface GemProvider {

	public int order();

	public Gem gem(String name, String version);
}
