package gb_emu.core.cpu;

import java.io.Serializable;

import gb_emu.core.GameBoy;
import gb_emu.core.cpu.registers.FlagRegister;
import gb_emu.core.cpu.registers.Register;
import gb_emu.core.cpu.registers.Register16Bit;
import gb_emu.core.cpu.registers.Register8Bit;


public class CPU implements Serializable{
    private GameBoy gb;
    private Register a, b, c, d, e, f, h, l, sp, pc;

    public CPU(GameBoy gb) {
        this.gb = gb;

        this.a = new Register8Bit();
        this.b = new Register8Bit();
        this.c = new Register8Bit();
        this.d = new Register8Bit();
        this.e = new Register8Bit();
        this.h = new Register8Bit();
        this.l = new Register8Bit();

        this.f = new FlagRegister();
        
        this.sp = new Register16Bit();
        this.pc = new Register16Bit();
    }
}
