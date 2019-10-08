/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.distance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;

/**
 *
 * @author user
 */
public class DistanceMatrixTest extends TestCase {

    private static final float DELTA = 0.00001f;
    
    public DistanceMatrixTest(String testName) {
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
     * Test of setDistance method, of class DistanceMatrix.
     */
    public void testDistanceMatrix1() {
        System.out.println("Creating the distance matrix.");

        for (int n = 0; n < 10; n++) {
            try {
                int DIM = 100;

                DenseMatrix matrix = new DenseMatrix();
                for (int i = 0; i < 500; i++) {
                    float[] vector = new float[DIM];

                    for (int j = 0; j < vector.length; j++) {
                        vector[j] = (float) Math.random();
                    }

                    matrix.addRow(new DenseVector(vector));
                }

                Dissimilarity diss = new CosineBased();
                if (n % 2 == 0) {
                    diss = new Euclidean();
                }

                DistanceMatrix dmat = new DistanceMatrix(matrix, diss);

                for (int i = 0; i < matrix.getRowCount(); i++) {
                    for (int j = 0; j < matrix.getRowCount(); j++) {
                        if (i == j) {
                            continue;
                        }

                        float dist = diss.calculate(matrix.getRow(i), matrix.getRow(j));
                        assertEquals(dist, dmat.getDistance(i, j), DELTA);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(DistanceMatrixTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void testDistanceMatrix2() {
        System.out.println("Creating the distance matrix.");

        for (int n = 0; n < 10; n++) {
            int DIM = 100;

            DenseMatrix matrix = new DenseMatrix();
            for (int i = 0; i < 500; i++) {
                float[] vector = new float[DIM];

                for (int j = 0; j < vector.length; j++) {
                    vector[j] = (float) Math.random();
                }

                matrix.addRow(new DenseVector(vector));
            }

            Dissimilarity diss = new CosineBased();
            if (n % 2 == 0) {
                diss = new Euclidean();
            }

            DistanceMatrix dmat = new DistanceMatrix(matrix.getRowCount());

            for (int i = 0; i < matrix.getRowCount(); i++) {
                for (int j = 0; j < matrix.getRowCount(); j++) {
                    float dist = diss.calculate(matrix.getRow(i), matrix.getRow(j));
                    dmat.setDistance(i, j, dist);
                }
            }

            for (int i = 0; i < matrix.getRowCount(); i++) {
                for (int j = 0; j < matrix.getRowCount(); j++) {
                    if (i == j) {
                        continue;
                    }

                    float dist = diss.calculate(matrix.getRow(i), matrix.getRow(j));
                    assertEquals(dist, dmat.getDistance(i, j), DELTA);
                }
            }
        }
    }

    public void testMinMax1() {
        System.out.println("Testing the max em minimum.");

        for (int n = 0; n < 10; n++) {
            try {
                int DIM = 100;

                DenseMatrix matrix = new DenseMatrix();
                for (int i = 0; i < 500; i++) {
                    float[] vector = new float[DIM];

                    for (int j = 0; j < vector.length; j++) {
                        vector[j] = (float) Math.random();
                    }

                    matrix.addRow(new DenseVector(vector));
                }

                Dissimilarity diss = new CosineBased();
                if (n % 2 == 0) {
                    diss = new Euclidean();
                }

                DistanceMatrix dmat = new DistanceMatrix(matrix, diss);

                float min = Float.POSITIVE_INFINITY;
                float max = Float.NEGATIVE_INFINITY;

                for (int i = 0; i < matrix.getRowCount(); i++) {
                    for (int j = 0; j < matrix.getRowCount(); j++) {
                        if (i == j) {
                            continue;
                        }

                        float dist = diss.calculate(matrix.getRow(i), matrix.getRow(j));

                        if (min > dist) {
                            min = dist;
                        }

                        if (max < dist) {
                            max = dist;
                        }
                    }
                }

                assertEquals(min, dmat.getMinDistance(), DELTA);
                assertEquals(max, dmat.getMaxDistance(), DELTA);

            } catch (IOException ex) {
                Logger.getLogger(DistanceMatrixTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void testMinMax2() {
        System.out.println("Testing the max em minimum.");

        for (int n = 0; n < 10; n++) {
            int DIM = 100;

            DenseMatrix matrix = new DenseMatrix();
            for (int i = 0; i < 500; i++) {
                float[] vector = new float[DIM];

                for (int j = 0; j < vector.length; j++) {
                    vector[j] = (float) Math.random();
                }

                matrix.addRow(new DenseVector(vector));
            }

            Dissimilarity diss = new CosineBased();
            if (n % 2 == 0) {
                diss = new Euclidean();
            }

            DistanceMatrix dmat = new DistanceMatrix(matrix.getRowCount());

            float min = Float.POSITIVE_INFINITY;
            float max = Float.NEGATIVE_INFINITY;

            for (int i = 0; i < matrix.getRowCount(); i++) {
                for (int j = 0; j < matrix.getRowCount(); j++) {
                    if (i == j) {
                        continue;
                    }

                    float dist = diss.calculate(matrix.getRow(i), matrix.getRow(j));
                    dmat.setDistance(i, j, dist);

                    if (min > dist) {
                        min = dist;
                    }

                    if (max < dist) {
                        max = dist;
                    }
                }
            }

            assertEquals(min, dmat.getMinDistance(), DELTA);
            assertEquals(max, dmat.getMaxDistance(), DELTA);
        }
    }

    public void testSaveLoad() {
        System.out.println("Testing saving/loading matrix.");

        String filename = "dmat.tmp";

        for (int n = 0; n < 10; n++) {
            try {
                int DIM = 100;

                DenseMatrix matrix = new DenseMatrix();
                for (int i = 0; i < 500; i++) {
                    float[] vector = new float[DIM];

                    for (int j = 0; j < vector.length; j++) {
                        vector[j] = (float) Math.random();
                    }

                    matrix.addRow(new DenseVector(vector));
                }

                Dissimilarity diss = new CosineBased();
                if (n % 2 == 0) {
                    diss = new Euclidean();
                }

                float[] cdata1 = new float[matrix.getRowCount()];
                for (int i = 0; i < cdata1.length; i++) {
                    cdata1[i] = i;
                }

                ArrayList<String> ids1 = new ArrayList<String>();
                for (int i = 0; i < cdata1.length; i++) {
                    ids1.add(Integer.toString(i));
                }

                DistanceMatrix dmat1 = new DistanceMatrix(matrix, diss);
                dmat1.setClassData(cdata1);
                dmat1.setIds(ids1);
                dmat1.save(filename);

                DistanceMatrix dmat2 = new DistanceMatrix(filename);
                float[] cdata2 = dmat2.getClassData();
                ArrayList<String> ids2 = dmat2.getIds();

                for (int i = 0; i < cdata2.length; i++) {
                    assertEquals(cdata1[i], cdata2[i], DELTA);
                }

                for (int i = 0; i < ids2.size(); i++) {
                    assertTrue(ids1.get(i).equals(ids2.get(i)));
                }

                for (int i = 0; i < matrix.getRowCount(); i++) {
                    for (int j = 0; j < matrix.getRowCount(); j++) {
                        if (i == j) {
                            continue;
                        }

                        assertEquals(dmat1.getDistance(i, j), dmat2.getDistance(i, j), DELTA);
                    }
                }

                assertEquals(dmat1.getMinDistance(), dmat2.getMinDistance(), DELTA);
                assertEquals(dmat1.getMaxDistance(), dmat2.getMaxDistance(), DELTA);

            } catch (IOException ex) {
                Logger.getLogger(DistanceMatrixTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

}
