/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.util;

import visualizer.datamining.clustering.BKmeans;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.TestCase;
import visualizer.matrix.Matrix;
import visualizer.matrix.SparseMatrix;
import visualizer.matrix.SparseVector;
import visualizer.projection.distance.CosineBased;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.Euclidean;

/**
 *
 * @author user
 */
public class BKmeansTest extends TestCase {

    public BKmeansTest(String testName) {
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
     * Test of clusters method, of class BKmeans.
     */
    public void testExecute() throws Exception {
        System.out.println("execute1: sparse matrix");

        for (int t = 0; t < 15; t++) {
            Matrix matrix = new SparseMatrix();

            for (int i = 0; i < 25; i++) {
                float[] point = new float[2];
                point[0] = (float) Math.random();
                point[1] = (float) Math.random();
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

            BKmeans bkmeans = new BKmeans((int) Math.sqrt(matrix.getRowCount()));
            ArrayList<ArrayList<Integer>> clusters = bkmeans.execute(new Euclidean(), matrix);

            for (int i = 0; i < clusters.size(); i++) {
                int id = clusters.get(i).get(0) / 25;

                for (int j = 0; j < clusters.get(i).size(); j++) {
                    assertFalse("neighborhood wil defined.",
                            ((int) (clusters.get(i).get(j) / 25) != id));
                }
            }
        }
    }

    public void testExecute2() throws Exception {
        System.out.println("execute2 : null vectors");

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
                    if (esp > 0.75f) {
                        point[j] = (float) Math.random();
                    }
                }

                matrix.addRow(new SparseVector(point));
            }

            Dissimilarity diss = new Euclidean();
            if (t % 2 == 0) {
                diss = new CosineBased();
            }

            BKmeans bkmeans = new BKmeans((int) Math.sqrt(matrix.getRowCount()));
            ArrayList<ArrayList<Integer>> clusters = bkmeans.execute(diss, matrix);

            for (int i = 0; i < clusters.size(); i++) {
                boolean nullvector = ((clusters.get(i).get(0) / 50) == 0);

                for (int j = 0; j < clusters.get(i).size(); j++) {
                    if (nullvector) {
                        if ((clusters.get(i).get(j) / 50) != 0) {
                            System.out.println("diss: " + diss.toString());
                            System.out.println("distance: " + diss.calculate(matrix.getRow(i),
                                    matrix.getRow(clusters.get(i).get(j))));
                            assertFalse("Null vector on wrong cluster.", true);
                        }
                    } else {
                        if ((clusters.get(i).get(j) / 50) == 0) {
                            System.out.println("diss: " + diss.toString());
                            System.out.println("distance: " + diss.calculate(matrix.getRow(i),
                                    matrix.getRow(clusters.get(i).get(j))));
                            assertFalse("Non-null vector on wrong cluster.", true);
                        }
                    }
                }
            }
        }
    }
}
