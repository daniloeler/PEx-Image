/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

/**
 *
 * @author Danilo Medeiros Eler
 */
import java.lang.Math.*;

public class Complex {

    private Double R; // real part
    private Double I; // imaginary part

    /** Argumentless constructor */
    public Complex() {
        R = 0.0;
        I = 0.0;
    }

    /** constructor with real and imaginary parts */
    public Complex(Double r, Double i) {
        R = r;
        I = i;
    }

    /** set the real part */
    public void setReal(Double r) {
        R = r;
    }

    /** return the real part */
    public Double real() {
        return R;
    }

    /** set the imaginary part */
    public void setImag(Double i) {
        I = i;
    }

    /** return the imaginary part */
    public Double imag() {
        return I;
    }

    public static Complex multiply(Complex c1, Complex c2) {
        Complex cR = new Complex(0.0, 0.0);
        cR.setReal(c1.real() * c2.real() - c1.imag() * c2.imag());
        cR.setImag(c1.real() * c2.imag() + c1.imag() * c2.real());
        return cR;
    }

    public static Complex multiply(double elem, Complex c1) {
        Complex cR = new Complex(0.0, 0.0);
        cR.setReal(elem * c1.real());
        cR.setImag(elem * c1.imag());
        return cR;
    }
}
