package gb_emu.ui.debugScreen;

import gb_emu.core.ppu.PPURegisters;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class PPURegistersPanel extends JPanel {

    private PPURegisters registers;
    private Map<String, JLabel> registerLabels;

    public PPURegistersPanel(PPURegisters registers) {
        this.registers = registers;
        this.registerLabels = new LinkedHashMap<>();

        setLayout(new GridLayout(0, 2, 10, 10)); // Two columns: Register Name and Value

        // Add all registers to the panel
        addRegister("LCDC", registers.getLCDC());
        addRegister("STAT", registers.getSTAT());
        addRegister("SCY", registers.getSCY());
        addRegister("SCX", registers.getSCX());
        addRegister("LY", registers.getLY());
        addRegister("LYC", registers.getLYC());
        addRegister("WY", registers.getWY());
        addRegister("WX", registers.getWX());
        addRegister("BGP", registers.getBGP());
    }

    private void addRegister(String name, int value) {
        JLabel nameLabel = new JLabel(name + ":");
        JLabel valueLabel = new JLabel(String.format("0x%04X", value)); // Display in hexadecimal format
        registerLabels.put(name, valueLabel);

        add(nameLabel);
        add(valueLabel);
    }

    public void updateRegisters() {
        // Update the values of all registers
        registerLabels.get("LCDC").setText(String.format("0x%02X", registers.getLCDC()));
        registerLabels.get("STAT").setText(String.format("0x%02X", registers.getSTAT()));
        registerLabels.get("SCY").setText(String.format("0x%02X", registers.getSCY()));
        registerLabels.get("SCX").setText(String.format("0x%02X", registers.getSCX()));
        registerLabels.get("LY").setText(String.format("0x%02X", registers.getLY()));
        registerLabels.get("LYC").setText(String.format("0x%02X", registers.getLYC()));
        registerLabels.get("WY").setText(String.format("0x%02X", registers.getWY()));
        registerLabels.get("WX").setText(String.format("0x%02X", registers.getWX()));
        registerLabels.get("BGP").setText(String.format("0x%04X", registers.getBGP()));
    }
}
