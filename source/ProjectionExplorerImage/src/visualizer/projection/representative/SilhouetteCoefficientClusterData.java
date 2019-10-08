/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.io.IOException;
import visualizer.datamining.clustering.SilhouetteCoefficient;
import visualizer.graph.Scalar;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author wilson
 */
public class SilhouetteCoefficientClusterData implements BoxplotDataGenerator {
    private Dissimilarity diss;
    
    public SilhouetteCoefficientClusterData(Dissimilarity diss) {
        this.diss = diss;
    }    
    
    @Override
    public float[] generateLocalAnalysis(Matrix cluster) throws IOException {
        return new SilhouetteCoefficient().execute(cluster, diss);
    }
    
    @Override
    public String toString() {
        return "Silhouette Coefficient";
    }

    @Override
    public float[] generateGlobalAnalysis(Matrix selected, Matrix projection) throws IOException {
        float[] scSelected = new float[selected.getRowCount()];
        
        SilhouetteCoefficient sc = new SilhouetteCoefficient();
        float[] scProjection = sc.execute(projection, diss);
        
        int size = 0;
        System.out.println("OLHA A QUANDIDADE: "+scProjection.length);
        for( int i = 0; i < scProjection.length; ++i )
            for( int j = 0; j < selected.getRowCount(); ++j )
                if( selected.getRow(j).getId().equals(String.valueOf(i)) ) {
                    scSelected[size++] = scProjection[i];
                    break;
                }
            
        System.out.println("OLHA O TAMANHO DA GENERATEGLOBALANALYSIS (SC): "+size);
        
        return scSelected;
    }
}
