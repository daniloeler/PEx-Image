/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

import java.util.Vector;

/**
 *
 * @author Danilo Medeiros Eler
 */
public class DFT1D {

    private int N;        // number of "points" in the DFT
    private double[] data;  // Data on which the DFT is performed, 
    // set up in constructor
    public DFT1D(double[] d) {
        N = 0;
        data = null;
        if (d != null) {
            int len = d.length;
            if (len > 0) {                
                // the Vector length is > 0 && the type is correct
                N = len;
                data = d;
            }
        }
    } // dft constructor

    public Complex dftPoint(int m) {
        final double twoPi = 2 * Math.PI;
        Complex cx = new Complex(0.0, 0.0);

        if (m >= 0 && m < N) {
            double R = 0.0;
            double I = 0.0;

            // At m == 0 the DFT reduces to the sum of the data
            if (m == 0) {
                double p;
                for (int n = 0; n < N; n++) {
                    p = data[n];
                    R = R + p;
                } // for
            } else {
                double x;
                double scale;
                Double p;

                for (int n = 0; n < N; n++) {
                    p = data[n];
                    x = p;
                    scale = (twoPi * n * m) / N;
                    R = R + x * Math.cos(scale);
                    I = I - x * Math.sin(scale);
                } // for
            } // else
            cx.setReal((Double) R);
            cx.setImag((Double) I);
        }
        return cx;
    } // dftPoint
}
