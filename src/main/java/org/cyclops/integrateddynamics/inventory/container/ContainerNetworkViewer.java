package org.cyclops.integrateddynamics.inventory.container;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.container.ScrollingInventoryContainer;
import org.cyclops.cyclopscore.inventory.slot.SlotExtended;
import org.cyclops.cyclopscore.inventory.slot.SlotSingleItem;
import org.cyclops.cyclopscore.persist.IDirtyMarkListener;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.item.IVariableFacade;
import org.cyclops.integrateddynamics.api.item.IVariableFacadeHandlerRegistry;
import org.cyclops.integrateddynamics.api.network.INetwork;
import org.cyclops.integrateddynamics.client.gui.GuiNetworkViewer;
import org.cyclops.integrateddynamics.client.gui.NetworkElementGui;
import org.cyclops.integrateddynamics.client.gui.NetworkElementItemGui;
import org.cyclops.integrateddynamics.client.gui.NetworkElementPartGui;
import org.cyclops.integrateddynamics.core.persist.world.LabelsWorldStorage;
import org.cyclops.integrateddynamics.core.persist.world.NetworkWorldStorage;
import org.cyclops.integrateddynamics.item.ItemNetworkViewer;
import org.cyclops.integrateddynamics.item.ItemVariable;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Container for the {@link ItemNetworkViewer}.
 */
public class ContainerNetworkViewer extends ScrollingInventoryContainer<NetworkElementGui> implements IDirtyMarkListener {

    public static final int OUTPUT_X = 232;
    public static final int OUTPUT_Y = 110;

    protected static final IItemPredicate<NetworkElementGui> FILTERER = new IItemPredicate<NetworkElementGui>(){

        @Override
        public boolean apply(NetworkElementGui item, Pattern pattern) {
        	return true;
            //return pattern.matcher(item.getMatchString()).matches() || pattern.matcher(item.getSymbol()).matches();
        }
    };

    private final int itemIndex;

    private final SimpleInventory writeSlot;
    private final SimpleInventory filterSlots;
    private NetworkElementGui activeElement = null;
    private NetworkElementGui temporarySlotsElement = null;
    private SimpleInventory temporaryInputSlots = null;
    private L10NHelpers.UnlocalizedString lastError;
    private LoadConfigListener loadConfigListener;

    private IValueType filterIn1 = null;
    private IValueType filterIn2 = null;
    private IValueType filterOut = null;

    @SideOnly(Side.CLIENT)
    private GuiNetworkViewer gui;

    private String lastLabel = "";

    public ContainerNetworkViewer(EntityPlayer player, int itemIndex) {
        this(player.inventory, ItemNetworkViewer.getInstance(), itemIndex, player.isSneaking());
    }

    public ContainerNetworkViewer(InventoryPlayer inventory, IGuiContainerProvider guiProvider, int itemIndex,
    		boolean isSneaking) {
        super(inventory, guiProvider, getElements(isSneaking), FILTERER);
        this.itemIndex = itemIndex;
        this.writeSlot = new SimpleInventory(1, "writeSlot", 1);
        this.filterSlots = new SimpleInventory(3, "filterSlots", 1);
        this.filterSlots.addDirtyMarkListener(new FilterSlotListener());
        this.writeSlot.addDirtyMarkListener(this);
        this.writeSlot.addDirtyMarkListener(loadConfigListener = new LoadConfigListener());
        this.temporaryInputSlots = new SimpleInventory(0, "temporaryInput", 1);
        initializeSlotsPre();
        initializeSlotsPost();
    }

    protected static List<NetworkElementGui> getElements(boolean isSneaking) {
        INetwork network =
        		NetworkWorldStorage.getInstance(IntegratedDynamics._instance).getNetworks().stream().findFirst().orElse(null);
        List<NetworkElementGui> elements = Lists.newLinkedList();
        if (network != null) {
        	elements.addAll(
        			(isSneaking ? getItems(network) : getParts(network))
        			.collect(Collectors.toList()));
        }
        return elements;
    }

    private static Stream<NetworkElementGui> getParts(INetwork network){
    	return network.getElements().stream()
    			.map(networkElement->new NetworkElementPartGui(networkElement));
    }

    private static Stream<NetworkElementGui> getItems(INetwork network){
    	return network.getElements().stream()
    		.flatMap(networkElement->NetworkElementItemGui.getItemStacks(networkElement)
    				.map(is->new NetworkElementItemGui(networkElement, is)));
			//.map(networkElement->new NetworkElementItemGui(networkElement));
			//.filter(gui->gui.getItemStack().isPresent())
    }

    public ItemStack getItemStack(EntityPlayer player) {
        return InventoryHelpers.getItemFromIndex(player, itemIndex);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        ItemStack item = getItemStack(player);
        return item != null && item.getItem() == ItemNetworkViewer.getInstance();
    }

