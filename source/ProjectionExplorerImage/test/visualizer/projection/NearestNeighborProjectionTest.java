/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.distance.CosineBased;
import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Paulovich
 */
public class NearestNeighborProjectionTest extends TestCase {

    public NearestNeighborProjectionTest(String testName) {
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
     * Test of project method, of class NearestNeighborProjection.
     */
    public void testProject() {
        try {
            System.out.println("project");

            String filename = ".\\test\\data\\cbrilpirson.data";
            Matrix matrix = MatrixFactory.getInstance(filename);
            DistanceMatrix dmat = new DistanceMatrix(matrix, new CosineBased());            
            
            NearestNeighborProjection nnp = new NearestNeighborProjection();
            float[][] projection = nnp.project(dmat);
            
            for(int i=0; i < projection.length; i++) {
                assertTrue(projection[i][0] == projection[i][0]);
                assertTrue(projection[i][1] == projection[i][1]);
            }
        } catch (IOException ex) {
            Logger.getLogger(NearestNeighborProjectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
