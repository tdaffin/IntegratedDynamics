package org.cyclops.integrateddynamics.client.gui;

import java.util.List;

import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.network.INetworkElement;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.network.PartNetworkElement;
import org.cyclops.integrateddynamics.core.network.TileNetworkElement;
import org.cyclops.integrateddynamics.core.part.PartStateActiveVariableBase;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class NetworkElementPartGui extends NetworkElementGui {

	public NetworkElementPartGui(INetworkElement element) {
		super(element);
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
	public int getColor() {
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
		}
		return Helpers.RGBToInt(255, 255, 255);
		//Triple<Float, Float, Float> rgb = Triple.of(1f, 1f, 1f);
	}

	@Override
	public String getSymbol() {
		INetworkElement element = getElement();
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

}
