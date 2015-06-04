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

import java.util.concurrent.ThreadLocalRandom;

import org.yardstickframework.BenchmarkConfiguration;
import org.yardstickframework.BenchmarkDriverAdapter;
import org.yardstickframework.BenchmarkUtils;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;

/**
 * Abstract class for Gemfire benchmarks.
 */
public abstract class GemfireAbstractBenchmark extends BenchmarkDriverAdapter {
    
    /** Arguments. */
    protected final GemfireBenchmarkArguments args = new GemfireBenchmarkArguments();

    /** Node. */
    private GemfireNode node;

    /** Cache. */
    protected GemFireCache gemCache;
    
    /** Gemfrie Region. */
    protected Region<Integer, SampleValue> testRegion;
    
    /**
     * @param cacheName Cache name.
     */
    protected GemfireAbstractBenchmark() {
    }

    /** {@inheritDoc} */
    @Override public void setUp(BenchmarkConfiguration cfg) throws Exception {
        super.setUp(cfg);

        jcommander(cfg.commandLineArguments(), args, "<gemfire-driver>");
        configureGFClient(args, cfg);
        
        println("Gemfire client configured. cache=" + gemCache.getName() + " region=" + testRegion.getFullPath());
        println(cfg, "Gemfire benchmark config: [" + cfg + "].");
    }

    /**
     * Configure Gemfire map.
     *
     * @param args Arguments.
     */
    private void configureGFClient(GemfireBenchmarkArguments args , BenchmarkConfiguration cfg) {
      if(args.clientMode()){
        gemCache = new ClientCacheFactory()
        .set("name", "GemfireClient")
        .set("cache-xml-file", args.clientConfiguration())
        .setPoolSubscriptionEnabled(true)        
        .set("log-level", "info")
        .create();
      }else{
        gemCache = new CacheFactory()
        .set("mcast-port", "10111")
        .set("log-level", "info")
        .set("cache-xml-file", args.configuration())
        .create();
      } 
      testRegion = gemCache.getRegion("testRegion");
    }

    /** {@inheritDoc} */
    @Override public void tearDown() throws Exception {
      gemCache.close();
    }

    /** {@inheritDoc} */
    @Override public String description() {
        String desc = BenchmarkUtils.description(cfg, this);

        return desc.isEmpty() ?
            getClass().getSimpleName() + args.description() + cfg.defaultDescription() : desc;
    }

    /** {@inheritDoc} */
    @Override public String usage() {
        return BenchmarkUtils.usage(args);
    }

    /**
     * @param max Key range.
     * @return Next key.
     */
    protected int nextRandom(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    /**
     * @param min Minimum key in range.
     * @param max Maximum key in range.
     * @return Next key.
     */
    protected int nextRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max - min) + min;
    }
}
