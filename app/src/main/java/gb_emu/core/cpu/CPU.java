package gb_emu.core.cpu;

import java.io.Serializable;

import gb_emu.core.GameBoy;


public class CPU implements Serializable{
    private GameBoy gb;
    private Registers registers;

    public CPU(GameBoy gb) {
        this.gb = gb;

        this.registers = new Registers();
    }
}