    @SideOnly(Side.CLIENT)
    public void setGui(GuiNetworkViewer gui) {
        this.gui = gui;
    }

    @SideOnly(Side.CLIENT)
    public GuiNetworkViewer getGui() {
        return this.gui;
    }

    protected void initializeSlotsPre() {
        addSlotToContainer(new SlotSingleItem(writeSlot, 0, OUTPUT_X, OUTPUT_Y, ItemVariable.getInstance()));
        SlotSingleItem filterSlotIn1 = new SlotSingleItem(filterSlots, 0, 6, 218, ItemVariable.getInstance());
        SlotSingleItem filterSlotIn2 = new SlotSingleItem(filterSlots, 1, 24, 218, ItemVariable.getInstance());
        SlotSingleItem filterSlotOut = new SlotSingleItem(filterSlots, 2, 58, 218, ItemVariable.getInstance());
        filterSlotIn1.setPhantom(true);
        filterSlotIn2.setPhantom(true);
        filterSlotOut.setPhantom(true);
        addSlotToContainer(filterSlotIn1);
        addSlotToContainer(filterSlotIn2);
        addSlotToContainer(filterSlotOut);
    }

    protected void initializeSlotsPost() {
        addPlayerInventory((InventoryPlayer) getPlayerIInventory(), 88, 131);
    }

    @Override
    public int getPageSize() {
        return 10;
    }

    @Override
    protected int getSizeInventory() {
        return 1;
    }

    public void setActiveElementById(String typeId, String elementId) {
    	//TODO TCD Not implemented
        /*ILogicProgrammerElementType type = LogicProgrammerElementTypes.REGISTRY.getType(typeId);
        if (type != null) {
            ILogicProgrammerElement element = type.getByName(elementId);
            if(!LogicProgrammerElementTypes.areEqual(getActiveElement(), element)) {
                setActiveElement(element, 0, 0);
                onDirty();
            }
        } else {
            setActiveElement(null, 0, 0);
        }*/
    }

    /**
     * Set the new active element.
     * @param activeElement The new element.
     * @param baseX The slots X coordinate
     * @param baseY The slots Y coordinate
     */
    public void setActiveElement(final NetworkElementGui activeElement, int baseX, int baseY) {
        if(this.activeElement != null) {
        	this.activeElement.deactivate();
        }
        this.activeElement = activeElement;

        this.lastError = null;
        this.activeElement = activeElement;

        this.setElementInventory(this.activeElement, baseX, baseY);

        if(activeElement != null) {
        	activeElement.activate();
        }
    }

    /**
     * Set the new active element.
     * @param element The new element.
     * @param baseX The slots X coordinate
     * @param baseY The slots Y coordinate
     */
    public void setElementInventory(final NetworkElementGui element, int baseX, int baseY) {
        this.lastError = null;

        // This assumes that there is only one other slot, the remaining slots will be erased!
        // (We can do this because they are all ghost slots)
        inventoryItemStacks = NonNullList.create();
        inventorySlots = Lists.newArrayList();
        initializeSlotsPre();
        this.temporaryInputSlots.removeDirtyMarkListener(this);
        this.temporaryInputSlots = new SimpleInventory(element == null ? 0 : element.getRenderPattern().getSlotPositions().length, "temporaryInput", 1);
        temporaryInputSlots.addDirtyMarkListener(this);
        this.temporarySlotsElement = element;
        if(element != null) {
            Pair<Integer, Integer>[] slotPositions = element.getRenderPattern().getSlotPositions();
            for (int i = 0; i < temporaryInputSlots.getSizeInventory(); i++) {
                final int slotId = i;
                SlotExtended slot = new SlotExtended(temporaryInputSlots, i, 1 + baseX + slotPositions[i].getLeft(),
                        1 + baseY + slotPositions[i].getRight()) {
                    @Override
                    public boolean isItemValid(ItemStack itemStack) {
                        return element.isItemValidForSlot(slotId, itemStack);
                    }
                };
                slot.setPhantom(true);
                addSlotToContainer(slot);
            }
        }
        initializeSlotsPost();
        this.lastLabel = "";
    }

    public boolean canWriteActiveElementPre() {
        if(activeElement != null) {
            return activeElement.canWriteElementPre();
        }
        return false;
    }

    public boolean canWriteActiveElement() {
        if(!canWriteActiveElementPre()) {
            return false;
        }
        lastError = activeElement.validate();
        return lastError == null;
    }

