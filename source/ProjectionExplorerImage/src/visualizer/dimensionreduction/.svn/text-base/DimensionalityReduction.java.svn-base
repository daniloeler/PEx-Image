/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.dimensionreduction;

import java.io.IOException;
import java.util.ArrayList;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.matrix.Vector;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class DimensionalityReduction {

    public DimensionalityReduction(int targetDimension) {
        this.targetDimension = targetDimension;
    }

    public Matrix reduce(Matrix matrix, Dissimilarity diss) throws IOException {
        Matrix redmatrix = new DenseMatrix();

        float[][] red = this.execute(matrix, diss);

        //transforming the reduce form into a dense matrix
        for (int i = 0; i < matrix.getRowCount(); i++) {
            Vector vector = matrix.getRow(i);

            DenseVector dvector = new DenseVector(red[i], vector.getId(), vector.getKlass());
            redmatrix.addRow(dvector);
        }

        //setting the new attributes
        ArrayList<String> attr = new ArrayList<String>();
        for (int i = 0; i < redmatrix.getDimensions(); i++) {
            attr.add("attr");
        }
        redmatrix.setAttributes(attr);

        return redmatrix;
    }

    protected abstract float[][] execute(Matrix matrix, Dissimilarity diss) throws IOException;

    protected int targetDimension;
}
