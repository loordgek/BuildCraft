package buildcraft.lib.path.task;

import java.util.concurrent.Callable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskMiniChunkFiller implements Callable<FilledChunk> {
    private final World world;
    private final BlockPos offset;

    public TaskMiniChunkFiller(World world, BlockPos min) {
        this.world = world;
        this.offset = min;
    }

    @Override
    public FilledChunk call() throws Exception {
        FilledChunk filled = new FilledChunk();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos pos = offset.add(x, y, z);
                    EnumTraversalExpense expense = EnumTraversalExpense.getFor(world, pos, world.getBlockState(pos));
                    filled.expenses[x][y][z] = expense;
                    filled.expenseCounts[expense.ordinal()]++;
                }
            }
        }
        return filled;
    }
}
