package gb_emu.core.cpu.instructions;

import java.util.HashMap;

import gb_emu.core.Instruction;
import gb_emu.core.cpu.CPU;
import gb_emu.core.cpu.CPURegisters;
import gb_emu.core.mem.MMU;

public class IncDecInstructions extends AbstractInstruction implements InstructionSet {
    public IncDecInstructions(CPU cpu, CPURegisters registers, MMU mmu) {
        super(cpu, registers, mmu);
    }

    private void inc8Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value + 1) & 0xFF;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(false);
        registers.setFlagH(((value & 0xF) + 1) > 0xF);
    }

    private void inc16Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value + 1) & 0xFFFF;

        setter.accept(result);
    }

    private void dec8Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value - 1) & 0xFF;

        setter.accept(result);

        registers.setFlagZ(result == 0);
        registers.setFlagN(true);
        // Half borrow ocorre se o nibble baixo do valor original for 0
        registers.setFlagH((value & 0xF) == 0);
    }

    private void dec16Bit(java.util.function.IntSupplier getter, java.util.function.IntConsumer setter) {
        int value = getter.getAsInt();
        int result = (value - 1) & 0xFFFF;

        setter.accept(result);
    }

    @Override
    public void registerAll(HashMap<Integer, Instruction> functions) {
        /**
         * INC
         */
        functions.put(0x03, wrap(() -> inc16Bit(registers::getBC, registers::setBC), 8)); // INC BC
        functions.put(0x04, wrap(() -> inc8Bit(registers::getB, registers::setB), 4)); // INC B
        functions.put(0x0C, wrap(() -> inc8Bit(registers::getC, registers::setC), 4)); // INC C
        functions.put(0x13, wrap(() -> inc16Bit(registers::getDE, registers::setDE), 8)); // INC DE
        functions.put(0x14, wrap(() -> inc8Bit(registers::getD, registers::setD), 4)); // INC D
        functions.put(0x1C, wrap(() -> inc8Bit(registers::getE, registers::setE), 4)); // INC E
        functions.put(0x23, wrap(() -> inc16Bit(registers::getHL, registers::setHL), 8)); // INC HL
        functions.put(0x24, wrap(() -> inc8Bit(registers::getH, registers::setH), 4)); // INC H
        functions.put(0x2C, wrap(() -> inc8Bit(registers::getL, registers::setL), 4)); // INC L
        functions.put(0x3C, wrap(() -> inc8Bit(registers::getA, registers::setA), 4)); // INC A
        functions.put(0x33, wrap(() -> inc16Bit(registers::getSP, registers::setSP), 8)); // INC SP
        functions.put(0x34, wrap(() -> inc16Bit(registers::getHL, registers::setHL), 12)); // // INC (HL)

        /**
         * DEC
         */
        functions.put(0x05, wrap(() -> dec8Bit(registers::getB, registers::setB), 4)); // DEC B
        functions.put(0x0D, wrap(() -> dec8Bit(registers::getC, registers::setC), 4)); // DEC C
        functions.put(0x15, wrap(() -> dec8Bit(registers::getD, registers::setD), 4)); // DEC D
        functions.put(0x1D, wrap(() -> dec8Bit(registers::getE, registers::setE), 4)); // DEC E
        functions.put(0x25, wrap(() -> dec8Bit(registers::getH, registers::setH), 4)); // DEC H
        functions.put(0x2D, wrap(() -> dec8Bit(registers::getL, registers::setL), 4)); // DEC L
        functions.put(0x3D, wrap(() -> dec8Bit(registers::getA, registers::setA), 4)); // DEC A
        functions.put(0x0B, wrap(() -> dec16Bit(registers::getBC, registers::setBC), 8)); // DEC BC
        functions.put(0x1B, wrap(() -> dec16Bit(registers::getDE, registers::setDE), 8)); // DEC DE
        functions.put(0x2B, wrap(() -> dec16Bit(registers::getHL, registers::setHL), 8)); // DEC HL
        functions.put(0x3B, wrap(() -> dec16Bit(registers::getSP, registers::setSP), 8)); // DEC SP
        functions.put(0x35, wrap(() -> dec16Bit(registers::getHL, registers::setHL), 12)); // DEC HL
    }
}