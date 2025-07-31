package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class MiscInstructions extends AbstractInstruction implements InstructionSet {

    public MiscInstructions(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        functions.put(0x00, wrap(() -> {
            registers.incrementPC();
        }, 4)); // NOP

        functions.put(0x10, wrap(() -> {
            registers.incrementPC();
            registers.incrementPC();
            cpu.setStopped(true);
        }, 4)); // STOP 0

        functions.put(0xF3, wrap(() -> {
            cpu.setStopped(false);
            registers.incrementPC();
        }, 4)); // DI - Disable Interrupts

        functions.put(0xE3, wrap(() -> {
            // undocumented, so is treated as a NOP
            registers.incrementPC();
        }, 4));

        functions.put(0xF4, wrap(() -> {
            // undocumented, so is treated as a NOP
            registers.incrementPC();
        }, 4));

        functions.put(0xFC, wrap(() -> {
            // undocumented, so is treated as a NOP
            registers.incrementPC();
        }, 4));

        functions.put(0xFD, wrap(() -> {
            // undocumented, so is treated as a NOP
            registers.incrementPC();
        }, 4));
    }
}
