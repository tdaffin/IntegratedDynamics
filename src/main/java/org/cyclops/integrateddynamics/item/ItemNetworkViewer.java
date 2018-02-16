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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.stream.Stream;

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
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                  float hitX, float hitY, float hitZ, EnumHand hand) {
        if(!world.isRemote) {
        	INetwork network = NetworkHelpers.getNetwork(world, pos, side);
        	if ( network != null ) {
        		currentNetwork = network;
        		player.playSound(
        				SoundEvents.ENTITY_FIREWORK_LARGE_BLAST,
        				//SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
        				1f, 1f);
        				//playerIn.getSoundVolume(), playerIn.getSoundPitch());
        				//1f, 0.9f);*/
        		player.swingArm(hand);
        	    //world.playSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F),
        	    //		SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1f, 1F, false);

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
        		// mc.displayGuiScreen(new GuiExample());
        		;
        		//player.inventory.getCurrentItem();
        		Minecraft mc = Minecraft.getMinecraft();
        		if ( mc != null )
        			mc.addScheduledTask(()->mc.displayGuiScreen(new GuiNetworkViewer(player, player.inventory.currentItem)));

        	    /*public GuiNetworkViewer(EntityPlayer player, int itemIndex) {
        	        this(player.inventory, new ContainerNetworkViewer(player, itemIndex));
        	    }

        	    public GuiNetworkViewer(InventoryPlayer inventoryPlayer, ContainerNetworkViewer container) {
        	        super(container);*/
        		//openGuiForItemIndex(world, player, player.inventory.currentItem, hand);
        		return EnumActionResult.SUCCESS;
        	}
        }
        /*if(!world.isRemote || player.isSneaking()) {
            return EnumActionResult.PASS;
        } else if(block.rotateBlock(world, pos, side)) {
            player.swingArm(hand);
            return EnumActionResult.SUCCESS;
        }*/
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World world, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(playerIn, world, pos, hand, facing, hitX, hitY, hitZ);
    }

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		return super.onItemRightClick(world, player, hand);
	}

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    @Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    @Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
    	super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
    }

    private static Stream<INetwork> getNetworks(){
        return NetworkWorldStorage.getInstance(IntegratedDynamics._instance).getNetworks().stream();
    }

    public INetwork getCurrentNetwork() {
    	if ( currentNetwork == null ) {
			//currentNetwork = getNetworks().findFirst().orElse(null);
    	} else {
            if ( !getNetworks().anyMatch(network->network==currentNetwork) )
                currentNetwork = null;
        }
    	return currentNetwork;
    }

}
