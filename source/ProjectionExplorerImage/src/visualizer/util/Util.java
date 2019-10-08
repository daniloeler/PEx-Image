/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>, 
 * Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package visualizer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import visualizer.graph.Edge;
import visualizer.graph.Graph;
import visualizer.graph.Connectivity;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.matrix.Vector;
import visualizer.view.legend.Legend;
import visualizer.projection.SourceType;
import visualizer.view.color.ColorTable;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Util {

    /**
     * Import a graph connectivity from a file. The first line is the 
     * connectivity's name, and the remaining are the edges. Each line 
     * represents an edge and it is composed by tree fields, separated by a 
     * semicoloumn. The first two are the urls of the vertex linked by the 
     * edge and the third is the edge's lentgh.
     *  
     * @param filename The fie name.
     * @param vertex The vertex which composed this connectivity.
     * @return The read connectivity.
     * @throws IOException
     */
    public static Connectivity importConnectivity(ArrayList<Vertex> vertex,
            String filename) throws IOException {
        Connectivity con = null;

        //creating an index
        HashMap<String, Integer> index = new HashMap<String, Integer>();
        for (Vertex v : vertex) {
            index.put(v.getUrl(), (int) v.getId());
        }

        //creating the neighborhood
        ArrayList<ArrayList<Pair>> neigh_aux = new ArrayList<ArrayList<Pair>>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new java.io.FileReader(filename));

            //first line is the connectivity title
            String line = null;
            while ((line = in.readLine()) != null) {
                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    break;
                }
            }

            con = new Connectivity(line.trim());

            for (int i = 0; i < index.size(); i++) {
                neigh_aux.add(new ArrayList<Pair>());
            }

            //the remaining lines are the edges
            while ((line = in.readLine()) != null) {
                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    Integer from = index.get(t.nextToken());
                    Integer to = index.get(t.nextToken());
                    float len = Float.parseFloat(t.nextToken());

                    if (from != null && to != null) {
                        neigh_aux.get(from).add(new Pair(to, len));
                        neigh_aux.get(to).add(new Pair(from, len));
                    }
                }
            }

            Pair[][] neighborhood = new Pair[index.size()][];

            for (int i = 0; i < neigh_aux.size(); i++) {
                neighborhood[i] = new Pair[neigh_aux.get(i).size()];

                for (int j = 0; j < neigh_aux.get(i).size(); j++) {
                    neighborhood[i][j] = neigh_aux.get(i).get(j);
                }
            }

            //creating the connectivity with the read neighborhood
            con.create(vertex, neighborhood);

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            //fechar o arquivo
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return con;
    }

    /**
     * Export a graph connectivity to a file.
     * 
     * @param connectivity The connectivity to be exported.
     * @param filename The file name.
     * @throws IOException
     */
    public static void exportConnectivity(Connectivity connectivity,
            String filename) throws IOException {

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));

            //Writting the connectivity name
            out.write(connectivity.getName());
            out.write("\r\n");

            for (Edge e : connectivity.getEdges()) {
                out.write(e.getSource().getUrl());
                out.write(";");
                out.write(e.getTarget().getUrl());
                out.write(";");
                out.write(Float.toString(e.getLength()));
                out.write("\r\n");
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    //cdata;year
    //filename1.txt;1.3;0.70
    //filename2.txt;4.0;0.06
    //filename3.txt;6.7;0.40
    //filename4.txt;3.0;0.12
    //filename5.txt;8.9;0.11
    public static void importScalars(Graph graph, String filename) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            ArrayList<String> scalars = new ArrayList<String>();

            //Capturing the scalar names
            int linenumber = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                linenumber++;

                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    while (t.hasMoreTokens()) {
                        scalars.add(t.nextToken().trim());
                    }

                    break;
                }
            }

            //index for the vertex
            HashMap<String, Vertex> index = new HashMap<String, Vertex>();
            for (Vertex v : graph.getVertex()) {
                index.put(v.getUrl().trim(), v);
            }

            //reading the scalars
            while ((line = in.readLine()) != null) {
                linenumber++;
                ArrayList<Float> values = new ArrayList<Float>();

                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";", false);

                    //Capturing the filename
                    String fname = t.nextToken().trim();

                    //Capturing the scalar values
                    while (t.hasMoreTokens()) {
                        float value = Float.parseFloat(t.nextToken().trim());
                        values.add(value);
                    }

                    //checking the data
                    if (scalars.size() != values.size()) {
                        throw new IOException("The number of values for one scalar " +
                                "does not match with the number of declared scalars.\r\n" +
                                "Check line " + linenumber + " of the file.");
                    }

                    //Adding the scalar values to the vertex
                    Vertex v = index.get(fname);

                    if (v != null) {
                        for (int i = 0; i < scalars.size(); i++) {
                            Scalar s = graph.addScalar(scalars.get(i));
                            v.setScalar(s, values.get(i));
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void exportScalars(Graph graph, String filename) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));

            //writing the scalar names
            for (int i = 0; i < graph.getScalars().size(); i++) {
                if (!graph.getScalars().get(i).getName().equals(PExConstants.DOTS)) {
                    out.write(graph.getScalars().get(i).getName().replaceAll(";", "_"));
                    if (i < graph.getScalars().size() - 1) {
                        out.write(";");
                    }
                }
            }

            out.write("\r\n");

            //writing the scalar values
            for (Vertex v : graph.getVertex()) {
                if (v.isValid()) {
                    out.write(v.getUrl() + ";");

                    for (int i = 0; i < graph.getScalars().size(); i++) {
                        if (!graph.getScalars().get(i).getName().equals(PExConstants.DOTS)) {
                            float scalar = v.getScalar(graph.getScalars().get(i));
                            out.write(Float.toString(scalar).replaceAll(";", "_"));
                            if (i < graph.getScalars().size() - 1) {
                                out.write(";");
                            }
                        }
                    }

                    out.write("\r\n");
                }
            }

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static Graph importProjection(String filename) throws IOException {
        Graph graph = new Graph();

        Matrix matrix = MatrixFactory.getInstance(filename);

        Scalar sdots = graph.addScalar(PExConstants.DOTS);
        Scalar scdata = graph.addScalar(PExConstants.CDATA);

        int index = graph.addTitle("file name");

        ArrayList<Vertex> vertex = graph.getVertex();

        for (int id = 0; id < matrix.getRowCount(); id++) {
            Vector vec = matrix.getRow(id);

            Vertex v = new Vertex(id, vec.getValue(0), vec.getValue(1));
            v.setScalar(sdots, 0.0f);
            v.setScalar(scdata, vec.getKlass());
            v.setUrl(vec.getId());

            v.setTitle(index, vec.getId());
            vertex.add(v);
        }

        graph.setVertex(vertex);

        Connectivity dotsCon = new Connectivity(PExConstants.DOTS);
        graph.addConnectivity(dotsCon);

        graph.getProjectionData().setSourceFile(filename);
        graph.getProjectionData().setNumberObjects(vertex.size());
        graph.getProjectionData().setSourceType(SourceType.POINTS);

        return graph;
    }
    
    public static Matrix exportSelectedProjection(List<Vertex> vertex, Scalar scalar) throws IOException {
        Matrix matrix = new DenseMatrix();
                
        vertex.stream().filter((Vertex v)->(
            v.isSelected()
            )).forEach((Vertex v)-> { 
                addVertexRow(v, scalar, matrix);
        });
        
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("x");
        attributes.add("y");
        matrix.setAttributes(attributes);

        return matrix;
    }
    
    public static Matrix exportSelectedProjection(Graph graph, Scalar scalar) throws IOException {
        return exportSelectedProjection(graph.getVertex(), 
                scalar == null ? graph.getScalarByName(PExConstants.DOTS) : scalar);
    }

    public static Matrix exportProjection(Graph graph, Scalar scalar) throws IOException {
        Matrix matrix = new DenseMatrix();

        if (scalar == null) {
            scalar = graph.getScalarByName(PExConstants.DOTS);
        }

        for (int i = 0; i < graph.getVertex().size(); i++) 
            addVertexRow(graph.getVertex().get(i), scalar, matrix);           
       

        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("x");
        attributes.add("y");

        matrix.setAttributes(attributes);

        return matrix;
    }

    private static void addVertexRow(Vertex vertex, Scalar scalar, Matrix matrix) {
        float[] point = new float[2];
        point[0] = vertex.getX();
        point[1] = vertex.getY();
        
        float cdata = vertex.getScalar(scalar);
        String id = vertex.getUrl();
        
        matrix.addRow(new DenseVector(point, id, cdata));
    }

    public static void exportVTKFile(Graph graph, String filename,
            Connectivity connectivity) throws IOException {
        BufferedWriter out = null;

        //saving the VTK file
        try {
            out = new BufferedWriter(new FileWriter(filename));

            //Writting the file header
            out.write("# vtk DataFile Version 2.0\r\n");
            out.write("output\r\n");
            out.write("ASCII\r\n");
            out.write("DATASET POLYDATA\r\n");
            out.write("POINTS ");
            out.write(Integer.toString(graph.getVertex().size()));
            out.write(" float\r\n");

            //Writting the points coordinates
            for (Vertex v : graph.getVertex()) {
                Scalar s = graph.getScalarByName(PExConstants.CDATA);

                out.write(Float.toString(v.getX()));
                out.write(" ");
                out.write(Float.toString(v.getY()));
                out.write(" ");
                out.write(Float.toString(v.getScalar(s)));
                out.write("\r\n");
            }

            ArrayList<Edge> edges = connectivity.getEdges();

            //Writting the Delaunay triangulation
            int numberEdges = edges.size();
            out.write("\r\nLINES ");
            out.write(Integer.toString(numberEdges));
            out.write(" ");
            out.write(Integer.toString(numberEdges * 3));
            out.write("\r\n");
            for (Edge e : edges) {
                out.write("2 ");
                out.write(Long.toString(e.getSource().getId()));
                out.write(" ");
                out.write(Long.toString(e.getTarget().getId()));
                out.write("\r\n");
            }

            out.write("\r\nPOINT_DATA ");
            out.write(Integer.toString(graph.getVertex().size()));
            out.write("\r\n");

            //Writting the scalar values
            for (Scalar s : graph.getScalars()) {
                out.write("SCALARS ");
                out.write(s.getName().replace("\'", " ").trim().replace(" ", "+"));
                out.write(" float\r\n");
                out.write("LOOKUP_TABLE default\r\n");

                for (Vertex v : graph.getVertex()) {
                    out.write(Float.toString(v.getScalar(s)));
                    out.write("\r\n");
                }
                out.write("\r\n");
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File \"" + filename + "\" was not found!");
        } catch (IOException e) {
            throw new IOException("Problems reading the file \"" + filename + "\"");
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //saving the names file        
        try {
            out = new BufferedWriter(new FileWriter(filename + ".names"));
            for (Vertex v : graph.getVertex()) {
                String title = v.toString();
                if (title.length() > 125) {
                    title = title.substring(0, 124);
                }
                out.write(title);
                out.write("\r\n");
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File \"" + filename + ".names" + "\" was not found!");
        } catch (IOException e) {
            throw new IOException("Problems reading the file \"" + filename + ".names" + "\"");
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void exportTitles(Graph graph, String filename) throws IOException {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(filename));

            //Writting the names of the titles
            for (int i = 0; i < graph.getTitles().size() - 1; i++) {
                out.write(graph.getTitles().get(i).replaceAll(";", "_"));
                out.write(";");
            }

            out.write(graph.getTitles().get(graph.getTitles().size() - 1));
            out.write("\r\n");

            //getting the title indexes
            int[] titleIndex = new int[graph.getTitles().size()];
            for (int i = 0; i < titleIndex.length; i++) {
                titleIndex[i] = graph.getTitleIndex(graph.getTitles().get(i));
            }

            //writing the titles 
            for (Vertex v : graph.getVertex()) {
                if (v.isValid()) {
                    out.write(v.getUrl());
                    out.write(";");

                    for (int i = 0; i < titleIndex.length - 1; i++) {
                        v.changeTitle(titleIndex[i]);
                        out.write(v.toString().replaceAll(";", "_"));
                        out.write(";");
                    }

                    v.changeTitle(titleIndex[titleIndex.length - 1]);
                    out.write(v.toString().replaceAll(";", "_"));
                    out.write("\r\n");
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IOException("File \"" + filename + "\" was not found!");
        } catch (IOException ex) {
            throw new IOException("Problems writing the file \"" + filename + "\"");
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void importTitles(Graph graph, String filename) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            ArrayList<String> titles = new ArrayList<String>();

            //Capturing the titles names
            int linenumber = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                linenumber++;

                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    while (t.hasMoreTokens()) {
                        String title = t.nextToken().trim();
                        titles.add(title);
                    }

                    break;
                }
            }

            //reading the titles
            while ((line = in.readLine()) != null) {
                linenumber++;

                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");
                    ArrayList<String> values = new ArrayList<String>();

                    //Capturing the filename
                    String fname = t.nextToken().trim();

                    //Capturing the titles values
                    while (t.hasMoreTokens()) {
                        String title = t.nextToken().trim();
                        values.add(title);
                    }

                    //checking the data
                    if (titles.size() != values.size()) {
                        throw new IOException("The number of values for one scalar " +
                                "does not match with the number of declared scalars.\r\n" +
                                "Check line " + linenumber + " of the file.");
                    }

                    //Adding the scalar values to the vertex
                    for (Vertex v : graph.getVertex()) {
                        if (fname.equals(v.getUrl())) {
                            for (int i = 0; i < titles.size(); i++) {
                                int index = graph.addTitle(titles.get(i));
                                v.setTitle(index, values.get(i));
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public static void normalize(float[][] result) {
        int lvdimensions = result[0].length;
        int lvinstances = result.length;

        //for normalization
        float[] lvlowrange = new float[lvdimensions];
        float[] lvhighrange = new float[lvdimensions];

        //for each instance
        for (int lvins = 0; lvins < lvinstances; lvins++) {
            //for each attribute
            for (int lvfield = 0; lvfield < lvdimensions; lvfield++) {
                //if it is the first instance then assign the first value
                if (lvins == 0) {
                    lvlowrange[lvfield] = result[lvins][lvfield];
                    lvhighrange[lvfield] = result[lvins][lvfield];
                } //otherwise compare
                else {
                    lvlowrange[lvfield] = lvlowrange[lvfield] > result[lvins][lvfield] ? result[lvins][lvfield] : lvlowrange[lvfield];
                    lvhighrange[lvfield] = lvhighrange[lvfield] < result[lvins][lvfield] ? result[lvins][lvfield] : lvhighrange[lvfield];
                }
            }
        }

        //for each instance
        for (int lvins = 0; lvins < lvinstances; lvins++) {
            //for each attribute
            for (int lvfield = 0; lvfield < lvdimensions; lvfield++) {
                if ((lvhighrange[lvfield] - lvlowrange[lvfield]) > 0.0) {
                    result[lvins][lvfield] = (result[lvins][lvfield] - lvlowrange[lvfield]) /
                            (lvhighrange[lvfield] - lvlowrange[lvfield]);
                } else {
                    result[lvins][lvfield] = 0;
                }
            }
        }
    }

    public static int countFiles(String corpora) {
        int numberFiles = 0;

        //Capturing the filenames of the zip file
        ZipFile zip = null;
        try {
            zip = new ZipFile(corpora);
            Enumeration entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    numberFiles++;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return numberFiles;
    }

    public static float[][] transpose(float[][] matrix) {
        float[][] transpMatrix = new float[matrix[0].length][];

        for (int i = 0; i < matrix[0].length; i++) {
            transpMatrix[i] = new float[matrix.length];

            for (int j = 0; j < matrix.length; j++) {
                transpMatrix[i][j] = matrix[j][i];
            }
        }

        return transpMatrix;
    }

    public static int countObjectsDistanceFile(String filename) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            return Integer.parseInt(in.readLine());

        } catch (FileNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static int countObjectsPointsFile(String filename) throws IOException {
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));

            //read the header
            char[] header = in.readLine().trim().toCharArray();

            //checking
            if (header.length != 2) {
                throw new IOException("Wrong format of header information.");
            }

            //read the number of objects
            int nrobjs = Integer.parseInt(in.readLine());
            return nrobjs;

        } catch (FileNotFoundException ex) {
            throw new IOException(ex.getMessage());
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void savePrefuseGraph(Graph graph, String filename,
            Connectivity connectivity) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(filename));

            //Writting the file header
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<!--  An excerpt of an egocentric social network  -->\n");
            out.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n");
            out.write("<graph edgedefault=\"undirected\">");

            out.write("<!-- data schema -->\n");
            out.write("<key id=\"label\" for=\"node\" attr.name=\"label\" attr.type=\"string\"/>\n");
            out.write("<key id=\"cdata\" for=\"node\" attr.name=\"cdata\" attr.type=\"float\"/>\n");

            out.write("<!-- nodes -->\n");

            for (int i = 0; i <
                    graph.getVertex().size(); i++) {
                Vertex v = graph.getVertex().get(i);

                out.write("<node id=\"");
                out.write(Integer.toString(i));
                out.write("\">\n");
                out.write("<data key=\"label\">");

                if (v.isValid()) {
                    if (v.toString().length() <= 5) {
                        out.write(v.toString());
                    } else {
                        out.write(v.toString().substring(0, 5));
                    }

                } else {
                    out.write("_");
                }

                out.write("</data>\n");

                out.write("<data key=\"cdata\">");

                if (v.isValid()) {
                    Scalar s = graph.getScalarByName(PExConstants.CDATA);
                    out.write(Float.toString(v.getScalar(s)));
                } else {
                    out.write("-1.0");
                }

                out.write("</data>\n");
                out.write("</node>\n");
            }

            if (connectivity != null) {
                for (Edge e : connectivity.getEdges()) {
                    out.write("<edge source=\"" + Long.toString(e.getSource().getId()) +
                            "\" target=\"" + Long.toString(e.getTarget().getId()) + "\"></edge>\n");
                }

            }

            out.write("</graph>\n");
            out.write("</graphml>\n");

        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //fechar o arquivo
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Configures the system log.
     * @param console If true, the console log is activated, false it is not.
     * @param file If true, the file log is activated, false it is not.
     * @throws java.io.IOException
     */
    public static void log(boolean console, boolean file) throws IOException {
        String filename = "log%g.txt";
        int limit = 10000000; // 10 Mb

        int numLogFiles = 5;

        if (file) {
            FileHandler handler = new FileHandler(filename, limit, numLogFiles, true);
            handler.setFormatter(new SimpleFormatter());
//          handler.setFormatter(new XMLFormatter());
            handler.setLevel(Level.ALL);
            Logger.getLogger("").addHandler(handler);

            Logger.getLogger("Util").log(Level.INFO, "Adding the file logging.");
        } else {
            Logger.getLogger("Util").log(Level.INFO, "Removing the file logging.");

            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();

            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] instanceof FileHandler) {
                    rootLogger.removeHandler(handlers[i]);
                }
            }
        }

        //disable console log
        if (console) {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();

            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handlers[i]);
                }
            }

            rootLogger.addHandler(new ConsoleHandler());

            Logger.getLogger("Util").log(Level.INFO, "Adding console logging.");
        } else {
            Logger.getLogger("Util").log(Level.INFO, "Removing console logging.");

            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();

            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handlers[i]);
                }
            }
        }
    }

    public static Legend readLegend(ColorTable table, String filename) throws IOException {
        BufferedReader in = null;
        Legend legend = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));

            //capturing the legend title
            String title = in.readLine();

            //creating the legend
            legend = new Legend(table, title);

            //capturing the legend items
            String line = null;
            while ((line = in.readLine()) != null) {
                //ignore comments
                if (line.trim().length() > 0 && line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    String name = t.nextToken();
                    float value = Float.parseFloat(t.nextToken());

                    legend.addItem(name, value);
                }
            }
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return legend;
    }

    public static void unzipImages(String filename) {
        try {
            String unzipDir = SystemPropertiesManager.getInstance().getProperty("UNZIP.DIR");
            ZipFile zf = new ZipFile(filename);
            Enumeration zipEnum = zf.entries();
            while (zipEnum.hasMoreElements()) {
                ZipEntry item = (ZipEntry) zipEnum.nextElement();

                String newfile = item.getName();
                InputStream is = zf.getInputStream(item);
                FileOutputStream fos = new FileOutputStream(unzipDir + "\\" + newfile);
                int ch;
                while ((ch = is.read()) != -1) {
                    fos.write(ch);
                }
                is.close();
                fos.close();
            }
            zf.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void deleteFiles(String directory) {
        File dir = new File(directory);
        String[] files = dir.list();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File f = new File(directory + "\\" + files[i]);
                f.delete();
            }
        }
    }

    public static java.util.Vector<String> readOldNamesFile(String filename) throws java.io.IOException {
        java.util.Vector<String> elements = new java.util.Vector<String>();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            String line = null; //armazena a linha lida

            while ((line = in.readLine()) != null) {
                if (line.trim().length() > 0) {
                    elements.add(line.trim());
                }
            }

        } catch (FileNotFoundException e) {
            throw new java.io.IOException("File \"" + filename + "\" was not found!");
        } catch (IOException e) {
            throw new java.io.IOException("Problems reading the file \"" + filename + "\"");
        } finally {
            //fechar o arquivo
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (elements.size() > 0) {
            return elements;
        } else {
            return null;
        }
    }

    public static ArrayList<String> read(String filename) throws java.io.IOException {
        ArrayList<String> elements = new ArrayList<String>();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            String line = null; //armazena a linha lida

            while ((line = in.readLine()) != null) {
                if (line.trim().length() > 0) {
                    elements.add(line.trim());
                }
            }

        } catch (FileNotFoundException e) {
            throw new java.io.IOException("File \"" + filename + "\" was not found!");
        } catch (IOException e) {
            throw new java.io.IOException("Problems reading the file \"" + filename + "\"");
        } finally {
            //fechar o arquivo
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (elements.size() > 0) {
            return elements;
        } else {
            return null;
        }
    }
}
