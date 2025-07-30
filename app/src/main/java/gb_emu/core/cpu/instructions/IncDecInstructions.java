package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.cpu.CPURegisters;

public class IncDecInstructions implements InstructionSet {
    private CPURegisters registers;

    public IncDecInstructions(CPURegisters registers) {
        this.registers = registers;
    }

    private void inc8Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value + 1) & 0xFF;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(false);
        registers.setFlagH(((value & 0xF) + 1) > 0xF);

        registers.incrementPC();
    }

    private void inc16Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value + 1) & 0xFFFF;

        setter.accept(result);

        registers.incrementPC();
    }

    private void dec8Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value - 1) & 0xFF;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(true);
        // Half borrow ocorre se o nibble baixo do valor original for 0
        registers.setFlagH((value & 0xF) == 0);

        registers.incrementPC();
    }

    private void dec16Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value - 1) & 0xFFFF;

        setter.accept(result);

        registers.incrementPC();
    }

    @Override
    public void registerAll(HashMap<Integer, Runnable> functions) {
        /**
         * INC
         */
        functions.put(0x03, () -> inc16Bit(registers::getBC, registers::setBC)); // INC BC
        functions.put(0x04, () -> inc8Bit(registers::getB, registers::setB)); // INC B
        functions.put(0x0C, () -> inc8Bit(registers::getC, registers::setC)); // INC C
        functions.put(0x13, () -> inc16Bit(registers::getDE, registers::setDE)); // INC DE
        functions.put(0x14, () -> inc8Bit(registers::getD, registers::setD)); // INC D
        functions.put(0x1C, () -> inc8Bit(registers::getE, registers::setE)); // INC E
        functions.put(0x23, () -> inc16Bit(registers::getHL, registers::setHL)); // INC HL
        functions.put(0x24, () -> inc8Bit(registers::getH, registers::setH)); // INC H
        functions.put(0x2C, () -> inc8Bit(registers::getL, registers::setL)); // INC L
        functions.put(0x3C, () -> inc8Bit(registers::getA, registers::setA)); // INC A
        functions.put(0x33, () -> inc16Bit(registers::getSP, registers::setSP)); // INC SP
        functions.put(0x34, () -> inc16Bit(registers::getHL, registers::setHL)); // // INC (HL)

        /**
         * DEC
         */
        functions.put(0x05, () -> dec8Bit(registers::getB, registers::setB)); // DEC B
        functions.put(0x0D, () -> dec8Bit(registers::getC, registers::setC)); // DEC C
        functions.put(0x15, () -> dec8Bit(registers::getD, registers::setD)); // DEC D
        functions.put(0x1D, () -> dec8Bit(registers::getE, registers::setE)); // DEC E
        functions.put(0x25, () -> dec8Bit(registers::getH, registers::setH)); // DEC H
        functions.put(0x2D, () -> dec8Bit(registers::getL, registers::setL)); // DEC L
        functions.put(0x3D, () -> dec8Bit(registers::getA, registers::setA)); // DEC A
        functions.put(0x0B, () -> dec16Bit(registers::getBC, registers::setBC)); // DEC BC
        functions.put(0x1B, () -> dec16Bit(registers::getDE, registers::setDE)); // DEC DE
        functions.put(0x2B, () -> dec16Bit(registers::getHL, registers::setHL)); // DEC HL
        functions.put(0x3B, () -> dec16Bit(registers::getSP, registers::setSP)); // DEC SP
        functions.put(0x35, () -> dec16Bit(registers::getHL, registers::setHL)); // DEC HL
    }
}
