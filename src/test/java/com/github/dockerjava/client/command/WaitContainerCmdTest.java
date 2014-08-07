package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class WaitContainerCmdTest extends AbstractDockerClientTest {

	@BeforeTest
	public void beforeTest() throws DockerException {
		super.beforeTest();
	}

	@AfterTest
	public void afterTest() {
		super.afterTest();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		super.beforeMethod(method);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		super.afterMethod(result);
	}

	@Test
	public void testWaitContainer() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		tmpContainers.add(container.getId());

		dockerClient.startContainerCmd(container.getId()).exec();

		int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();
		LOG.info("Container exit code: {}", exitCode);

		assertThat(exitCode, equalTo(0));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();
		LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

		assertThat(inspectContainerResponse.getState().isRunning(),	is(equalTo(false)));
		assertThat(inspectContainerResponse.getState().getExitCode(), is(equalTo(exitCode)));

	}



}
