package org.cyclops.integrateddynamics.item;

import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.client.gui.GuiNetworkViewer;
import org.cyclops.integrateddynamics.core.helper.NetworkHelpers;
import org.cyclops.integrateddynamics.core.persist.world.NetworkWorldStorage;
import org.cyclops.integrateddynamics.inventory.container.ContainerNetworkViewer;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    private INetwork currentNetwork;

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

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World world, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //ItemStack itemStack = playerIn.getHeldItem(hand);
        if(!world.isRemote) {
        	INetwork network = NetworkHelpers.getNetwork(world, pos, facing);
        	if ( network != null ) {
        		currentNetwork = network;
        		/*playerIn.playSound(
        				SoundEvents.ENTITY_FIREWORK_LARGE_BLAST,
        				//SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
        				1f, 1f);
        				//playerIn.getSoundVolume(), playerIn.getSoundPitch());
        				//1f, 0.9f);*/

        	    world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F),
        	    		SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1f, 1F, false);

                /*if (entity instanceof EntityXPOrb)
                {
                    this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.35F + 0.9F);
                }
                else
                {
                    this.clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 1.4F + 2.0F);
                }
        		 (block.getSoundType().getVolume() + 1.0F) / 2.0F, block.getSoundType().getPitch() * 0.8F);*/
        		//ItemBlockCable.playPlaceSound(world, pos);
        		return EnumActionResult.SUCCESS;
        	}
        }
        return super.onItemUse(playerIn, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public INetwork getCurrentNetwork() {
    	if ( currentNetwork == null ) {
			currentNetwork = NetworkWorldStorage.getInstance(IntegratedDynamics._instance).getNetworks().stream()
					.findFirst().orElse(null);
    	}
    	return currentNetwork;
    }

}
