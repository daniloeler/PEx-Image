/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.io.IOException;
import visualizer.datamining.dataanalysis.NeighborhoodHit;
import visualizer.graph.Scalar;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;

/**
 *
 * @author wilson
 */
public class NeighborhoodHitClusterData implements BoxplotDataGenerator {
    private int nNeighbors;
    
    public NeighborhoodHitClusterData(int nNeighbors) {
        this.nNeighbors = nNeighbors;
    }

    @Override
    public float[] generateLocalAnalysis(Matrix cluster) throws IOException {
        return NeighborhoodHit.getNHitValues(nNeighbors-1, cluster, new DistanceMatrix(cluster, new Euclidean()));
    }
    
    @Override
    public String toString() {
        return "Neighborhood Hit";
    }

    @Override
    public float[] generateGlobalAnalysis(Matrix selected, Matrix projection) throws IOException {
        float[] nHitSelected = new float[selected.getRowCount()];
        
        float[] nHitProjection = NeighborhoodHit.getNHitValues(nNeighbors-1, projection, 
                        new DistanceMatrix(projection, new Euclidean()));
        
        
        int size = 0;
        System.out.println("OLHA A QUANDIDADE: "+nHitProjection.length);
        for( int i = 0; i < nHitProjection.length; ++i )
            for( int j = 0; j < selected.getRowCount(); ++j )
                if( selected.getRow(j).getId().equals(String.valueOf(i)) ) {
                    nHitSelected[size++] = nHitProjection[i];
                    break;
                }
            
        System.out.println("OLHA O TAMANHO DA GENERATEGLOBALANALYSIS (SC): "+size);
        return nHitSelected;
    }
    
    
}
