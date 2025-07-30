package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class ArithmeticInstructionsMap extends AbstractInstruction implements InstructionSet {

    public ArithmeticInstructionsMap(CPURegisters registers, MMU mmu) {
        super(registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        // TODO to be implemented
    }
}
