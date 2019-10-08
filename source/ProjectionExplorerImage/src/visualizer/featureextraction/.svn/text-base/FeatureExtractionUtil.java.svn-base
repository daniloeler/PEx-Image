/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 *
 * @author Danilo Medeiros Eler
 */
public class FeatureExtractionUtil {

    public static double[][] getMatrixFromImage(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        int width = ip.getWidth();
        int height = ip.getHeight();
        double[][] matrix = new double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[y][x] = ip.getPixel(x, y);
            }
        }
        return matrix;
    }

    public static double[][] fftShift(double[][] matrix) {
        double[][] mS = new double[matrix.length][matrix[0].length];

        int mRow = matrix.length / 2;
        int mCol = matrix[0].length / 2;
        //System.out.println("mRow: " + mRow + " mCol: " + mCol);
        int yInv = 0;
        int xInv = 0;
        int y;
        int x;
        //System.out.println("Trying to Shift");
        //Part 1 - up left
        yInv = mRow;
        for (y = 0; y <= mRow; y++) {
            xInv = mCol;
            for (x = 0; x <= mCol; x++) {
                mS[yInv][xInv] = matrix[y][x];
                xInv--;
            }
            yInv--;
        }
        //System.out.println("Part 1 - 0k");
        //Part 2 - up right
        yInv = mRow;
        for (y = 0; y <= mRow; y++) {
            xInv = matrix[y].length - 1;
            for (x = mCol + 1; x < matrix[y].length; x++) {
                mS[yInv][xInv] = matrix[y][x];
                xInv--;
            }
            yInv--;
        }
        //System.out.println("Part 2 - 0k");
        //Part 3 - down left
        yInv = matrix.length - 1;
        for (y = mRow + 1; y < matrix.length; y++) {
            xInv = mCol;
            for (x = 0; x <= mCol; x++) {
                mS[yInv][xInv] = matrix[y][x];
                xInv--;
            }
            yInv--;
        }
        //System.out.println("Part 3 - 0k");
        //Part 4 - down right
        yInv = matrix.length - 1;
        for (y = mRow + 1; y < matrix.length; y++) {
            xInv = matrix[y].length - 1;
            for (x = mCol + 1; x < matrix[y].length; x++) {
                mS[yInv][xInv] = matrix[y][x];
                xInv--;
            }
            yInv--;
        }
        //System.out.println("Part 4 - 0k");
        return mS;
    }

    public static double[][] applyMaskOnMatrix(double[][] mask, double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (mask[y][x] == 1) {
                    result[y][x] = matrix[y][x];
                } else {
                    result[y][x] = 0;
                }
            }
        }
        return result;
    }

    public static double[][] subtractMask1FromMask2(double[][] mask1, double[][] mask2) {
        double[][] result = new double[mask1.length][mask1[0].length];
        for (int y = 0; y < mask1.length; y++) {
            for (int x = 0; x < mask1[y].length; x++) {
                result[y][x] = mask2[y][x] - mask1[y][x];

            }
        }
        return result;
    }

    public static double computeEnergyFromNonZero(double[][] matrix, int numElem) {
        double energy = 0;
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                energy = energy + Math.abs(matrix[y][x]);
            }
        }
        energy = energy / numElem;
        return energy;
    }

    public static double computeMeanFromMatrix(double[][] matrix) {
        double mean = 0;
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                mean = mean + Math.abs(matrix[y][x]);
            }
        }
        mean = mean / (matrix.length * matrix[0].length);
        return mean;
    }

    public static double computeStdDeviationFromMatrix(double[][] matrix) {
        double mean = computeMeanFromMatrix(matrix);
        double xi = 0;
        double sum = 0;
        double stdDev = 0;
        int np = matrix.length * matrix[0].length;

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                xi = matrix[y][x];
                sum = sum + ((xi - mean) * (xi - mean));
            }
        }
        stdDev = Math.sqrt(sum / (np - 1));

        return stdDev;
    }

    public static double[] computeHistogramFromMatrix(double[][] matrix) {
        double[] histogram = new double[256];
        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                int pos = (int) matrix[y][x];
                histogram[pos] += 1;
            }
        }
        return histogram;
    }
}
