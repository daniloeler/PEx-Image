/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.hybrid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.normalization.Normalization;
import visualizer.matrix.normalization.NormalizationFactory;
import visualizer.matrix.normalization.NormalizationType;
import visualizer.projection.ProjectionData;
import visualizer.projection.ProjectionFactory;
import visualizer.projection.ProjectionType;

/**
 *
 * @author DaniloEler
 */
public class Teste {
    public static void main(String args[]){
        try {
            DenseMatrix matrix = new DenseMatrix();
            matrix.load("F:\\iris.data");
//            
//            
//            ProjectionData pData = new ProjectionData();    //configurara a ProjectionData        
//            float projection[][] = ProjectionFactory.getInstance(ProjectionType.IDMAP).project(matrix, pData, null);
//            
//            
//            DenseMatrix dproj = new DenseMatrix();
//            ArrayList<String> att = new ArrayList<String>();
//            att.add("X");
//            att.add("Y");
//            dproj.setAttributes(att);
//            for (int i = 0; i < projection.length; i++) {
//                dproj.addRow(new DenseVector(projection[i]));
//            }
//            String filename = "arquivoDeSaida.data";
//            dproj.save(filename);
            
            
            
            
            System.out.println("Numero de instancias: "+matrix.getRowCount());
            System.out.println("Numero de atributos: " + matrix.getDimensions());
            for (int i=0; i<matrix.getRowCount(); i++){
                DenseVector v = (DenseVector) matrix.getRow(i);
                System.out.println(v.getValue(2));                
            }
            
            Normalization normColum= NormalizationFactory.getInstance(NormalizationType.NORMALIZE_COLUMNS);
            DenseMatrix normalizada = (DenseMatrix) normColum.execute(matrix);
            
            for (int i=0; i<normalizada.getRowCount(); i++){
                DenseVector v = (DenseVector) normalizada.getRow(i);
                System.out.println(v.getValue(2));                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
