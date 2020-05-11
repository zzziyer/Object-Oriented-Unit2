public class Methods {
    private int[][] transfers = new int[23][23];
    private static int[] A =
        {1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 1, 1, 1};
    private static int[] B =
        {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
    private static int[] C =
        {0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
            1, 0, 1, 0, 1, 0, 0, 0, 0, 0};

    public static int[] getA() {
        return A;
    }

    public static int[] getB() {
        return B;
    }

    public static int[] getC() {
        return C;
    }

    public Methods() {
        int i;
        int j;
        for (i = 0; i < 23; i++) {
            for (j = 0; j < 23; j++) {
                transfers[i][j] = 100;
            }
        }
        for (j = 4; j <= 16; j++) { //13
            transfers[0][j] = 1;
        }
        transfers[1][5] = 1;
        transfers[2][5] = 1;
        transfers[4][5] = 1; //3
        for (i = 4; i <= 16; i++) {  //13*5=65
            for (j = 18; j <= 22; j++) {
                transfers[i][j] = 15;
            }
        }
        for (j = 6; j <= 16; j += 2) {
            transfers[5][j] = 5;
        }
    }

    public int getBuildingFooter(int floor) {
        int tempFloor = floor;
        if (tempFloor < 0) {
            tempFloor += 3;
        } else {
            tempFloor += 2;
        }
        return tempFloor;
    }

    public int get(int a, int b) {

        return transfers[getBuildingFooter(a)][getBuildingFooter(b)];
    }
}
