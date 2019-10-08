/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.io.IOException;
import visualizer.matrix.Matrix;

/**
 *
 * @author wilson
 */
public interface BoxplotDataGenerator {    
    float[] generateLocalAnalysis(Matrix cluster) throws IOException;
    float[] generateGlobalAnalysis(Matrix selected, Matrix projection) throws IOException;
}
