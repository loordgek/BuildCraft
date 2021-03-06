package buildcraft.transport.pipe.behaviour;

import net.minecraft.nbt.NBTTagCompound;

import buildcraft.api.transport.PipeEventHandler;
import buildcraft.api.transport.PipeEventItem;
import buildcraft.api.transport.neptune.IPipe;
import buildcraft.api.transport.neptune.PipeBehaviour;

public class PipeBehaviourGold extends PipeBehaviour {
    private static final double SPEED_DELTA = 0.07;
    private static final double SPEED_TARGET = 0.2;

    public PipeBehaviourGold(IPipe pipe) {
        super(pipe);
    }

    public PipeBehaviourGold(IPipe pipe, NBTTagCompound nbt) {
        super(pipe, nbt);
    }

    @PipeEventHandler
    public static void modifySpeed(PipeEventItem.ModifySpeed event) {
        event.modifyTo(SPEED_TARGET, SPEED_DELTA);
    }
}
