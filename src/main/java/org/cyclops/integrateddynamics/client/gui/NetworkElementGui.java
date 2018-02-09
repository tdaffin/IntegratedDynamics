package org.cyclops.integrateddynamics.client.gui;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers.UnlocalizedString;
import org.cyclops.integrateddynamics.api.client.gui.subgui.IGuiInputElement;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.logicprogrammer.IConfigRenderPattern;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.evaluate.variable.gui.GuiElementValueTypeStringRenderPattern;
import org.cyclops.integrateddynamics.core.logicprogrammer.RenderPattern;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeLPElementRenderPattern;
import org.cyclops.integrateddynamics.core.network.PartNetworkElement;
import org.cyclops.integrateddynamics.core.network.TileNetworkElement;
import org.cyclops.integrateddynamics.core.part.PartStateActiveVariableBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerNetworkViewer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//public interface INetworkViewerElement<S extends ISubGuiBox, G extends Gui, C extends Container> extends IGuiInputElement<S, G, C> {
public class NetworkElementGui implements IGuiInputElement<RenderPattern, GuiNetworkViewer, ContainerNetworkViewer> {

	private final INetworkElement element;
	
	public NetworkElementGui(INetworkElement element){
		this.element = element;
	}
	
	/**
	 * Check if the given item can be inserted into the given slot.
	 * 
	 * @param slotId The slot id.
	 * @param itemStack The item that will be inserted.
	 * @return If it can be inserted.
	 */
	public boolean isItemValidForSlot(int slotId, ItemStack itemStack) {
		return false;
	}

	/**
	 * @return If this element can be written to an item in its current state.
	 */
	public boolean canWriteElementPre() {
		return false;
	}

	/**
	 * Called when an input item slot has been updated.
	 * 
	 * @param slotId The slot id.
	 * @param itemStack The itemstack currently in the slot, can be null.
	 */
	public void onInputSlotUpdated(int slotId, ItemStack itemStack) {

	}

	/**
	 * The stack to write the current state of this element to.
	 * 
	 * @param player The player that is writing the element.
	 * @param itemStack The stack to write to.
	 * @return The resulting itemstack.
	 */
	public ItemStack writeElement(EntityPlayer player, ItemStack itemStack) {
		return itemStack;
	}

	/**
	 * @param variableFacade A variable facade
	 * @return If this element corresponds to the given variable facade.
	 */
	public boolean isFor(IVariableFacade variableFacade) {
		return false;
	}
	
    /**
     * @param subGui The corresponding sub gui of this element.
     * @return If this element has the active focus. For typing and things like that.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFocused(RenderPattern subGui) {
		// TODO Auto-generated method stub
		return false;
    }

    /**
     * Set the focus of this element.
     * @param subGui The corresponding sub gui of this element.
     * @param focused If it must be focused.
     */
    @SideOnly(Side.CLIENT)
    public void setFocused(RenderPattern subGui, boolean focused) {
		// TODO Auto-generated method stub
    }
    
	@Override
	public String getLocalizedNameFull() {
		return getSymbol();
	}
	
	@Override
	public void loadTooltip(List<String> lines) {
		lines.add(getSymbol());
		
		if ( element instanceof PartNetworkElement ) {
			PartNetworkElement partNetworkElement = (PartNetworkElement)element;
			IPartType part = partNetworkElement.getPart();
			IPartState partState = partNetworkElement.getPartState();
            part.loadTooltip(partState, lines);
			return;
		} else if ( element instanceof TileNetworkElement ) {
			TileNetworkElement tileNetworkElement = (TileNetworkElement)element;
			DimPos position = tileNetworkElement.getPosition();
			World world = position.getWorld();
			if ( world != null ) {
				IBlockState blockState = world.getBlockState(position.getBlockPos());
				//lines.add(blockState.getBlock().getLocalizedName());
				
			}
		}
		
		int maxlen = 45;
		String str = element.toString();
		while (str.length() > maxlen) {
			String start = str.substring(0, maxlen);
			lines.add(start);
			str = str.substring(maxlen);
		}
		lines.add(str);
	}

	@Override
	public IConfigRenderPattern getRenderPattern() {
		return org.cyclops.integrateddynamics.api.logicprogrammer.IConfigRenderPattern.NONE;
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UnlocalizedString validate() {
		return null;
	}

	@Override
	public int getColor() {
		if ( element instanceof PartNetworkElement ) {
			PartNetworkElement partNetworkElement = (PartNetworkElement)element;
			IPartType part = partNetworkElement.getPart();
			IPartState partState = partNetworkElement.getPartState();
			if ( partState instanceof PartStateActiveVariableBase ) {
				PartStateActiveVariableBase state = (PartStateActiveVariableBase)partState;
				if (!state.getInventory().isEmpty()) {
		            if (!state.hasVariable() || !state.isEnabled()) {
		            	return Helpers.RGBToInt(250, 10, 13);//, TextFormatting.RED.toString());
		            }
				}
			}
		}
		return Helpers.RGBToInt(255, 255, 255);
		//Triple<Float, Float, Float> rgb = Triple.of(1f, 1f, 1f); 
	}

	@Override
	public String getSymbol() {		
		if ( element instanceof PartNetworkElement ) {
			PartNetworkElement partNetworkElement = (PartNetworkElement)element;
			IPartType part = partNetworkElement.getPart();
			return L10NHelpers.localize(part.getUnlocalizedName());
		} else if ( element instanceof TileNetworkElement ) {
			TileNetworkElement tileNetworkElement = (TileNetworkElement)element;
			DimPos position = tileNetworkElement.getPosition();
			World world = position.getWorld();
			if ( world != null ) {
				IBlockState blockState = world.getBlockState(position.getBlockPos());
				return blockState.getBlock().getLocalizedName();
			}
		}
		return element.getClass().getSimpleName();
	}

	@Override
	public RenderPattern createSubGui(int baseX, int baseY, int maxWidth, int maxHeight, GuiNetworkViewer gui,
			ContainerNetworkViewer container) {
		// TODO Auto-generated method stub
		return new RenderPattern(this, maxHeight, maxHeight, maxHeight, maxHeight, gui, container);
		//return new ValueTypeLPElementRenderPattern(this, baseX, baseY, maxWidth, maxHeight, gui, container);
		//return new GuiElementValueTypeStringRenderPattern<GuiElementValueTypeStringRenderPattern, GuiNetworkViewer, ContainerNetworkViewer>(this, baseX, baseY, maxWidth, maxHeight, gui, container);
	}
	
}

