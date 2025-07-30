package gb_emu.core.cpu;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.GameBoy;
import gb_emu.core.cpu.instructions.InstructionsMap;
import gb_emu.core.mem.MMU;

public class CPU implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CPU.class);

    private GameBoy gb;
    private CPURegisters registers;
    private MMU mmu;
    private InstructionsMap instructionsMap;

    public CPU(GameBoy gb, MMU mmu) {
        this.gb = gb;
        this.mmu = mmu;

        this.registers = new CPURegisters();
        this.instructionsMap = new InstructionsMap(mmu, registers);
    }

    public int step() {
        int pcBefore = registers.getPC();
        int opcode = mmu.read(pcBefore);

        LOGGER.debug("Opcode: " + String.format("0x%02X", opcode));
        LOGGER.debug("PC: " + pcBefore);

        int cycles = instructionsMap.execute(opcode);

        // If PC hans't change, increment it to avoid crashes
        int pcAfter = registers.getPC();
        if (pcBefore == pcAfter) {
            registers.incrementPC();
        }

        return cycles;
    }
}
