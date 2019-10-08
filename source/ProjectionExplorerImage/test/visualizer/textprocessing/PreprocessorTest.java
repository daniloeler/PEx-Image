/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.textprocessing;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;

/**
 *
 * @author Fernando Paulovich
 */
public class PreprocessorTest extends TestCase {

    public PreprocessorTest(String testName) {
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
     * Test of getMatrix method, of class Preprocessor.
     */
    public void testGetMatrix() throws Exception {
        System.out.println("getMatrix");
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMatrixSelected method, of class Preprocessor.
     */
    public void testGetMatrixSelected() throws Exception {
        System.out.println("getMatrixSelected");
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNgrams method, of class Preprocessor.
     */
    public void testGetNgrams() {
        System.out.println("getNgrams");
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNgramsAccordingTo method, of class Preprocessor.
     */
    public void testGetNgramsAccordingTo() throws Exception {
        System.out.println("getNgramsAccordingTo");
        ;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testRegularExpression() {
        System.out.println("testRegularExpression");

        String PATCOM = "[0-9]*[\\p{L}\\-]+[0-9]*[\\p{L}\\-]+[0-9]*";
        String filecontent = "fernando h5n1 relógio calçado 1997, springer-verlag 123ff ff123";
        Pattern pattern = Pattern.compile(PATCOM);
        Matcher matcher = pattern.matcher(filecontent);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
