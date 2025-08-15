package gb_emu.core.mem.cartridge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gb_emu.core.mem.RAM;

public class Cartridge implements Serializable {
    public static final Logger LOGGER = LoggerFactory.getLogger(Cartridge.class);

    private boolean gbc;
    private byte[] romData;
    private byte[] botRom;
    private RAM externalRAM;
    private boolean bootRomEnabled = true;

    public Cartridge(String romPath, String botRomPath) {
        this.romData = loadFile(romPath);
        this.botRom = loadFile(botRomPath);
        this.gbc = false;

        int externalRamSize = getRAMSize(); // KiB
        if (externalRamSize > 0) {
            this.externalRAM = new RAM(externalRamSize * 1024, 0xA000);
        } else {
            this.externalRAM = null;
        }
    }

    /**
     * Read the rom in the specified path
     * 
     * @param path
     * @return byte array with the rom data
     * @throws RuntimeException
     */
    private byte[] loadFile(String path) throws RuntimeException {
        File file = new File(path);
        byte[] data = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead = fis.read(data);
            if (bytesRead != data.length) {
                throw new IOException("Error while reading the ROM file.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while reading the ROM file.");
        }

        return data;
    }

    public String getTitle() {
        byte[] titleBytes = Arrays.copyOfRange(romData, 0x0134, 0x0143);
        return new String(titleBytes).trim();
    }

    public CartridgeType getCartridgeType() {
        int cartridgeTypeCode = Byte.toUnsignedInt(romData[0x0147]);
        return CartridgeType.getFromCode(cartridgeTypeCode);
    }

    /**
     * @return number of rom banks in BiB
     */
    public int getROMSize() {
        int romSizeCode = Byte.toUnsignedInt(romData[0x0148]);
        switch (romSizeCode) {
            case 0x00:
                return 32; // 32 KiB
            case 0x01:
                return 64; // 64 KiB
            case 0x02:
                return 128; // 128 KiB
            case 0x03:
                return 256; // 256 KiB
            case 0x04:
                return 512; // 512 KiB
            case 0x05:
                return 1024; // 1 MiB
            case 0x06:
                return 2048; // 2 MiB
            case 0x07:
                return 4096; // 4 MiB
            case 0x08:
                return 8192; // 8 MiB
            case 0x52:
                return 1126; // 1.1 MiB (aprox. 1126 KiB)
            case 0x53:
                return 1280; // 1.2 MiB (aprox. 1280 KiB)
            case 0x54:
                return 1536; // 1.5 MiB (aprox. 1536 KiB)
            default:
                throw new IllegalArgumentException("Illegal ROM size code: " + romSizeCode);
        }
    }

    public int getROMBanks() {
        int romSizeCode = Byte.toUnsignedInt(romData[0x0148]);
        switch (romSizeCode) {
            case 0x00:
                return 2; // 2 banks (no banking)
            case 0x01:
                return 4;
            case 0x02:
                return 8;
            case 0x03:
                return 16;
            case 0x04:
                return 32;
            case 0x05:
                return 64;
            case 0x06:
                return 128;
            case 0x07:
                return 256;
            case 0x08:
                return 512;
            case 0x52:
                return 72;
            case 0x53:
                return 80;
            case 0x54:
                return 96;
            default:
                throw new IllegalArgumentException("Illegal ROM size code: " + romSizeCode);
        }
    }

    public int getRAMSize() {
        int ramSizeCode = Byte.toUnsignedInt(romData[0x0149]);
        switch (ramSizeCode) {
            case 0x00:
                return 0;
            case 0x01:
                return 0; // Unused
            case 0x02:
                return 8;
            case 0x03:
                return 32;
            case 0x04:
                return 128;
            case 0x05:
                return 64;
            default:
                throw new IllegalArgumentException("Illegal ram size, received " + ramSizeCode + " code");
        }
    }

    /**
     * Get the number of ram banks (each ram bank has 8KiB)
     * 
     * @return number of ram banks
     */
    public int getRAMBanks() {
        int ramSizeCode = Byte.toUnsignedInt(romData[0x0149]);
        switch (ramSizeCode) {
            case 0x00:
                return 0;
            case 0x01:
                return 0; // Unused
            case 0x02:
                return 1;
            case 0x03:
                return 4;
            case 0x04:
                return 16;
            case 0x05:
                return 8;
            default:
                throw new IllegalArgumentException("Illegal ram size, received " + ramSizeCode + " code");
        }
    }

    public int read(int address) {
        if (!gbc && bootRomEnabled && address >= 0x0000 && address < 0x0100) {
            return Byte.toUnsignedInt(botRom[address]);
        } else if (address == 0xFF50) {
            LOGGER.debug("[READ] FF50 -> bootRomEnabled=" + bootRomEnabled);
            return bootRomEnabled ? 0x00 : 0x01;
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            if (externalRAM == null) {
                // LOGGER.warn("Attempted to read from non-existent external RAM at 0x" +
                // Integer.toHexString(address));
                return 0xFF;
            }
            return externalRAM.read(address);
        } else {
            LOGGER.debug("[READ] ROM address 0x%04X = 0x%02X%n", address, Byte.toUnsignedInt(romData[address]));

            return Byte.toUnsignedInt(romData[address]);
        }
    }

    public void write(int address, int value) {
        CartridgeType cartridgeType = getCartridgeType();

        if (address == 0xFF50) {
            bootRomEnabled = false;
            LOGGER.debug("[WRITE] FF50 -> bootRomEnabled=false");
            return;
        }

        if (address >= 0xA000 && address <= 0xBFFF && externalRAM != null) {
            externalRAM.write(address, value);
        }

        // MBC0
        if (cartridgeType == CartridgeType.ROM_ONLY) {
            return;
        }

        // MBC's here in the future
    }

    public byte[] getRomData() {
        return romData;
    }

}