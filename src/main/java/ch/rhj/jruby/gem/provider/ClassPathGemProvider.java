package ch.rhj.jruby.gem.provider;

import static java.util.stream.Collectors.toList;

import java.util.List;

import ch.rhj.io.IO;
import ch.rhj.jruby.gem.Gem;

public class ClassPathGemProvider implements GemProvider {

	public final static int ORDER = 0;

	@Override
	public int order() {

		return ORDER;
	}

	@Override
	public Gem gem(String name, String version) {

		String fullName = name + "-" + version + ".gem";

		List<Gem> gems = ClassPathGemProviderConfig //
				.instance().locations().stream() //
				.map(p -> p.resolve(fullName)) //
				.filter(IO::exists) //
				.map(IO::read).map(Gem::new) //
				.collect(toList());

		return gems.isEmpty() ? null : gems.get(0);
	}
}
