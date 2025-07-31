package gb_emu.core.cpu.instructions;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public abstract class AbstractInstruction {
    protected CPU cpu;
    protected CPURegisters registers;
    protected MMU mmu;

    public AbstractInstruction(CPU cpu, CPURegisters registers, MMU mmu) {
        this.cpu = cpu;
        this.registers = registers;
        this.mmu = mmu;
    }

    protected Instruction wrap(Runnable r, int cycles) {
        return () -> {
            r.run();
            return cycles;
        };
    }

    protected int readImmediate8() {
        int value = mmu.read(registers.getPC());
        registers.incrementPC();
        return value;
    }

    protected int readImmediate16() {
        int low = mmu.read(registers.getPC());
        registers.incrementPC();
        int high = mmu.read(registers.getPC());
        registers.incrementPC();
        return (high << 8) | low;
    }
}
