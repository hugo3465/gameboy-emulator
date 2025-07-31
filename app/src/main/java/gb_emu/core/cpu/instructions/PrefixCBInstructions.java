package gb_emu.core.cpu.instructions;

import java.util.HashMap;
import java.util.function.Consumer;

import com.google.common.base.Supplier;

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

    private void rlc(Supplier<Integer> getter, Consumer<Integer> setter) {
        int value = getter.get() & 0xFF;
        int carry = (value >> 7) & 1;
        int result = ((value << 1) & 0xFF) | carry;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(false);
        registers.setFlagH(false);
        registers.setFlagC(carry == 1);
    }

    private void rl(Supplier<Integer> getter, Consumer<Integer> setter) {
        int value = getter.get() & 0xFF;
        int oldCarry = registers.getFlagC() ? 1 : 0;
        int carry = (value >> 7) & 1;
        int result = ((value << 1) & 0xFF) | oldCarry;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(false);
        registers.setFlagH(false);
        registers.setFlagC(carry == 1);
    }

    private void bit(int bit, int value) {
        boolean isSet = (value & (1 << bit)) == 0;
        registers.setFlagZ(isSet);
        registers.setFlagN(false);
        registers.setFlagH(true);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * SWAP r
         */
        functions.put(0x30, wrap(() -> registers.setB(doSwap(registers.getB())), 8));

        functions.put(0x31, wrap(() -> registers.setC(doSwap(registers.getC())), 8));

        functions.put(0x32, wrap(() -> registers.setD(doSwap(registers.getD())), 8));

        functions.put(0x33, wrap(() -> registers.setE(doSwap(registers.getE())), 8));

        functions.put(0x34, wrap(() -> registers.setH(doSwap(registers.getH())), 8));

        functions.put(0x35, wrap(() -> registers.setL(doSwap(registers.getL())), 8));

        functions.put(0x36, wrap(() -> {
            int address = registers.getHL();
            int value = mmu.read(address);
            int swapped = doSwap(value);
            mmu.write(address, swapped);
        }, 16));

        functions.put(0x37, wrap(() -> registers.setA(doSwap(registers.getA())), 8));

        /**
         * RLC
         */
        functions.put(0x00, wrap(() -> rlc(registers::getB, registers::setB), 8)); // RLC B
        functions.put(0x01, wrap(() -> rlc(registers::getC, registers::setC), 8)); // RLC C
        functions.put(0x02, wrap(() -> rlc(registers::getD, registers::setD), 8)); // RLC D
        functions.put(0x03, wrap(() -> rlc(registers::getE, registers::setE), 8)); // RLC E
        functions.put(0x04, wrap(() -> rlc(registers::getH, registers::setH), 8)); // RLC H
        functions.put(0x05, wrap(() -> rlc(registers::getL, registers::setL), 8)); // RLC L
        functions.put(0x06, wrap(() -> { // RLC (HL)
            int addr = registers.getHL();
            int value = mmu.read(addr) & 0xFF;
            int carry = (value >> 7) & 1;
            int result = ((value << 1) & 0xFF) | carry;
            mmu.write(addr, result);

            registers.setFlagZ(result == 0);
            registers.setFlagN(false);
            registers.setFlagH(false);
            registers.setFlagC(carry == 1);
        }, 16));
        functions.put(0x07, wrap(() -> rlc(registers::getA, registers::setA), 8)); // RLC A

        /**
         * RL
         */
        functions.put(0x10, wrap(() -> rl(registers::getB, registers::setB), 8)); // RL B
        functions.put(0x11, wrap(() -> rl(registers::getC, registers::setC), 8)); // RL C
        functions.put(0x12, wrap(() -> rl(registers::getD, registers::setD), 8)); // RL D
        functions.put(0x13, wrap(() -> rl(registers::getE, registers::setE), 8)); // RL E
        functions.put(0x14, wrap(() -> rl(registers::getH, registers::setH), 8)); // RL H
        functions.put(0x15, wrap(() -> rl(registers::getL, registers::setL), 8)); // RL L
        functions.put(0x16, wrap(() -> { // RL (HL)
            int addr = registers.getHL();
            int value = mmu.read(addr) & 0xFF;
            int oldCarry = registers.getFlagC() ? 1 : 0;
            int carry = (value >> 7) & 1;
            int result = ((value << 1) & 0xFF) | oldCarry;
            mmu.write(addr, result);

            registers.setFlagZ(result == 0);
            registers.setFlagN(false);
            registers.setFlagH(false);
            registers.setFlagC(carry == 1);
        }, 16));
        functions.put(0x17, wrap(() -> rl(registers::getA, registers::setA), 8)); // RL A

        /**
         * BIT
         */
        functions.put(0x40, wrap(() -> bit(0, registers.getB()), 8)); // BIT 0, B
        functions.put(0x41, wrap(() -> bit(0, registers.getC()), 8)); // BIT 0, C
        functions.put(0x42, wrap(() -> bit(0, registers.getD()), 8)); // BIT 0, D
        functions.put(0x43, wrap(() -> bit(0, registers.getE()), 8)); // BIT 0, E
        functions.put(0x44, wrap(() -> bit(0, registers.getH()), 8)); // BIT 0, H
        functions.put(0x45, wrap(() -> bit(0, registers.getL()), 8)); // BIT 0, L
        functions.put(0x46, wrap(() -> bit(0, mmu.read(registers.getHL())), 12)); // BIT 0, (HL)
        functions.put(0x47, wrap(() -> bit(0, registers.getA()), 8)); // BIT 0, A

        functions.put(0x48, wrap(() -> bit(1, registers.getB()), 8)); // BIT 1, B
        functions.put(0x49, wrap(() -> bit(1, registers.getC()), 8)); // BIT 1, C
        functions.put(0x4A, wrap(() -> bit(1, registers.getD()), 8)); // BIT 1, D
        functions.put(0x4B, wrap(() -> bit(1, registers.getE()), 8)); // BIT 1, E
        functions.put(0x4C, wrap(() -> bit(1, registers.getH()), 8)); // BIT 1, H
        functions.put(0x4D, wrap(() -> bit(1, registers.getL()), 8)); // BIT 1, L
        functions.put(0x4E, wrap(() -> bit(1, mmu.read(registers.getHL())), 12)); // BIT 1, (HL)
        functions.put(0x4F, wrap(() -> bit(1, registers.getA()), 8)); // BIT 1, A

        functions.put(0x50, wrap(() -> bit(2, registers.getB()), 8)); // BIT 2, B
        functions.put(0x51, wrap(() -> bit(2, registers.getC()), 8)); // BIT 2, C
        functions.put(0x52, wrap(() -> bit(2, registers.getD()), 8)); // BIT 2, D
        functions.put(0x53, wrap(() -> bit(2, registers.getE()), 8)); // BIT 2, E
        functions.put(0x54, wrap(() -> bit(2, registers.getH()), 8)); // BIT 2, H
        functions.put(0x55, wrap(() -> bit(2, registers.getL()), 8)); // BIT 2, L
        functions.put(0x56, wrap(() -> bit(2, mmu.read(registers.getHL())), 12)); // BIT 2, (HL)
        functions.put(0x57, wrap(() -> bit(2, registers.getA()), 8)); // BIT 2, A

        functions.put(0x58, wrap(() -> bit(3, registers.getB()), 8)); // BIT 3, B
        functions.put(0x59, wrap(() -> bit(3, registers.getC()), 8)); // BIT 3, C
        functions.put(0x5A, wrap(() -> bit(3, registers.getD()), 8)); // BIT 3, D
        functions.put(0x5B, wrap(() -> bit(3, registers.getE()), 8)); // BIT 3, E
        functions.put(0x5C, wrap(() -> bit(3, registers.getH()), 8)); // BIT 3, H
        functions.put(0x5D, wrap(() -> bit(3, registers.getL()), 8)); // BIT 3, L
        functions.put(0x5E, wrap(() -> bit(3, mmu.read(registers.getHL())), 12)); // BIT 3, (HL)
        functions.put(0x5F, wrap(() -> bit(3, registers.getA()), 8)); // BIT 3, A

        functions.put(0x60, wrap(() -> bit(4, registers.getB()), 8)); // BIT 4, B
        functions.put(0x61, wrap(() -> bit(4, registers.getC()), 8)); // BIT 4, C
        functions.put(0x62, wrap(() -> bit(4, registers.getD()), 8)); // BIT 4, D
        functions.put(0x63, wrap(() -> bit(4, registers.getE()), 8)); // BIT 4, E
        functions.put(0x64, wrap(() -> bit(4, registers.getH()), 8)); // BIT 4, H
        functions.put(0x65, wrap(() -> bit(4, registers.getL()), 8)); // BIT 4, L
        functions.put(0x66, wrap(() -> bit(4, mmu.read(registers.getHL())), 12)); // BIT 4, (HL)
        functions.put(0x67, wrap(() -> bit(4, registers.getA()), 8)); // BIT 4, A

        functions.put(0x68, wrap(() -> bit(5, registers.getB()), 8)); // BIT 5, B
        functions.put(0x69, wrap(() -> bit(5, registers.getC()), 8)); // BIT 5, C
        functions.put(0x6A, wrap(() -> bit(5, registers.getD()), 8)); // BIT 5, D
        functions.put(0x6B, wrap(() -> bit(5, registers.getE()), 8)); // BIT 5, E
        functions.put(0x6C, wrap(() -> bit(5, registers.getH()), 8)); // BIT 5, H
        functions.put(0x6D, wrap(() -> bit(5, registers.getL()), 8)); // BIT 5, L
        functions.put(0x6E, wrap(() -> bit(5, mmu.read(registers.getHL())), 12)); // BIT 5, (HL)
        functions.put(0x6F, wrap(() -> bit(5, registers.getA()), 8)); // BIT 5, A

        functions.put(0x70, wrap(() -> bit(6, registers.getB()), 8)); // BIT 6, B
        functions.put(0x71, wrap(() -> bit(6, registers.getC()), 8)); // BIT 6, C
        functions.put(0x72, wrap(() -> bit(6, registers.getD()), 8)); // BIT 6, D
        functions.put(0x73, wrap(() -> bit(6, registers.getE()), 8)); // BIT 6, E
        functions.put(0x74, wrap(() -> bit(6, registers.getH()), 8)); // BIT 6, H
        functions.put(0x75, wrap(() -> bit(6, registers.getL()), 8)); // BIT 6, L
        functions.put(0x76, wrap(() -> bit(6, mmu.read(registers.getHL())), 12)); // BIT 6, (HL)
        functions.put(0x77, wrap(() -> bit(6, registers.getA()), 8)); // BIT 6, A

        functions.put(0x78, wrap(() -> bit(7, registers.getB()), 8)); // BIT 7, B
        functions.put(0x79, wrap(() -> bit(7, registers.getC()), 8)); // BIT 7, C
        functions.put(0x7A, wrap(() -> bit(7, registers.getD()), 8)); // BIT 7, D
        functions.put(0x7B, wrap(() -> bit(7, registers.getE()), 8)); // BIT 7, E
        functions.put(0x7C, wrap(() -> bit(7, registers.getH()), 8)); // BIT 7, H
        functions.put(0x7D, wrap(() -> bit(7, registers.getL()), 8)); // BIT 7, L
        functions.put(0x7E, wrap(() -> bit(7, mmu.read(registers.getHL())), 12)); // BIT 7, (HL)
        functions.put(0x7F, wrap(() -> bit(7, registers.getA()), 8)); // BIT 7, A

    }
}
