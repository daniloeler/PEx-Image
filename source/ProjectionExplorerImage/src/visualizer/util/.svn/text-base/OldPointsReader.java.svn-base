/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class OldPointsReader {
    
    public OldPointsReader(String filename) throws java.io.IOException {
        java.util.Vector<java.util.Vector<Float> > points = new java.util.Vector<java.util.Vector<Float> >();
        this.attributes=new java.util.Vector<String>();
        
        BufferedReader in = null;
        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            String line=null;
            
            //Read the attributes
            while((line=in.readLine()) != null && line.trim().length() > 1 ) {
                //Ignore comments
                if(line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line,";");
                    
                    while(t.hasMoreTokens()) {
                        String token=t.nextToken();
                        this.attributes.add(token.trim());
                    }
                    break;
                }
            }
            
            while((line=in.readLine()) != null && line.trim().length() > 1 ) {
                //Ignore comments
                if(line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line,";");
                    
                    java.util.Vector<Float> point=new java.util.Vector<Float>();
                    
                    while(t.hasMoreTokens()) {
                        String token=t.nextToken();
                        point.add(Float.parseFloat(token));
                    }
                    points.add(point);
                }
            }
            
            this.points=new float[points.size()][];
            this.cdata=new float[points.size()];
            for(int i=0; i < points.size(); i++) {
                this.points[i]=new float[points.elementAt(i).size()-1];
                for(int j=0; j < points.elementAt(i).size()-1; j++) {
                    this.points[i][j]=points.elementAt(i).elementAt(j);
                }
                this.cdata[i]=points.elementAt(i).elementAt(points.elementAt(i).size()-1);
            }
            
            //Remove the class attribute
            int nR=this.attributes.size()-this.points[0].length;
            for(int i=0; i < nR; i++) {
                System.out.println(this.attributes.lastElement());
                
                this.attributes.remove(this.attributes.lastElement());
            }
            
        } catch (FileNotFoundException e) {
            throw new java.io.IOException("File " + filename + " does not exist!");
        } catch (java.io.IOException e) {
            throw new java.io.IOException("Problems reading the file " + filename + " : " + e.getMessage());
        } finally{
            //fechar o arquivo
            if(in != null){
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    public float[][] getPoints() {
        return this.points;
    }
    
    public float[] getCdata() {
        return this.cdata;
    }
    
    public java.util.Vector<String> getAttributes() {
        return this.attributes;
    }
    
    private float[][] points;
    private float[] cdata;
    private java.util.Vector<String> attributes;
}

