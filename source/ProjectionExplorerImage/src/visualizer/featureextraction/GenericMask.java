/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

/**
 *
 * @author Danilo Medeiros Eler
 */
public abstract class GenericMask {

    protected int Row;
    protected int Col;
    protected double mask[][];

    GenericMask(int row, int col) {
        this.Row = row;
        this.Col = col;
        mask = new double[this.Row][this.Col];
    }

    public void setRow(int row) {
        this.Row = row;
    }

    public int getRow() {
        return this.Row;
    }

    public void setCol(int col) {
        this.Col = col;
    }

    public int getCol() {
        return this.Col;
    }

    public void allocateMask() {
        mask = new double[this.Row][this.Col];
    }

    public double[][] getMask() {
        return mask;
    }

    public void setMask(double mask[][]) {
        this.mask = mask;
    }
    
    public int getNumberOfNonZeros(){
        int sum=0;
        for (int y=0; y<this.Row; y++){
            for (int x=0; x<this.Col; x++){
                if (this.mask[y][x] == 1){
                    sum = sum + 1;   
                }
            }
        }
        return sum;
    }

    abstract void createMask();
}
