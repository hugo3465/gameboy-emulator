package gb_emu.core.cpu;

import java.io.Serializable;

import gb_emu.core.GameBoy;
import gb_emu.core.mem.MMU;

public class CPU implements Serializable {
    private GameBoy gb;
    private Registers registers;
    private MMU mmu;
    private InstructionsMap instructionsMap;

    public CPU(GameBoy gb, MMU mmu) {
        this.gb = gb;
        this.mmu = mmu;

        this.registers = new Registers();
        this.instructionsMap = new InstructionsMap(mmu, gb.getRam(), registers);
    }

    public void step() {
        int pc = registers.getPC();
        int opcode = mmu.read(pc);
        instructionsMap.execute(opcode);
    }
}
