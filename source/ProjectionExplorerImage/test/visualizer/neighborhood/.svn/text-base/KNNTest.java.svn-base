/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.neighborhood;

import java.util.Arrays;
import junit.framework.TestCase;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.matrix.SparseMatrix;
import visualizer.matrix.SparseVector;
import visualizer.projection.distance.CosineBased;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.Euclidean;
import visualizer.util.KNN;
import visualizer.util.Pair;

/**
 *
 * @author user
 */
public class KNNTest extends TestCase {

    public KNNTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class KNN.
     */
    public void testExecute1() throws Exception {
        System.out.println("execute1: sparse matrix");

        for (int t = 0; t < 15; t++) {
            Matrix matrix = new SparseMatrix();

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = 0.0f;
                point[1] = 0.0f;
                matrix.addRow(new SparseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random() + 10;
                point[1] = (float) Math.random();
                matrix.addRow(new SparseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random();
                point[1] = (float) Math.random() + 10;
                matrix.addRow(new SparseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random() + 10;
                point[1] = (float) Math.random() + 10;
                matrix.addRow(new SparseVector(point));
            }

            int nrNeighbors = (int) (23 * Math.random()) + 1;

            Dissimilarity diss = new Euclidean();
            if (t % 2 == 0) {
                diss = new CosineBased();
            }

            KNN knn = new KNN(nrNeighbors);
            Pair[][] result = knn.execute(matrix, diss);

            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index > 25));
                }
            }

            for (int i = 25; i < 50; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 25 && result[i][j].index > 50));
                }
            }

            for (int i = 50; i < 75; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 50 && result[i][j].index > 75));
                }
            }

            for (int i = 75; i < 100; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 75));
                }
            }
        }
    }

    public void testExecute2() throws Exception {
        System.out.println("execute2 : dense matrix");

        for (int t = 0; t < 15; t++) {
            Matrix matrix = new DenseMatrix();

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = 0.0f;
                point[1] = 0.0f;
                matrix.addRow(new DenseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random() + 10;
                point[1] = (float) Math.random();
                matrix.addRow(new DenseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random();
                point[1] = (float) Math.random() + 10;
                matrix.addRow(new DenseVector(point));
            }

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random() + 10;
                point[1] = (float) Math.random() + 10;
                matrix.addRow(new DenseVector(point));
            }

            int nrNeighbors = (int) (23 * Math.random()) + 1;

            Dissimilarity diss = new Euclidean();
            if (t % 2 == 0) {
                diss = new CosineBased();
            }

            KNN knn = new KNN(nrNeighbors);
            Pair[][] result = knn.execute(matrix, diss);

            for (int i = 0; i < 25; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index > 25));
                }
            }

            for (int i = 25; i < 50; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 25 && result[i][j].index > 50));
                }
            }

            for (int i = 50; i < 75; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 50 && result[i][j].index > 75));
                }
            }

            for (int i = 75; i < 100; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.",
                            (result[i][j].index < 75));
                }
            }
        }
    }

    public void testExecute3() throws Exception {
        System.out.println("execute3 : null vectors");

        for (int t = 0; t < 15; t++) {
            Matrix matrix = new SparseMatrix();

            int DIM = 1000;

            for (int i = 0; i < 50; i++) {
                float[] point = new float[DIM];
                Arrays.fill(point, 0.0f);
                matrix.addRow(new SparseVector(point));
            }

            for (int i = 0; i < 450; i++) {
                float[] point = new float[DIM];
                Arrays.fill(point, 0.0f);

                for (int j = 0; j < point.length; j++) {
                    float esp = (float) Math.random();
                    if (esp > 0.8f) {
                        point[j] = (float) Math.random();
                    }
                }

                matrix.addRow(new SparseVector(point));
            }

            int nrNeighbors = (int) (48 * Math.random()) + 1;

            Dissimilarity diss = new Euclidean();
            if (t % 2 == 0) {
                diss = new CosineBased();
            }

            KNN knn = new KNN(nrNeighbors);
            Pair[][] result = knn.execute(matrix, diss);

            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    assertFalse("neighborhood wil defined.", (result[i][j].index > 50));
                }
            }
        }
    }

}
