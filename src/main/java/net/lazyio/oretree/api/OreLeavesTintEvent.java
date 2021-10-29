package net.lazyio.oretree.api;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.IModBusEvent;

/**
 * Event to register the {@link net.lazyio.oretree.block.OreLeavesBlock} color to
 * BlockColors and ItemColors.
 *
 * This event is called on the MOD event bus and is CLIENT side.
 * 
 * Example: {@link net.lazyio.oretree.event.OnOreLeavesTintEvent#onOreLeavesTint(OreLeavesTintEvent)}
 */
public class OreLeavesTintEvent extends Event implements IModBusEvent {

    public OreLeavesTintEvent() {
        super();
    }
}
