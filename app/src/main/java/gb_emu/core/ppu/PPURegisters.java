package gb_emu.core.ppu;

public class PPURegisters {
    private int LCDC = 0x91; // (0xFF40) LCD Control
    private int STAT = 0x00; // (0xFF41) LCD Status
    private int SCY = 0x00; // (0xFF42) Scroll Y
    private int SCX = 0x00; // (0xFF43) Scroll X
    private int LY = 0x00; // (0xFF44) Current line
    private int LYC = 0x00; // (0xFF45) Line compare
    private int WY = 0x00; // (0xFF4A) Window Y
    private int WX = 0x00; // (0xFF4B) Window X
    private int BGP = 0xFC; // (0xFF47) Background palette

    public PPURegisters() {
    }

    public int getLCDC() {
        return LCDC;
    }

    public void setLCDC(int lCDC) {
        LCDC = lCDC;
    }

    public int getSTAT() {
        return STAT;
    }

    public void setSTAT(int sTAT) {
        STAT = sTAT;
    }

    public int getSCY() {
        return SCY;
    }

    public void setSCY(int sCY) {
        SCY = sCY;
    }

    public int getSCX() {
        return SCX;
    }

    public void setSCX(int sCX) {
        SCX = sCX;
    }

    public int getLY() {
        return LY;
    }

    public void setLY(int lY) {
        LY = lY;
    }

    public void incrementLY() {
        LY++;
    }

    public int getLYC() {
        return LYC;
    }

    public void setLYC(int lYC) {
        LYC = lYC;
    }

    public int getWY() {
        return WY;
    }

    public void setWY(int wY) {
        WY = wY;
    }

    public int getWX() {
        return WX;
    }

    public void setWX(int wX) {
        WX = wX;
    }

    public int getBGP() {
        return BGP;
    }

    public void setBGP(int bGP) {
        BGP = bGP;
    }

    /**
     * LCDC Utils
     * from coffee-gb
     */

    public boolean isBgAndWindowDisplay() {
        return (LCDC & 0x01) != 0;
    }

    public boolean isObjDisplay() {
        return (LCDC & 0x02) != 0;
    }

    public int getSpriteHeight() {
        return (LCDC & 0x04) == 0 ? 8 : 16;
    }

    public int getBgTileMapDisplay() {
        return (LCDC & 0x08) == 0 ? 0x9800 : 0x9c00;
    }

    public int getBgWindowTileData() {
        return (LCDC & 0x10) == 0 ? 0x9000 : 0x8000;
    }

    public boolean isBgWindowTileDataSigned() {
        return (LCDC & 0x10) == 0;
    }

    public boolean isWindowDisplay() {
        return (LCDC & 0x20) != 0;
    }

    public int getWindowTileMapDisplay() {
        return (LCDC & 0x40) == 0 ? 0x9800 : 0x9c00;
    }

    public boolean isLcdEnabled() {
        return (LCDC & 0x80) != 0;
    }

    public int readRegister(int address) {
        switch (address) {
            case 0xFF40: return getLCDC();
            case 0xFF41: return getSTAT();
            case 0xFF42: return getSCY();
            case 0xFF43: return getSCX();
            case 0xFF44: return getLY();
            case 0xFF45: return getLYC();
            case 0xFF47: return getBGP();
            case 0xFF4A: return getWY();
            case 0xFF4B: return getWX();
            default:
                return 0xFF;
        }
    }

    public void writeRegister(int address, int value) {
        switch (address) {
            case 0xFF40: setLCDC(value); break;
            case 0xFF41: setSTAT(value); break;
            case 0xFF42: setSCY(value); break;
            case 0xFF43: setSCX(value); break;
            case 0xFF44: 
                // LY just for reading
                break;
            case 0xFF45: setLYC(value); break;
            case 0xFF47: setBGP(value); break;
            case 0xFF4A: setWY(value); break;
            case 0xFF4B: setWX(value); break;
            default:
                break;
        }
    }
}
