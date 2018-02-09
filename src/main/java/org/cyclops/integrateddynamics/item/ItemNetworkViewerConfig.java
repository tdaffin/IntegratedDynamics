package org.cyclops.integrateddynamics.item;

import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.integrateddynamics.IntegratedDynamics;

/**
 * Config for the network viewer
 */
public class ItemNetworkViewerConfig extends ItemConfig {

    /**
     * The unique instance.
     */
    public static ItemNetworkViewerConfig _instance;

    /**
     * Make a new instance.
     */
    public ItemNetworkViewerConfig() {
        super(
                IntegratedDynamics._instance,
                true,
                "network_viewer",
                null,
                ItemNetworkViewer.class
        );
    }

}