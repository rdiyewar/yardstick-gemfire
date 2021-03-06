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

import com.beust.jcommander.*;

/**
 * Input arguments for gemfire benchmarks.
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
public class GemfireBenchmarkArguments {
    /** */
    @Parameter(names = {"-id", "--Member ID"}, description = "Member ID")
    private int id = 0;  
  
    /** */
    @Parameter(names = {"-nn", "--nodeNumber"}, description = "Node number")
    private int nodes = 1;

    /** */
    @Parameter(names = {"-b", "--backups"}, description = "Backups")
    private int backups;

    @Parameter(names = {"-gfcfg", "--gfConfig"}, description = "Configuration file")
    private String gfCfg = "config/gemfire-config.xml";

    @Parameter(names = {"-gfclientcfg", "--gfClientConfig"}, description = "Client configuration file")
    private String gfClientCfg = "config/gemfire-client-config.xml";

    /** */
    @Parameter(names = {"-cm", "--clientMode"}, description = "Client mode")
    private boolean clientMode = false;

    /** */
    @Parameter(names = {"-r", "--range"}, description = "Key range")
    private int range = 1_000_000;

    /**
     * @return Client mode.
     */
    public boolean clientMode() {
        return clientMode;
    }

    /**
     * @return Backups.
     */
    public int backups() {
        return backups;
    }

    /**
     * @return memberId
     */
    public int memberId(){
      return id;
    }
    
    /**
     * @return Nodes.
     */
    public int nodes() {
        return nodes;
    }

    /**
     * @return Key range, from {@code 0} to this number.
     */
    public int range() {
        return range;
    }

    /**
     * @return Configuration file.
     */
    public String configuration() {
        return gfCfg;
    }

    /**
     * @return Configuration file.
     */
    public String clientConfiguration() {
        return gfClientCfg;
    }

    /**
     * @return Description.
     */
    public String description() {
        return "-nn=" + nodes + "-b=" + backups + "-cm=" + clientMode;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return getClass().getSimpleName() + " [" +
            "id=" + id +
            ", nodes=" + nodes +
            ", backups=" + backups +
            ", gfConfig='" + gfCfg + '\'' +
            ", gfClientCfg='" + gfClientCfg + '\'' +
            ", clientMode=" + clientMode +
            ", range=" + range +
            ']';
    }
}
