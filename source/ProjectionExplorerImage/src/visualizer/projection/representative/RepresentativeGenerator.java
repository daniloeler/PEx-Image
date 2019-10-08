/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.awt.Image;
import visualizer.matrix.Matrix;

/**
 *
 * @author wilson
 */
public interface RepresentativeGenerator {
    Image getRepresentative(Matrix selected, Matrix projection);
}
