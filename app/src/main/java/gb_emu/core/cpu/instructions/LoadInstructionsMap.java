package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.Registers;
import gb_emu.core.mem.MMU;
import gb_emu.core.mem.RAM;

public class LoadInstructionsMap implements InstructionSet {
    private Registers registers;
    private MMU mmu;

    public LoadInstructionsMap(Registers registers, MMU mmu) {
        this.registers = registers;
        this.mmu = mmu;
    }

    private int readImmediate8() {
        int value = mmu.read(registers.getPC());
        registers.incrementPC();
        return value;
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        functions.put(0x00, () -> { // NOP
        });

        functions.put(0x02, () -> { // LD (BC), A
            int address = registers.getBC();
            mmu.write(address, registers.getA());
        });

        functions.put(0x06, () -> { // LD B, d8
            registers.setB(readImmediate8());
        });

        functions.put(0x0E, () -> { // LD C, d8
            registers.setC(readImmediate8());
        });

        functions.put(0x12, () -> { // LD (DE), A
            int address = registers.getDE();
            mmu.write(address, registers.getA());
        });

        functions.put(0x16, () -> { // LD D, d8
            registers.setD(readImmediate8());
        });

        functions.put(0x1A, () -> { // LD A, (DE)
            int address = registers.getDE();
            int value = mmu.read(address);
            registers.setA(value);
        });

        functions.put(0x1E, () -> { // LD E, d8
            registers.setE(readImmediate8());
        });

        functions.put(0x22, () -> { // LD (HL+), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address + 1) & 0xFFFF);
        });

        functions.put(0x26, () -> { // LD H, d8
            registers.setH(readImmediate8());
        });

        functions.put(0x2A, () -> { // LD A, (HL+)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() + 1) & 0xFFFF);
        });

        functions.put(0x2E, () -> { // LD L, d8
            registers.setL(readImmediate8());
        });

        functions.put(0x32, () -> { // LD (HL-), A
            int address = registers.getHL();
            mmu.write(address, registers.getA());
            registers.setHL((address - 1) & 0xFFFF);
        });

        functions.put(0x36, () -> { // LD (HL), d8
            mmu.write(registers.getHL(), readImmediate8());
        });

        functions.put(0x3A, () -> { // LD A, (HL-)
            int value = mmu.read(registers.getHL());
            registers.setA(value);
            registers.setHL((registers.getHL() - 1) & 0xFFFF);
        });

        functions.put(0x3E, () -> { // LD A, d8
            registers.setA(readImmediate8());
        });
    }
}
