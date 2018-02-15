package org.cyclops.integrateddynamics.client.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.network.PartNetworkElement;
import org.cyclops.integrateddynamics.core.network.TileNetworkElement;
import org.cyclops.integrateddynamics.core.part.PartStateActiveVariableBase;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class NetworkElementItemGui extends NetworkElementGui {

	private ItemStack itemStack;

	public NetworkElementItemGui(INetworkElement element, ItemStack itemStack) {
		super(element);
		this.itemStack = itemStack;
	}

	@Override
	public void loadTooltip(List<String> lines) {
		getItemStack().ifPresent(itemStack->{
			lines.addAll(itemStack.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL));
		});
		if ( hasError() )
		    super.loadTooltip(lines);
	}

	@Override
	public int getColor() {
		return super.getColor();
		//return getItemStack().map(itemStack->Helpers.RGBToInt(255, 255, 255))
		//		.orElse(Helpers.RGBToInt(250, 10, 13));//, TextFormatting.RED.toString()););

		/*return getItemStack().map(itemStack->L10NHelpers.localize(itemStack.getUnlocalizedName()))
				.orElse(Helpers.RGBToInt(255, 255, 255));
		getItemStack().ifPresent(itemStack->{
			lines.addAll(itemStack.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL));
		});
		INetworkElement element = getElement();

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
		}*/

		//Triple<Float, Float, Float> rgb = Triple.of(1f, 1f, 1f);
	}

	@Override
	public String getSymbol() {
		return getItemStack().map(itemStack->itemStack.getDisplayName()
				/*{
			String unlocalizedName = itemStack.getItem().getUnlocalizedName(itemStack);
			//itemStack.getUnlocalizedName()
			return L10NHelpers.localize(unlocalizedName);
		}*/).orElse("<no item>");
	}

	Optional<ItemStack> getItemStack() {
		return Optional.of(itemStack);
		/*List<ItemStack> itemStacks = Lists.newLinkedList();
		//element.addDrops(itemStacks, false, false);

		INetworkElement element = getElement();

		getItemStacks(element).forEach(is->itemStacks.add(is));;

		if ( itemStacks.size() > 1 ) {
			System.err.format("more than one itemstack %d\n", itemStacks.size());
		}
		return itemStacks.stream().findFirst();*/
	}

}
