/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.tools.apriori;

import junit.framework.TestCase;

/**
 *
 * @author Fernando Paulovich
 */
public class NLPToolsTest extends TestCase {
    
    public NLPToolsTest(String testName) {
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
     * Test of simplePhraseSpliter method, of class NLPTools.
     */
    public void testSimplePhraseSpliter() {
       String filecontent = "fernando h5n1 relógio calçado 1997 springer-verlag 123ff ff123";
        String[] simplePhraseSpliter = NLPTools.simplePhraseSpliter(filecontent);
        
        for(String p : simplePhraseSpliter) {
            System.out.println(p);
        }
    }

}
