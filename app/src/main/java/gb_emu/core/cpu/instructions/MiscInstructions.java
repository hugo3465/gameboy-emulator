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
            // Noting
        }, 4)); // NOP

        functions.put(0x10, wrap(() -> {
            registers.incrementPC();
            cpu.setStopped(true);
        }, 4)); // STOP 0

        functions.put(0xFB, wrap(() -> {
            cpu.setEnableInterruptsNextInstruction(true);
        }, 4));

        functions.put(0x2F, wrap(() -> { // CPL
            int a = registers.getA();
            a = a ^ 0xFF;
            registers.setA(a);

            registers.setFlagN(true);
            registers.setFlagH(true);
        }, 4));

        functions.put(0xDD, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xF3, wrap(() -> {
            cpu.setStopped(false);
        }, 4)); // DI - Disable Interrupts

        functions.put(0xE3, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xF4, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xFC, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xFD, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xED, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));

        functions.put(0xEC, wrap(() -> {
            // undocumented, so is treated as a NOP
        }, 4));
    }
}
