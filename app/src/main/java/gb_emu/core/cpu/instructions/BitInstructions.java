package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class BitInstructions extends AbstractInstruction implements InstructionSet {

    public BitInstructions(CPURegisters registers, MMU mmu) {
        super(registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
    }
}
