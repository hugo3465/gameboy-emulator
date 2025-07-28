package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;

public class ArithmeticInstructionsMap implements InstructionSet {
    private Registers registers;
    private RAM ram;
    private MMU mmu;

    public ArithmeticInstructionsMap(Registers registers, RAM ram, MMU mmu) {
        this.registers = registers;
        this.ram = ram;
        this.mmu = mmu;
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        // TODO to be implemented
    }
}
