/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.session.data.couchbase.config.annotation.web.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link CouchbaseHttpSessionConfiguration}.
 *
 * @author Eddú Meléndez
 */
public class CouchbaseHttpSessionConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@Before
	public void before() {
		this.context = new AnnotationConfigApplicationContext();
	}

	@After
	public void after() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testDefaultValues() {
		registerAndRefresh(DefaultCouchbaseHttpSessionConfiguration.class);
		CouchbaseHttpSessionConfiguration configuration = this.context.getBean(CouchbaseHttpSessionConfiguration.class);
		assertThat(ReflectionTestUtils.getField(configuration, "timeoutInSeconds")).isEqualTo(1800);
		assertThat(ReflectionTestUtils.getField(configuration, "principalSessionsEnabled")).isEqualTo(false);
	}

	@Test
	public void testCustomTimeoutInSeconds() {
		registerAndRefresh(BaseConfiguration.class, CustomTimeoutInSecondsConfiguration.class);
		CouchbaseHttpSessionConfiguration configuration = this.context.getBean(CouchbaseHttpSessionConfiguration.class);
		assertThat(ReflectionTestUtils.getField(configuration, "timeoutInSeconds")).isEqualTo(15);
	}

	@Test
	public void testPrincipalSessionsEnabled() {
		registerAndRefresh(BaseConfiguration.class, CustomPrincipalSessionsEnabledConfiguration.class);
		CouchbaseHttpSessionConfiguration configuration = this.context.getBean(CouchbaseHttpSessionConfiguration.class);
		assertThat(ReflectionTestUtils.getField(configuration, "principalSessionsEnabled")).isEqualTo(true);
	}

	@Test
	public void testSetCustomTimeoutInSeconds() {
		registerAndRefresh(BaseConfiguration.class, CustomTimeoutInSecondsSetConfiguration.class);
		CouchbaseHttpSessionConfiguration configuration = this.context.getBean(CouchbaseHttpSessionConfiguration.class);
		assertThat(ReflectionTestUtils.getField(configuration, "timeoutInSeconds")).isEqualTo(10);
	}

	@Test
	public void testSetPrincipalSessionsEnabled() {
		registerAndRefresh(BaseConfiguration.class, CustomPrincipalSessionsEnabledSetConfiguration.class);
		CouchbaseHttpSessionConfiguration configuration = this.context.getBean(CouchbaseHttpSessionConfiguration.class);
		assertThat(ReflectionTestUtils.getField(configuration, "principalSessionsEnabled")).isEqualTo(true);
	}

	private void registerAndRefresh(Class<?>... annotatedClasses) {
		this.context.register(annotatedClasses);
		this.context.refresh();
	}

	static class BaseConfiguration {

		@Bean
		public CouchbaseTemplate couchbaseTemplate() {
			return mock(CouchbaseTemplate.class);
		}

	}

	@Configuration
	@EnableCouchbaseHttpSession
	static class DefaultCouchbaseHttpSessionConfiguration extends BaseConfiguration {

	}

	@Configuration
	@EnableCouchbaseHttpSession(timeoutInSeconds = 15)
	static class CustomTimeoutInSecondsConfiguration {

	}

	@Configuration
	@EnableCouchbaseHttpSession(principalSessionsEnabled = true)
	static class CustomPrincipalSessionsEnabledConfiguration {

	}

	@Configuration
	static class CustomTimeoutInSecondsSetConfiguration extends CouchbaseHttpSessionConfiguration {

		CustomTimeoutInSecondsSetConfiguration() {
			setTimeoutInSeconds(10);
		}

	}

	@Configuration
	static class CustomPrincipalSessionsEnabledSetConfiguration extends CouchbaseHttpSessionConfiguration {

		CustomPrincipalSessionsEnabledSetConfiguration() {
			setPrincipalSessionsEnabled(true);
		}
	}

}
