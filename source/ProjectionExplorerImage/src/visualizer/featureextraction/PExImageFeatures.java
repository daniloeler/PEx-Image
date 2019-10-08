/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.featureextraction;

import ij.ImagePlus;

/**
 *
 * @author Danilo
 */
public class PExImageFeatures extends Feature{
    public static final int TOTAL_FEATURES = 28;    
    
    public double[] extract(ImagePlus imp){
        double[] features = new double[TOTAL_FEATURES];
        double [][] matrix = FeatureExtractionUtil.getMatrixFromImage(imp);
        double[] histogram = FeatureExtractionUtil.computeHistogramFromMatrix(matrix);
        DFT1D dft1d = new DFT1D(histogram);
        double[] histTransformed = new double[histogram.length];
        for (int i=0; i<histogram.length; i++){
            histTransformed[i] = dft1d.dftPoint(i).real();
        }
        for (int i=0; i<10; i++){
            features[i] = histTransformed[i+1];
        }
        for (int i=0; i<10; i++){
            features[10+i] = histTransformed[246+i];
        }
        
        int minRay;
        if (matrix.length <= matrix[0].length){
            minRay = matrix.length / 2;
        }
        else {
            minRay = matrix[0].length / 2;
        }
        
        GenericMask mask1 = new CircularMask(matrix.length, matrix[0].length, 10);
        mask1.createMask();
        GenericMask mask2 = new CircularMask(matrix.length, matrix[0].length, 15);
        mask2.createMask();        
        GenericMask mask3 = new CircularMask(matrix.length, matrix[0].length, 20);
        mask3.createMask();        
        GenericMask mask4 = new CircularMask(matrix.length, matrix[0].length, 25);        
        mask4.createMask();
        GenericMask mask5 = new CircularMask(matrix.length, matrix[0].length, (minRay - 10));        
        mask5.createMask();        
        GenericMask mask6 = new FullMask(matrix.length, matrix[0].length, 1);
        mask6.createMask();
        
        mask6.setMask( FeatureExtractionUtil.subtractMask1FromMask2( mask5.getMask(), mask6.getMask() ));
        mask5.setMask( FeatureExtractionUtil.subtractMask1FromMask2( mask4.getMask(), mask5.getMask() ) );
        mask4.setMask( FeatureExtractionUtil.subtractMask1FromMask2( mask3.getMask(), mask4.getMask() ) );
        mask3.setMask( FeatureExtractionUtil.subtractMask1FromMask2( mask2.getMask(), mask3.getMask() ) );
        mask2.setMask( FeatureExtractionUtil.subtractMask1FromMask2( mask1.getMask(), mask2.getMask() ) );
        //mask1 is the proper mask1
        
        DFT2D dft2d = new DFT2D(matrix);
        dft2d.execute();
        double[][] matrixAux = new double[matrix.length][matrix[0].length];
        for (int y=0; y<matrix.length; y++){
            for (int x=0; x<matrix[y].length; x++){
                matrixAux[y][x] = dft2d.getTransData()[y][x].real();
            }            
        }
        double[][] matrixTransformed = FeatureExtractionUtil.fftShift(matrixAux);
        
        System.out.println("Fourier Transform Done");
        double [][] masked = FeatureExtractionUtil.applyMaskOnMatrix(mask1.getMask(), matrixTransformed);
        features[20] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask1.getNumberOfNonZeros());
        
        masked = FeatureExtractionUtil.applyMaskOnMatrix(mask2.getMask(), matrixTransformed);
        features[21] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask2.getNumberOfNonZeros());
        
        masked = FeatureExtractionUtil.applyMaskOnMatrix(mask3.getMask(), matrixTransformed);
        features[22] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask3.getNumberOfNonZeros());
        
        masked = FeatureExtractionUtil.applyMaskOnMatrix(mask4.getMask(), matrixTransformed);
        features[23] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask4.getNumberOfNonZeros());
        
        masked = FeatureExtractionUtil.applyMaskOnMatrix(mask5.getMask(), matrixTransformed);
        features[24] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask5.getNumberOfNonZeros());
        
        masked = FeatureExtractionUtil.applyMaskOnMatrix(mask6.getMask(), matrixTransformed);
        features[25] = FeatureExtractionUtil.computeEnergyFromNonZero(masked, mask6.getNumberOfNonZeros());
        System.out.println("Mask Applied");
        features[26] = FeatureExtractionUtil.computeMeanFromMatrix(matrix);
        features[27] = FeatureExtractionUtil.computeStdDeviationFromMatrix(matrix);
        
        return features;
    }
}
