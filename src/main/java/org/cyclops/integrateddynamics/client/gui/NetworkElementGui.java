package org.cyclops.integrateddynamics.client.gui;

import org.cyclops.cyclopscore.helper.L10NHelpers.UnlocalizedString;
import org.cyclops.integrateddynamics.api.client.gui.subgui.IGuiInputElement;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.logicprogrammer.IConfigRenderPattern;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.core.logicprogrammer.RenderPattern;
import org.cyclops.integrateddynamics.inventory.container.ContainerNetworkViewer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//public interface INetworkViewerElement<S extends ISubGuiBox, G extends Gui, C extends Container> extends IGuiInputElement<S, G, C> {
public abstract class NetworkElementGui implements IGuiInputElement<RenderPattern, GuiNetworkViewer, ContainerNetworkViewer> {

	private final INetworkElement element;

	protected NetworkElementGui(INetworkElement element){
		this.element = element;
	}

	INetworkElement getElement() {
		return element;
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
	public RenderPattern createSubGui(int baseX, int baseY, int maxWidth, int maxHeight, GuiNetworkViewer gui,
			ContainerNetworkViewer container) {
		// TODO Auto-generated method stub
		return new RenderPattern(this, maxHeight, maxHeight, maxHeight, maxHeight, gui, container);
		//return new ValueTypeLPElementRenderPattern(this, baseX, baseY, maxWidth, maxHeight, gui, container);
		//return new GuiElementValueTypeStringRenderPattern<GuiElementValueTypeStringRenderPattern, GuiNetworkViewer, ContainerNetworkViewer>(this, baseX, baseY, maxWidth, maxHeight, gui, container);
	}

}

