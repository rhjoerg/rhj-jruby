package ch.rhj.jruby;

import static ch.rhj.jruby.RubyBuilder.rubyBuilder;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jruby.Ruby;
import org.junit.jupiter.api.Test;

public class RubyBuilderTests {

	@Test
	public void test() {

		Ruby ruby = rubyBuilder().build();

		assertNotNull(ruby);
	}
}
