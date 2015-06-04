package org.yardstickframework.gemfire;

import org.yardstickframework.BenchmarkUtils;

import com.gemstone.gemfire.distributed.LocatorLauncher;
import com.gemstone.gemfire.distributed.LocatorLauncher.LocatorState;

public class GemfireLocator {

  private LocatorLauncher locatorLauncher;

  public GemfireLocator() {
    locatorLauncher = new LocatorLauncher.Builder()
    .setMemberName("locator1")
    .setPort(13489)
    .build();
  }

  public LocatorState startLocator(){
    LocatorState state = locatorLauncher.start();
    locatorLauncher.waitOnLocator();
    BenchmarkUtils.println("Started Gemfire locator " + locatorLauncher.getId());
    return state;
  }
  
  public LocatorState stopLocator() {
    return locatorLauncher.stop();
  }
}
