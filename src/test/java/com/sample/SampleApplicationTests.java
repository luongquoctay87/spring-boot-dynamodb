package com.sample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

//@SpringBootTest
@Profile("test")
class SampleApplicationTests {

	@Test
	void contextLoads() {
	}

}
