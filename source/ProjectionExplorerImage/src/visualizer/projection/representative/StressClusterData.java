/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.projection.representative;

import java.io.IOException;
import visualizer.datamining.dataanalysis.Stress;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Euclidean;

/**
 *
 * @author wilson
 */
public class StressClusterData implements BoxplotDataGenerator {
    private DistanceMatrix dmatdata;
    private DistanceMatrix dmatdataProjection;
    private Stress algorithm;
    
    public StressClusterData(DistanceMatrix dmatdataProjection, DistanceMatrix dmatdata, Stress algorithm) {
        this.dmatdata = dmatdata;
        this.algorithm = algorithm;
        this.dmatdataProjection = dmatdataProjection;
    }

    @Override
    public float[] generateLocalAnalysis(Matrix cluster) throws IOException {          
        return algorithm.calculateGroup(dmatdata, new DistanceMatrix(cluster, new Euclidean()));
    }
    
    @Override
    public String toString() {
        return "Stress";
    }

    @Override
    public float[] generateGlobalAnalysis(Matrix selected, Matrix projection) throws IOException {
        
        float[] stressSelected = new float[selected.getRowCount()];
        
        DistanceMatrix dmatprj = new DistanceMatrix(projection, new Euclidean());
        float[] values = algorithm.calculateGroup(dmatdataProjection, dmatprj);
        
        
        int size = 0;
        System.out.println("OLHA A QUANDIDADE: "+values.length);
        for( int i = 0; i < values.length; ++i )
            for( int j = 0; j < selected.getRowCount(); ++j )
                if( selected.getRow(j).getId().equals(String.valueOf(i)) ) {
                    stressSelected[size++] = values[i];
                    break;
                }
            
        System.out.println("OLHA O TAMANHO DA GENERATEGLOBALANALYSIS (SC): "+size);
        
        return stressSelected;
    }
}
