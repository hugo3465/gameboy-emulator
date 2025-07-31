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
    }
}
