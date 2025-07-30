package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class MiscInstructions extends AbstractInstruction implements InstructionSet {

    public MiscInstructions(CPURegisters registers, MMU mmu) {
        super(registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        functions.put(0x00, () -> {
            registers.incrementPC();
        }); // NOP
    }
}
