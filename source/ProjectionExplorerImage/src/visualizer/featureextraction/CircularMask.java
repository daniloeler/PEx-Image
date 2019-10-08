/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

/**
 *
 * @author Danilo
 */
public class CircularMask extends GenericMask {

    private int ray;

    CircularMask(int row, int col, int ray) {
        super(row, col);
        this.ray = ray;
    }

    public void setRay(int ray) {
        this.ray = ray;
    }

    public int getRay() {
        return this.ray;
    }

    public void createMask() {
        int x = 0;
        int y = this.ray;
        int d = 1 / this.ray;
        int xO = this.Col / 2;
        int yO = this.Row / 2;

        circlePoints(xO, yO, x, y);
        while (x <= this.ray / 1.4142) {
            if (d < 0) {
                d = d + (2 * x + 3);
            } else {
                d = d + (2 * (x - y) + 5);
                y = y - 1;
            }
            x = x + 1;
            circlePoints(xO, yO, x, y);
        }
        fill(xO, yO + this.ray);
    }

    private void circlePoints(int xO, int yO, int x, int y) {
        this.mask[y + yO][x + xO] = 1;
        this.mask[y + yO][-x + xO] = 1;
        this.mask[-y + yO][x + xO] = 1;
        this.mask[-y + yO][-x + xO] = 1;

        this.mask[x + yO][y + xO] = 1;
        this.mask[x + yO][-y + xO] = 1;
        this.mask[-x + yO][y + xO] = 1;
        this.mask[-x + yO][-y + xO] = 1;
    }

    private void fill(int x, int y) {
        int yc = y - 1;
        while (mask[yc][x] == 0) {
            mask[yc][x] = 1; //filling the column central point 
            int xd = x + 1; //filling to rigth
            while (mask[yc][xd] == 0) {
                mask[yc][xd] = 1;
                xd = xd + 1;
            }
            int xe = x - 1;  //filling to left
            while (mask[yc][xe] == 0) {
                mask[yc][xe] = 1;
                xe = xe - 1;
            }
            yc = yc - 1;
        }
    }
}
