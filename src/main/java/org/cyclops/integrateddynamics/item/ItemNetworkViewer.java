package org.cyclops.integrateddynamics.item;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.integrateddynamics.client.gui.GuiNetworkViewer;
import org.cyclops.integrateddynamics.inventory.container.ContainerNetworkViewer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A network viewer item
 */
public class ItemNetworkViewer extends ItemGui {

    private static ItemNetworkViewer _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ItemNetworkViewer getInstance() {
        return _instance;
    }

    /**
     * Make a new item instance.
     *
     * @param eConfig Config for this blockState.
     */
    public ItemNetworkViewer(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerNetworkViewer.class;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Class<? extends GuiScreen> getGui() {
        return GuiNetworkViewer.class;
    }
}