    public NetworkElementGui getActiveElement() {
        return activeElement;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.world.isRemote) {
            ItemStack itemStack = writeSlot.getStackInSlot(0);
            if(!itemStack.isEmpty()) {
                player.dropItem(itemStack, false);
            }
        }
    }

    public void onLabelPacket(String label) {
        this.lastLabel = label;
        labelCurrent();
    }

    protected void labelCurrent() {
        ItemStack itemStack = writeSlot.getStackInSlot(0);
        if(!itemStack.isEmpty()) {
            IVariableFacade variableFacade = ItemVariable.getInstance().getVariableFacade(itemStack);
            if(variableFacade.isValid()) {
                LabelsWorldStorage.getInstance(IntegratedDynamics._instance).put(variableFacade.getId(), this.lastLabel);
            }
        }
    }

    protected ItemStack writeElementInfo() {
        ItemStack itemStack = writeSlot.getStackInSlot(0);
        ItemStack result = getActiveElement().writeElement(player, itemStack.copy());
        return result;
    }

    public ItemStack getWriteItemStack() {
    	return writeSlot.getStackInSlot(0);
    }

    @Override
    public void onDirty() {
    	NetworkElementGui activeElement = getActiveElement();
        if(activeElement != null) {
            for (int i = 0; i < temporaryInputSlots.getSizeInventory(); i++) {
                ItemStack itemStack = temporaryInputSlots.getStackInSlot(i);
                temporarySlotsElement.onInputSlotUpdated(i, itemStack);
            }
        }

        ItemStack itemStack = writeSlot.getStackInSlot(0);
        if(canWriteActiveElement() && !itemStack.isEmpty()) {
            ItemStack outputStack = writeElementInfo();
            writeSlot.removeDirtyMarkListener(this);
            writeSlot.setInventorySlotContents(0, outputStack);
            if(!StringUtils.isNullOrEmpty(this.lastLabel)) {
                labelCurrent();
            }
            writeSlot.addDirtyMarkListener(this);
        }
    }

    /*protected void loadConfigFrom(ItemStack itemStack) {
        // Only do this client-side, a packet will be sent to do the same server-side.
        if(MinecraftHelpers.isClientSide()) {
            IVariableFacadeHandlerRegistry registry = IntegratedDynamics._instance.getRegistryManager().getRegistry(IVariableFacadeHandlerRegistry.class);
            IVariableFacade variableFacade = registry.handle(itemStack);
            for(NetworkElementGui element : getElements()) {
                if(element.isFor(variableFacade)) {
                    getGui().handleElementActivation(element);
                }
            }
        }
    }*/

    public L10NHelpers.UnlocalizedString getLastError() {
        return this.lastError;
    }

    public IInventory getTemporaryInputSlots() {
        return this.temporaryInputSlots;
    }

    public boolean hasWriteItemInSlot() {
        return !this.writeSlot.getStackInSlot(0).isEmpty();
    }

    @Override
    protected boolean additionalApplies(NetworkElementGui item) {
    	return true;
        /*return (
                ((filterIn1 == null || item.matchesInput(filterIn1)) && (filterIn2 == null || item.matchesInput(filterIn2))) || (filterIn1 == null && filterIn2 == null))
                && (filterOut == null || item.matchesOutput(filterOut));*/
    }

    @Override
    public ItemStack slotClick(int slotId, int arg, ClickType clickType, EntityPlayer player) {
        // Handle cases where the client may have more (phantom) slots than the server.
        if (slotId >= this.inventorySlots.size()) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, arg, clickType, player);
    }

    /**
     * Load existing operator data when a variable card is inserted into the write slot
     */
    protected class LoadConfigListener implements IDirtyMarkListener {

        @Override
        public void onDirty() {
            // Currently disabled, this requires quite complex negotiation between C and S, not too mention
            // any other players having the gui open!
            /*if ((temporaryInputSlots == null || temporaryInputSlots.isEmpty())
                    && (activeElement == null || activeElement.canCurrentlyReadFromOtherItem())) {
                ItemStack itemStack = writeSlot.getStackInSlot(0);
                if (!itemStack.isEmpty()) {
                    ContainerLogicProgrammer.this.loadConfigFrom(itemStack);
                }
            }*/
        }

    }

    /**
     * Filter LP elements based on the filter value types.
     */
    protected class FilterSlotListener implements IDirtyMarkListener {

        protected IValueType getValueType(IInventory inventory, int slot) {
            IVariableFacadeHandlerRegistry handler = IntegratedDynamics._instance.getRegistryManager().getRegistry(IVariableFacadeHandlerRegistry.class);
            if(inventory.getStackInSlot(slot) != null) {
                IVariableFacade variableFacade = handler.handle(inventory.getStackInSlot(slot));
                if(variableFacade.isValid()) {
                    return variableFacade.getOutputType();
                }
            }
            return null;
        }

        @Override
        public void onDirty() {
            IValueType filterIn1Prev = filterIn1;
            IValueType filterIn2Prev = filterIn2;
            IValueType filterOutPrev = filterOut;

            filterIn1 = getValueType(filterSlots, 0);
            filterIn2 = getValueType(filterSlots, 1);
            filterOut = getValueType(filterSlots, 2);
            if (filterIn1Prev != filterIn1 || filterIn2Prev != filterIn2 || filterOutPrev != filterOut) {
                refreshFilter();
            }
        }

    }

}
