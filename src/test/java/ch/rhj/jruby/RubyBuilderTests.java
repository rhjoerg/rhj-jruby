package ch.rhj.jruby;

import static ch.rhj.jruby.RubyBuilder.rubyBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jruby.Ruby;

import ch.rhj.util.Cfg;

public class RubyBuilderTests {

	// @Test // slow. dependes on system property TRAVIS_TOKEN
	public void test() {

		Cfg system = Cfg.system();
		String token = system.get("TRAVIS_TOKEN");

		assertNotNull(token);

		Ruby ruby = rubyBuilder() //
				.require("travis", "1.9.1.travis.1208.9") //
				.build();

		String login = String.format("Travis.access_token = '%1$s'", token);

		assertTrue(ruby.evalScriptlet("require 'travis'").isTrue());
		assertEquals(token, ruby.evalScriptlet(login).asJavaString());

		System.out.println(ruby.evalScriptlet("Travis::User.current.name"));
	}
}
