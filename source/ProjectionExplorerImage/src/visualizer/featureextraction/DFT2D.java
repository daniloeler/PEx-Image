/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

/**
 *
 * @author Danilo
 */
import java.util.Vector;

public class DFT2D {

    private int Col = 0;        // number of columns
    private int Row = 0;        // number of rows 
    private double[][] data;  // Data on which the DFT is performed, 
    // set up in constructor
    private Complex[][] transData;

    public DFT2D(double matrix[][]) {
        data = matrix;
        if (matrix != null) {
            this.Row = matrix.length;
            this.Col = matrix[0].length;
            this.transData = new Complex[this.Row][this.Col];
        }
        System.out.println("Cols: = " + this.Col);
        System.out.println("Rows: = " + this.Row);
    } // dft constructor

    public Complex[][] getTransData() {
        return this.transData;
    }

    public void execute() {
        final double twoPi = 2 * Math.PI;
        Complex[][] transDataTemp = new Complex[this.Row][this.Col];
        for (int x = 0; x < this.Col; x++) {
            for (int y = 0; y < this.Row; y++) {
                double scale;
                double elem; //element
                Complex cE = new Complex(0.0, 0.0);
                Complex cT = new Complex(0.0, 0.0);
                Complex cR;
                int m = y;
                for (int n = 0; n < this.Row; n++) {
                    elem = data[n][x];
                    scale = (twoPi * n * m) / this.Row;
                    cE.setReal(Math.cos(scale));
                    cE.setImag(-Math.sin(scale));
                    cR = Complex.multiply(elem, cE);
                    cT.setReal(cT.real() + cR.real());
                    cT.setImag(cT.imag() + cR.imag());
                }
                transDataTemp[y][x] = cT;
            }
        }
        /*
        System.out.println("\n\nParte Real\n");
        for (int y=0; y<this.Row; y++){
        for (int x=0; x<this.Col; x++){
        System.out.print(this.transDataTemp[y][x].real() + " ");
        }
        System.out.println("");
        }
        System.out.println("\n\nParte Imaginaria\n");
        for (int y=0; y<this.Row; y++){
        for (int x=0; x<this.Col; x++){
        System.out.print(this.transDataTemp[y][x].imag() + " ");
        }
        System.out.println("");
        }    
         */
        for (int y = 0; y < this.Row; y++) {
            for (int x = 0; x < this.Col; x++) {
                Complex elem; //element
                Complex cE = new Complex(0.0, 0.0);
                Complex cT = new Complex(0.0, 0.0);
                Complex cR;
                double scale;
                int m = x;
                for (int n = 0; n < this.Col; n++) {
                    elem = transDataTemp[y][n];
                    scale = (twoPi * n * m) / this.Col;
                    cE.setReal(Math.cos(scale));
                    cE.setImag(-Math.sin(scale));
                    cR = Complex.multiply(elem, cE);
                    cT.setReal(cT.real() + cR.real());
                    cT.setImag(cT.imag() + cR.imag());
                }
                this.transData[y][x] = cT;
            }
        }
    }

    public Complex dftPoint(int r, int c) {
        final double twoPi = 2 * Math.PI;
        Complex cx = new Complex(0.0, 0.0);

        if (r >= 0 && r < this.Row && c >= 0 && c <= this.Col) {
            double R = 0.0;
            double I = 0.0;

            double x;
            double y;
            double scaleC;
            double scaleR;
            double scaleS;
            Double p;
            for (int nX = 0; nX < this.Col; nX++) {
                for (int nY = 0; nY < this.Row; nY++) {
                    p = data[nY][nX];
                    x = p;
                    scaleC = (twoPi * c * nX) / this.Col;
                    scaleR = (twoPi * r * nY) / this.Row;
                    scaleS = scaleC + scaleR;
                    R = R + x * (Math.cos(scaleS));
                //I = I - x * ( Math.sin( scaleS ) );
                }
            } // for
            //} // else
            cx.setReal((Double) R);
            cx.setImag((Double) I);
        }
        return cx;
    } // dftPoint
} // class dft

