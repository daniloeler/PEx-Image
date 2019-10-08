/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.matrix;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author user
 */
public class SparseVectorTest extends TestCase {

    private static final float DELTA = 0.00001f;
    public SparseVectorTest(String testName) {
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
     * Test of dot method, of class SparseVector.
     */
    public void testdot() {
        System.out.println("dot");

        SparseVector svect1 = new SparseVector(new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 1});
        SparseVector svect2 = new SparseVector(new float[]{0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10});
        assertEquals(11, svect1.dot(svect2), DELTA);

        svect1 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        svect2 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertEquals(0, svect1.dot(svect2), DELTA);

        svect1 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0});
        svect2 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0});
        assertEquals(1, svect1.dot(svect2), DELTA);

        svect1 = new SparseVector(new float[]{1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 1});
        svect2 = new SparseVector(new float[]{1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0});
        assertEquals(26, svect1.dot(svect2), DELTA);

        svect1 = new SparseVector(new float[]{1, 0, 0, 0, 0, 0, 5, 4, 0, 0, 0, 0});
        svect2 = new SparseVector(new float[]{0, 1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 1});
        assertEquals(0, svect1.dot(svect2), DELTA);
    }

    /**
     * Test of normalize method, of class SparseVector.
     */
    public void testNormalize() {
        System.out.println("normalize");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect = new float[size];
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
//                if(n%10==0) vect[i]=0.0f;
//                else vect[i]=(Math.random() > threshold) ? (float)Math.random() : 0.0f;
                vect[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
            }

            SparseVector spvect = new SparseVector(vect);

            if (!spvect.isNull()) {
                spvect.normalize();
                assertEquals(((float) Math.sqrt(spvect.dot(spvect))), spvect.norm(), DELTA);
            }

            if (!spvect.isNull()) {
                assertEquals(spvect.size() + "_" + spvect.norm(), 1.0f, spvect.norm(), DELTA);
            }
        }
    }

    public void testSize() {
        System.out.println("size");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect = new float[size];
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
                if (n % 10 == 0) {
                    vect[i] = 0.0f;
                } else {
                    vect[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                }
            }

            SparseVector spvect = new SparseVector(vect);
            assertEquals(vect.length, spvect.size());
        }
    }

    public void testIsNull() {
        System.out.println("isNull");

        SparseVector svect1 = new SparseVector(new float[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 1});
        assertFalse(svect1.isNull());

        SparseVector svect2 = new SparseVector(new float[]{0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10});
        assertFalse(svect2.isNull());

        svect1 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertTrue(svect1.isNull());

        svect2 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertTrue(svect2.isNull());

        svect1 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0});
        assertFalse(svect1.isNull());

        svect2 = new SparseVector(new float[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0});
        assertFalse(svect2.isNull());

        svect1 = new SparseVector(new float[]{1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 1});
        assertFalse(svect1.isNull());

        svect2 = new SparseVector(new float[]{1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0});
        assertFalse(svect2.isNull());

        svect1 = new SparseVector(new float[]{1, 0, 0, 0, 0, 0, 5, 4, 0, 0, 0, 0});
        assertFalse(svect1.isNull());

        svect2 = new SparseVector(new float[]{0, 1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 1});
        assertFalse(svect2.isNull());
    }

    /**
     * Test of calculateNorm method, of class SparseVector.
     */
    public void testCalculateNorm() {
        System.out.println("calculateNorm");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect = new float[size];
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
                if (n % 10 == 0) {
                    vect[i] = 0.0f;
                } else {
                    vect[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                }
            }

            SparseVector spvect = new SparseVector(vect);
            assertEquals(((float) Math.sqrt(spvect.dot(spvect))), spvect.norm(), DELTA);
        }
    }

    /**
     * Test of sparsity method, of class SparseVector.
     */
    public void testSparsity() {
        System.out.println("sparsity");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect = new float[size];
            float nrElements = 0;
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
                float value = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                if (n % 10 == 0) {
                    vect[i] = 0.0f;
                } else {
                    vect[i] = value;
                }
                if (vect[i] > 0.0f) {
                    nrElements++;
                }
            }

            SparseVector spvect = new SparseVector(vect);
            assertEquals(1.0f - (nrElements / vect.length), spvect.sparsity(), DELTA);
        }
    }

    /**
     * Test of toArray method, of class SparseVector.
     */
    public void testToArray() {
        System.out.println("toArray");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect1 = new float[size];
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
                if (n % 10 == 0) {
                    vect1[i] = 0.0f;
                } else {
                    vect1[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                }
            }

            SparseVector spvect = new SparseVector(vect1);
            float[] vect2 = spvect.toArray();

            for (int i = 0; i < vect1.length; i++) {
                assertEquals(vect1[i], vect2[i], DELTA);
            }
        }
    }

    /**
     * Test of getValue method, of class SparseVector.
     */
    public void testGetValue() {
        System.out.println("getValue");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class SparseVector.
     */
    public void testSetValue() {
        System.out.println("setValue");

        for (int n = 0; n < 30; n++) {
            int size = (int) (10000 * Math.random());
            float[] vect1 = new float[size];
            Arrays.fill(vect1, 0.0f);
            
            SparseVector spvect = new SparseVector(vect1);

            for (int i = 0; i < size / 10; i++) {
                int index = (int) ((size - 1) * Math.random());
                vect1[index] = (float) Math.random();
                spvect.setValue(index, vect1[index]);
            }
            
            float[] vect2 = spvect.toArray();

            for (int i = 0; i < vect1.length; i++) {                
                assertEquals(vect1[i], vect2[i], DELTA);
            }
        }
    }

    /**
     * Test of write method, of class SparseVector.
     */
    public void testWrite() throws Exception {
        System.out.println("write");

        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class SparseVector.
     */
    public void testClone() throws Exception {
        System.out.println("clone");

        for (int n = 0; n < 30; n++) {
            int size = (int) (1000 * Math.random());
            float[] vect = new float[size];
            float threshold = (float) Math.random();
            for (int i = 0; i < size; i++) {
                if (n % 10 == 0) {
                    vect[i] = 0.0f;
                } else {
                    vect[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                }
            }

            SparseVector spvect = new SparseVector(vect, 19.0f);
            SparseVector clone = (SparseVector) spvect.clone();

            float[] spvect_v = spvect.toArray();
            float[] clone_v = clone.toArray();

            for (int i = 0; i < spvect_v.length; i++) {
                assertEquals(spvect_v[i], clone_v[i], DELTA);
            }

            assertEquals(spvect.norm(), clone.norm());
            assertEquals(spvect.size(), clone.size());
            assertEquals(spvect.getId(), clone.getId());
            assertEquals(spvect.getKlass(), clone.getKlass());

            assertNotSame("Clone is the same of original object!", spvect_v, clone_v);
        }
    }

    /**
     * Test of equals method, of class SparseVector.
     */
    public void testEquals() {
        System.out.println("equals");

        for (int n = 0; n < 30; n++) {
            try {
                int size = (int) (1000 * Math.random());
                float[] vect = new float[size];
                float threshold = (float) Math.random();
                for (int i = 0; i < size; i++) {
                    if (n % 10 == 0) {
                        vect[i] = 0.0f;
                    } else {
                        vect[i] = (Math.random() > threshold) ? (float) Math.random() : 0.0f;
                    }
                }

                SparseVector spvect = new SparseVector(vect);
                SparseVector clone = (SparseVector) spvect.clone();

                assertTrue("A SparseVector must be equals to its clone!", spvect.equals(clone));
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SparseVectorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
