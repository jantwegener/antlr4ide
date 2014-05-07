package com.github.jknack.antlr4ide;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.Path;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.github.jknack.antlr4ide.generator.Distributions;
import com.github.jknack.antlr4ide.generator.ToolOptionsProvider;

public class ActivatorTest {

  @Test
  public void start() throws MalformedURLException {
    String path = "lib/" + ToolOptionsProvider.DEFAULT_TOOL;
    String runtime = "lib/antlr4ide.runtime.jar";
    File[] jars = {new File(Distributions.defaultDistribution().getValue()),
        new File(System.getProperty("java.io.tmpdir"), "antlr4ide.runtime.jar") };
    for (File jar : jars) {
      jar.delete();
    }

    Bundle bundle = createMock(Bundle.class);

    BundleContext context = createMock(BundleContext.class);
    expect(context.getBundle()).andReturn(bundle).times(2);

    expect(bundle.getSymbolicName()).andReturn("antlr4ide.core");

    expect(bundle.getResource(path)).andReturn(
        Path.fromOSString("..").append("antlr4ide.core").append("lib")
            .append(ToolOptionsProvider.DEFAULT_TOOL).
            toFile().toURI().toURL());

    expect(bundle.getResource(runtime)).andReturn(
        Path.fromOSString("..").append("antlr4ide.core").append("lib")
            .append("antlr4ide.runtime.jar").
            toFile().toURI().toURL());

    Object[] mocks = {context, bundle };

    replay(mocks);

    new Activator().start(context);

    // must be created again
    for (File jar : jars) {
      assertTrue(jar.exists());
    }

    verify(mocks);
  }

}
