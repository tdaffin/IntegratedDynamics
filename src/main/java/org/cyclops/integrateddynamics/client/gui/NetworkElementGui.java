package org.cyclops.integrateddynamics.client.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers.UnlocalizedString;
import org.cyclops.integrateddynamics.api.client.gui.subgui.IGuiInputElement;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.logicprogrammer.IConfigRenderPattern;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.logicprogrammer.RenderPattern;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeItemStackLPElement;
import org.cyclops.integrateddynamics.core.network.PartNetworkElement;
import org.cyclops.integrateddynamics.core.network.TileNetworkElement;
import org.cyclops.integrateddynamics.core.part.PartStateActiveVariableBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamics.inventory.container.ContainerNetworkViewer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

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
		//return new RenderPattern(this, baseX, baseY, maxWidth, maxHeight, gui, container);
		return new SubGuiRenderPattern(this, baseX, baseY, maxWidth, maxHeight, gui, container);
		//return new ValueTypeLPElementRenderPattern(this, baseX, baseY, maxWidth, maxHeight, gui, container);
		//return new GuiElementValueTypeStringRenderPattern<GuiElementValueTypeStringRenderPattern, GuiNetworkViewer, ContainerNetworkViewer>(this, baseX, baseY, maxWidth, maxHeight, gui, container);
	}

	//NetworkElementGui implements IGuiInputElement<RenderPattern, GuiNetworkViewer, ContainerNetworkViewer> {
    @SideOnly(Side.CLIENT)
    protected class SubGuiRenderPattern extends RenderPattern<NetworkElementGui, GuiNetworkViewer, ContainerNetworkViewer> {

        public SubGuiRenderPattern(NetworkElementGui element, int baseX, int baseY, int maxWidth, int maxHeight,
                                   GuiNetworkViewer gui, ContainerNetworkViewer container) {
            super(element, baseX, baseY, maxWidth, maxHeight, gui, container);
        }

        @Override
        public void drawGuiContainerForegroundLayer(int guiLeft, int guiTop, TextureManager textureManager, FontRenderer fontRenderer, int mouseX, int mouseY) {
            super.drawGuiContainerForegroundLayer(guiLeft, guiTop, textureManager, fontRenderer, mouseX, mouseY);

            List<String> lines = new ArrayList<String>();
            loadTooltip(lines);

            if (!lines.isEmpty())
                gui.drawString(fontRenderer, lines.get(0), getX(), getY(), getColor());
            //gui.drawHoveringText(lines, getX(), getY());
            //gui.drawTooltip(lines, getX(), getY());
            /*IValueType valueType = element.getValueType();

            // Output type tooltip
            if(!container.hasWriteItemInSlot()) {
                if(gui.isPointInRegion(ContainerLogicProgrammerBase.OUTPUT_X, ContainerLogicProgrammerBase.OUTPUT_Y,
                        GuiLogicProgrammerBase.BOX_HEIGHT, GuiLogicProgrammerBase.BOX_HEIGHT, mouseX, mouseY)) {
                    gui.drawTooltip(getValueTypeTooltip(valueType), mouseX - guiLeft, mouseY - guiTop);
                }
            }*/

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            RenderHelper.disableStandardItemLighting();
            RenderHelper.enableGUIStandardItemLighting();


            INetworkElement element = getElement().getElement();
            //GuiNetworkViewer gui
            if (element instanceof PartNetworkElement) {
                PartNetworkElement partNetworkElement = (PartNetworkElement) element;
                IPartType part = partNetworkElement.getPart();
                IPartState partState = partNetworkElement.getPartState();
                ItemStack itemStack = part.getItemStack(partState, false);
                gui.drawItemStack(itemStack, getX(), getY(), null);//part.getName());
            } else if ( element instanceof TileNetworkElement) {
                TileNetworkElement tileNetworkElement = (TileNetworkElement)element;
                DimPos position = tileNetworkElement.getPosition();
                World world = position.getWorld();
                if ( world != null ) {
                    IBlockState blockState = world.getBlockState(position.getBlockPos());
                    //lines.add(blockState.getBlock().getLocalizedName());
                    gui.drawItemStack(new ItemStack(blockState.getBlock()), getX(), getY(), null);//part.getName());
                }
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }

    }

	boolean hasError(){
		INetworkElement element = getElement();
		if ( element instanceof PartNetworkElement) {
			PartNetworkElement partNetworkElement = (PartNetworkElement)element;
			//IPartType part = partNetworkElement.getPart();
			IPartState partState = partNetworkElement.getPartState();
			if ( partState instanceof PartStateActiveVariableBase) {
				PartStateActiveVariableBase state = (PartStateActiveVariableBase)partState;
				if (!state.getInventory().isEmpty()) {
					if (!state.hasVariable() || !state.isEnabled()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int getColor() {
		if ( hasError() )
			return Helpers.RGBToInt(250, 10, 13);//, TextFormatting.RED.toString());
		return Helpers.RGBToInt(255, 255, 255);
		//Triple<Float, Float, Float> rgb = Triple.of(1f, 1f, 1f);
	}

	@Override
	public void loadTooltip(List<String> lines) {
		lines.add(getSymbol());

		INetworkElement element = getElement();
		if ( element instanceof PartNetworkElement ) {
			PartNetworkElement partNetworkElement = (PartNetworkElement)element;
			IPartType part = partNetworkElement.getPart();
			IPartState partState = partNetworkElement.getPartState();
			part.loadTooltip(partState, lines);
			return;
		} else if ( element instanceof TileNetworkElement) {
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
}

