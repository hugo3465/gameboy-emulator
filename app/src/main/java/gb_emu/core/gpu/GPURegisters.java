package gb_emu.core.gpu;

public class GPURegisters {
    private int LCDC = 0x91; // (0xFF40) LCD Control
    private int STAT = 0x00; // (0xFF41) LCD Status
    private int SCY = 0x00; // (0xFF42) Scroll Y
    private int SCX = 0x00; // (0xFF43) Scroll X
    private int LY = 0x00; // (0xFF44) Current line
    private int LYC = 0x00; // (0xFF45) Line compare
    private int WY = 0x00; // (0xFF4A) Window Y
    private int WX = 0x00; // (0xFF4B) Window X
    private int BGP = 0xFC; // (0xFF47) Background palette

    public GPURegisters() {

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

    
}
