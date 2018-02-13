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
