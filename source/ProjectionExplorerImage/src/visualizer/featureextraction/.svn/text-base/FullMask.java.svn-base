/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

/**
 *
 * @author Danilo
 */
public class FullMask extends GenericMask {
    private double value;
    
    public FullMask(int row, int col, double value){
        super(row, col);
        this.value = value;                 
    }
    
    public double getValue(){
        return this.value;
    }
    
    public void setValue(double value){
        this.value = value;
    }
    
    public void createMask() {
        for (int y = 0; y < this.Row; y++) {
            for (int x = 0; x < this.Col; x++) {
                this.mask[y][x] = value;
            }
        }
    }
}
