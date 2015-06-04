/*
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.yardstickframework.gemfire;

import static org.yardstickframework.BenchmarkUtils.jcommander;
import static org.yardstickframework.BenchmarkUtils.println;

import java.util.Set;

import org.yardstickframework.BenchmarkConfiguration;
import org.yardstickframework.BenchmarkServer;
import org.yardstickframework.BenchmarkUtils;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.distributed.DistributedMember;

/**
 * Standalone Gemfire node.
 */
public class GemfireNode implements BenchmarkServer {
    /** */
    public GemfireNode() {
        // No-op.
    }
  
    /** Gemfire cache*/
    private Cache gemCache;
    
    /** {@inheritDoc} */
    @Override public void start(BenchmarkConfiguration cfg) throws Exception {      
        GemfireBenchmarkArguments benchArgs = new GemfireBenchmarkArguments();
        jcommander(cfg.commandLineArguments(), benchArgs, "<gemfire-node>");
        println(cfg, "Gemfire benchmark config: [" + benchArgs + "].");

        configureAndStart(benchArgs, cfg);  
        waitForNodes(benchArgs);

        println(cfg, "Gemfire member started.");
    }

    /**
     * Configure Gemfire .
     *
     * @param benchArgs Arguments.
     */
    private void configureAndStart(GemfireBenchmarkArguments benchArgs, BenchmarkConfiguration cfg) {
      if(gemCache == null){
        gemCache = new CacheFactory()
        .set("mcast-port", "10111")
        .set("log-level", "info")
        .set("cache-xml-file", benchArgs.configuration())
        .create();  
      }
    }

    /** {@inheritDoc} */
    @Override public void stop() throws Exception {
      gemCache.close();
      println("Stopped server node");
    }

    /** {@inheritDoc} */
    @Override public String usage() {
        return BenchmarkUtils.usage(new GemfireBenchmarkArguments());
    }
    
    /**
     * @throws Exception If failed.
     */
    private void waitForNodes(GemfireBenchmarkArguments benchArgs) throws Exception {
      boolean allStarted = false;
      while (!allStarted){
        Set<DistributedMember> members = gemCache.getDistributedSystem().getAllOtherMembers();
        if (members.size() >= benchArgs.nodes() - 2){          
          allStarted = true;
        }else{
          println("Waiting for " + (benchArgs.nodes() - 1) + " server nodes to start...");
          Thread.sleep(1000);
        }
      }
    }
}
