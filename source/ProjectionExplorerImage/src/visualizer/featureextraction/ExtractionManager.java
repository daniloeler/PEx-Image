/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visualizer.featureextraction;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import visualizer.projection.ProjectionData;

/**
 *
 * @author Danilo
 */
public class ExtractionManager {

    private float matrixPoints[][];
    private ArrayList<String> nameImages;
    private float[] nameClasses;
    private float[] cdata;

    public ExtractionManager(ProjectionData pdata, String pathname) {
        ImagePlus imp = null;
        Vector<File> filesTrainer = new Vector<File>();
        for (File f: new File(pathname).listFiles()){
            if (f.isFile()){
                filesTrainer.add(f);
            }
        }

        //ImagePlus vectorImagesOpened[] = new ImagePlus[filesTrainer.length];        
        Vector<ImagePlus> vectorImagesOpened = new Vector<ImagePlus>();
        int totalFeatures = PExImageFeatures.TOTAL_FEATURES;

        matrixPoints = new float[filesTrainer.size()][totalFeatures];
        int i, j;

        File directory = new File(pathname);

        try {
            nameImages = getNameImages(filesTrainer);
            // nameClasses = getClasses(filesTrainer);
            createCdata(filesTrainer);

            for (i = 0; i < filesTrainer.size(); i++) {                
                    imp = openImage(filesTrainer.get(i));
                    imp = imagePreProcessing(imp); //convert to gray
                    //imp.show();
                    vectorImagesOpened.add(imp);                
            }

            int indiceFeature = 0;

            Feature extractor = new PExImageFeatures();
            double result[] = null;

            System.out.println("\nExtracting PEx-Image Features");
            for (i = 0; i < vectorImagesOpened.size(); i++) {
                System.out.println("# Image " + (i + 1) + ":");
                ///extrair caracteristicas com os extratores selecionados pelo usuario
                result = extractor.extract(vectorImagesOpened.get(i));
                for (j = 0; j < result.length; j++) {
                    matrixPoints[i][j] = (float) result[j];
                }
            }

        //Util.normalize(matrixPoints);
                /*
        System.out.println("\nPrinting Matrix of Points");
        for (i = 0; i < matrixPoints.length; i++) {
        for (j = 0; j < matrixPoints[i].length; j++) {
        System.out.print(matrixPoints[i][j] + " ");
        }
        System.out.println();
        }     */
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public float[][] getMatrixPoints() {
        return matrixPoints;
    }

    public ArrayList<String> getNameImages() {
        return nameImages;
    }

    public float[] getClasses() {
        return nameClasses;
    }

    public float[] getClassData() {
        return this.cdata;
    }

    /** Pega o nome das imagens e armazena em um vetor de strings */
    private ArrayList<String> getNameImages(Vector<File> filesTrainer) {
        ArrayList<String> nameImages = new ArrayList<String>();

        for (int i = 0; i < filesTrainer.size(); i++) {            
            nameImages.add(filesTrainer.get(i).getName());            
        }

        return nameImages;
    }

    private void createCdata(Vector<File> filesTrainer) {
        if (filesTrainer.size() > 0) {
            //Capturing the initials
            ArrayList<String> initials_aux = new ArrayList<String>();
            for (int i = 0; i < filesTrainer.size(); i++) {
                
                    String filename = filesTrainer.get(i).getName();
                    //int begin=filename.lastIndexOf(System.getProperty("file.separator"));
                    int begin = filename.lastIndexOf("_");
                    String ini;
                    if (begin > -1) {
                        filename = filename.substring(0, begin);
                        ini = filename;
                    } else {
                        ini = filename;
                        if (filename.length() > 2) {
                            ini = filename.substring(0, 2);
                        }
                    }

                    //Create the initials with two letters
                    if (!initials_aux.contains(ini)) {
                        initials_aux.add(ini);
                    }                
            }

            String[] initials = new String[initials_aux.size()];
            for (int i = 0; i < initials_aux.size(); i++) {
                initials[i] = initials_aux.get(i);
            }

            Arrays.sort(initials);

            //Creating the cdata
            if (initials.length > 1) {
                this.cdata = new float[filesTrainer.size()];

                for (int i = 0; i < filesTrainer.size(); i++) {
                    this.cdata[i] = -1;
                    for (int j = 0; j < initials.length; j++) {

                        //Taking out the part of the file that correspond to the directory
                        String filename = filesTrainer.get(i).getName();
                        //int begin=filename.lastIndexOf(System.getProperty("file.separator"));
                        int begin = filename.lastIndexOf("_");
                        if (begin > -1) {
                            filename = filename.substring(0, begin);
                        }

                        //Given the cdata number
                        if (filename.startsWith(initials[j])) {
                            this.cdata[i] = j;
                        }
                    }
                }
            } else {
                this.cdata = new float[filesTrainer.size()];
                Arrays.fill(this.cdata, 0.0f);
            }
        }
    }

    /** Pega somente o nome da classe e armazena em um vetor de inteiros */
    private float[] getClasses(Vector<File> filesTrainer) {
        float[] nameClasses = new float[filesTrainer.size()];
        String[] temp;

        for (int i = 0; i < filesTrainer.size(); i++) {
            temp = filesTrainer.get(i).getName().split("_");
            nameClasses[i] = Float.parseFloat(temp[0].replaceFirst("classe", ""));
        }
        return nameClasses;
    }

    /** Realiza o pre-processamento das imagens */
    public ImagePlus imagePreProcessing(ImagePlus imp) {
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        return imp;
    }

    /** Abre as imagens */
    public ImagePlus openImage(File filesTrainer) {
        ImagePlus imp = null;
        // Abrindo as imagens do caminho de treinamento
        imp = IJ.openImage(filesTrainer.getPath());
        if (imp == null) {
            System.out.println("A imagem não pode ser aberta.");
        }
        return imp;
    }
}
