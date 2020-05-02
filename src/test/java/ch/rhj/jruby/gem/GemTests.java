package ch.rhj.jruby.gem;

import org.junit.jupiter.api.Test;

import ch.rhj.io.IO;

public class GemTests {

	@Test
	public void test() throws Exception {

		byte[] bytes = IO.readResource("ruby.gems/rhj_mini_gem-0.0.1.gem");
		Gem gem = new Gem("rhj_mini_gem", "0.0.1", bytes);

		gem.test();
	}
}
