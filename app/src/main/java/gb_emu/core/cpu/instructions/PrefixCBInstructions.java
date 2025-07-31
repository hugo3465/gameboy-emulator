package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class PrefixCBInstructions extends AbstractInstruction implements InstructionSet {

    public PrefixCBInstructions(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    private int doSwap(int value) {
        int result = ((value & 0x0F) << 4) | ((value & 0xF0) >> 4);
        registers.setFlagZ(result == 0);
        registers.setFlagN(false);
        registers.setFlagH(false);
        registers.setFlagC(false);
        return result;
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * SWAP r
         */
        functions.put(0x30, wrap(() -> {
            registers.setB(doSwap(registers.getB()));
        }, 8));

        functions.put(0x31, wrap(() -> {
            registers.setC(doSwap(registers.getC()));
        }, 8));

        functions.put(0x32, wrap(() -> {
            registers.setD(doSwap(registers.getD()));
        }, 8));

        functions.put(0x33, wrap(() -> {
            registers.setE(doSwap(registers.getE()));
        }, 8));

        functions.put(0x34, wrap(() -> {
            registers.setH(doSwap(registers.getH()));
        }, 8));

        functions.put(0x35, wrap(() -> {
            registers.setL(doSwap(registers.getL()));
        }, 8));

        functions.put(0x36, wrap(() -> {
            int address = registers.getHL();
            int value = mmu.read(address);
            int swapped = doSwap(value);
            mmu.write(address, swapped);
        }, 16));

        functions.put(0x37, wrap(() -> {
            registers.setA(doSwap(registers.getA()));
        }, 8));
    }
}
