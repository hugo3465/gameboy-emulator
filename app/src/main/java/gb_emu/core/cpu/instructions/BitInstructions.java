package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.MMU;

public class BitInstructions extends AbstractInstruction implements InstructionSet {

    public BitInstructions(Registers registers, MMU mmu) {
        super(registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
    }
}
