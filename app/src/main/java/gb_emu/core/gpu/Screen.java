package gb_emu.core.gpu;

public class Screen {
    public static int SCREEN_WIDTH = 160;
    public static int SCREEN_HEIGHT = 144;

    // Used array instead of a matrix for easy graphic libraries integration
    private int[] screen = new int[SCREEN_WIDTH * SCREEN_HEIGHT];

    public Screen() {
    }

    public int[] getPixels() {
        return screen;
    }

    public void writeOnScreen(int index, int color) {
        if (index >= 0 && index < screen.length) {
            screen[index] = color;
        }
    }
}
