/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.matrix;

import junit.framework.TestCase;
import visualizer.projection.distance.CosineBased;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Paulovich
 */
public class SparseMatrixTest extends TestCase {

    public SparseMatrixTest(String testName) {
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
     * Test of addRow method, of class SparseMatrix.
     */
    public void testAddRow() {
        System.out.println("addRow");
        Vector vector = null;
        SparseMatrix instance = new SparseMatrix();
        instance.addRow(vector);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRow method, of class SparseMatrix.
     */
    public void testSetRow() {
        System.out.println("setRow");
        int index = 0;
        Vector vector = null;
        SparseMatrix instance = new SparseMatrix();
        instance.setRow(index, vector);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of load method, of class SparseMatrix.
     */
    public void testLoad() throws Exception {
        System.out.println("load");
        String filename = "";
        SparseMatrix instance = new SparseMatrix();
        instance.load(filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class SparseMatrix.
     */
    public void testSave() throws Exception {
        System.out.println("save");
        String filename = "";
        SparseMatrix instance = new SparseMatrix();
        instance.save(filename);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testEquality() throws Exception {
        System.out.println("equality between matrix and distance matrix");

        String dmatfilename = ".\\test\\data\\cbr-ilp-ir.dmat";
        DistanceMatrix dmat = new DistanceMatrix(dmatfilename);

        String matrixfilename = ".\\test\\data\\cbr-ilp-ir.data";
        Matrix matrix = MatrixFactory.getInstance(matrixfilename);

        Dissimilarity diss = new CosineBased();

        for (int i = 0; i < dmat.getElementCount(); i++) {
            for (int j = 0; j < dmat.getElementCount(); j++) {
                float dmatvalue = dmat.getDistance(i, j);
                float matrixvalue = diss.calculate(matrix.getRow(i), matrix.getRow(j));

                assertEquals(dmatvalue, matrixvalue, 0.00001f);
            }
        }
    }

}
