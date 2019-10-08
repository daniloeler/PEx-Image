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
 * of the original code is Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Fernando Vieira Paulovich <fpaulovich@gmail.com>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.graph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import visualizer.corpus.Corpus;
import visualizer.corpus.zip.ZipCorpus;
import visualizer.dimensionreduction.DimensionalityReductionType;
import visualizer.projection.ProjectionType;
import visualizer.projection.ProjectorType;
import visualizer.projection.SourceType;
import visualizer.projection.distance.DissimilarityType;
import visualizer.projection.distance.kolmogorov.CompressorType;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class XMLGraphParser extends DefaultHandler {

    public Graph parse(String filename) throws IOException {
        graph = new Graph();

        SAXParserFactory spf = SAXParserFactory.newInstance();

        try {
            InputSource in = new InputSource(new InputStreamReader(new FileInputStream(filename), "ISO-8859-1"));
            SAXParser sp = spf.newSAXParser();
            sp.parse(in, this);
        } catch (SAXException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return graph;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equalsIgnoreCase(VERTEX)) {
            vertex.add(tempVertex);
        } else if (qName.equalsIgnoreCase(EDGES)) {
            edges = Connectivity.compress(edges);
            con.setEdges(edges);
            graph.addConnectivity(con);
        } else if (qName.equalsIgnoreCase(GRAPH)) {
            graph.setVertex(vertex);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equalsIgnoreCase(EDGE)) {
            long vertex1 = Long.parseLong(attributes.getValue(SOURCE));
            long vertex2 = Long.parseLong(attributes.getValue(TARGET));

            float length = Edge.NO_SIZE;
            String aux_len = attributes.getValue(LENGTH);
            if (aux_len != null) {
                length = Float.parseFloat(aux_len);
            }

            Vertex v1 = vertexIndex.get(vertex1);
            Vertex v2 = vertexIndex.get(vertex2);

            edges.add(new Edge(length, v1, v2));

        } else if (qName.equalsIgnoreCase(SCALAR)) {
            String name = attributes.getValue(NAME);
            String value = attributes.getValue(VALUE);

            if (name != null && value != null) {
                Scalar s = graph.addScalar(name);
                tempVertex.setScalar(s, Float.parseFloat(value));
            }
        } else if (qName.equalsIgnoreCase(LABEL)) {
            String name = attributes.getValue(NAME);
            String value = attributes.getValue(VALUE);

            if (name != null && value != null) {
                int index = graph.addTitle(name);
                tempVertex.setTitle(index, value);
            }
        } else if (qName.equalsIgnoreCase(VERTEX)) {
            String id = attributes.getValue(ID);
            tempVertex = new Vertex(Long.parseLong(id));
        } else if (qName.equalsIgnoreCase(VALID)) {
            String value = attributes.getValue(VALUE);
            if (value != null) {
                tempVertex.setValid(value.equals("1"));
            }
        } else if (qName.equalsIgnoreCase(X_COORDINATE)) {
            String value = attributes.getValue(VALUE);
            if (value != null) {
                tempVertex.setX(Float.parseFloat(value));
            }
        } else if (qName.equalsIgnoreCase(Y_COORDINATE)) {
            String value = attributes.getValue(VALUE);
            if (value != null) {
                tempVertex.setY(Float.parseFloat(value));
            }
        } else if (qName.equalsIgnoreCase(URL)) {
            String value = attributes.getValue(VALUE);
            if (value != null) {
                tempVertex.setUrl(value);
            }
        } else if (qName.equalsIgnoreCase(EDGES)) {
            vertexIndex = new HashMap<Long, Vertex>();

            for (Vertex v : vertex) {
                vertexIndex.put(v.getId(), v);
            }

            con = new Connectivity(attributes.getValue(NAME));
            edges = new ArrayList<Edge>();
        } else if (qName.equalsIgnoreCase(GRAPH)) {
            String description = attributes.getValue(DESCRIPTION);
            if (description != null && description.trim().length() > 0) {
                graph.setDescription(description);
            }

            vertex = new ArrayList<Vertex>();
        } else if (qName.equalsIgnoreCase(SOURCE_TYPE)) {
            String src_type = attributes.getValue(VALUE);
            if (src_type != null) {
                graph.getProjectionData().setSourceType(SourceType.retrieve(src_type));
            }
        } else if (qName.equalsIgnoreCase(PROJECTION_TECHNIQUE)) {
            String proj_tech = attributes.getValue(VALUE);
            if (proj_tech != null) {
                graph.getProjectionData().setProjectionType(ProjectionType.retrieve(proj_tech));
            }
        } else if (qName.equalsIgnoreCase(DISTANCE_TYPE)) {
            String dist_type = attributes.getValue(VALUE);
            if (dist_type != null) {
                graph.getProjectionData().setDissimilarityType(DissimilarityType.retrieve(dist_type));
            }
        } else if (qName.equalsIgnoreCase(SOURCE_FILE)) {
            String src_file = attributes.getValue(VALUE);
            if (src_file != null) {
                graph.getProjectionData().setSourceFile(src_file);

                if (src_file.startsWith("google/")) {
                    Corpus cp = new ZipCorpus(src_file, graph.getProjectionData().getNumberGrams());
                    graph.setCorpus(cp);
                }
            }
        } else if (qName.equalsIgnoreCase(NUMBER_ITERATIONS)) {
            String numb_it = attributes.getValue(VALUE);
            if (numb_it != null) {
                graph.getProjectionData().setNumberIterations(Integer.parseInt(numb_it));
            }
        } else if (qName.equalsIgnoreCase(FRACTION_DELTA)) {
            String frac_delta = attributes.getValue(VALUE);
            if (frac_delta != null) {
                graph.getProjectionData().setFractionDelta(Float.parseFloat(frac_delta));
            }
        } else if (qName.equalsIgnoreCase(PROJECTION_TYPE)) {
            String proj_type = attributes.getValue(VALUE);
            if (proj_type != null) {
                graph.getProjectionData().setProjectorType(ProjectorType.retrieve(proj_type));
            }
        } else if (qName.equalsIgnoreCase(LUHN_LOWER_CUT)) {
            String luhn_lw_cut = attributes.getValue(VALUE);
            if (luhn_lw_cut != null) {
                graph.getProjectionData().setLunhLowerCut(Integer.parseInt(luhn_lw_cut));
            }
        } else if (qName.equalsIgnoreCase(LUHN_UPPER_CUT)) {
            String luhn_up_cut = attributes.getValue(VALUE);
            if (luhn_up_cut != null) {
                graph.getProjectionData().setLunhUpperCut(Integer.parseInt(luhn_up_cut));
            }
        } else if (qName.equalsIgnoreCase(NUMBER_GRAMS)) {
            String numb_grams = attributes.getValue(VALUE);
            if (numb_grams != null) {
                graph.getProjectionData().setNumberGrams(Integer.parseInt(numb_grams));
            }
        } else if (qName.equalsIgnoreCase(COMPRESSOR_TYPE)) {
            String comp_type = attributes.getValue(VALUE);
            if (comp_type != null) {
                graph.getProjectionData().setCompressorType(CompressorType.retrieve(comp_type));
            }
        } else if (qName.equalsIgnoreCase(CLUSTER_FACTOR)) {
            String clust_factor = attributes.getValue(VALUE);
            if (clust_factor != null) {
                graph.getProjectionData().setClusterFactor(Float.parseFloat(clust_factor));
            }
        } else if (qName.equalsIgnoreCase(NUMBER_NEIGHBORS)) {
            String numb_neigh = attributes.getValue(VALUE);
            if (numb_neigh != null) {
                graph.getProjectionData().setNumberNeighborsConnection(Integer.parseInt(numb_neigh));
            }
        } else if (qName.equalsIgnoreCase(NUMBER_CONTROL_POINTS)) {
            String numb_cp = attributes.getValue(VALUE);
            if (numb_cp != null) {
                graph.getProjectionData().setNumberControlPoints(Integer.parseInt(numb_cp));
            }
        } else if (qName.equalsIgnoreCase(NUMBER_OBJECTS)) {
            String numb_objects = attributes.getValue(VALUE);
            if (numb_objects != null) {
                graph.getProjectionData().setNumberObjects(Integer.parseInt(numb_objects));
            }
        } else if (qName.equalsIgnoreCase(NUMBER_DIMENSIONS)) {
            String numb_dimen = attributes.getValue(VALUE);
            if (numb_dimen != null) {
                graph.getProjectionData().setNumberDimensions(Integer.parseInt(numb_dimen));
            }
        } else if (qName.equalsIgnoreCase(DIMEN_REDUCTION)) {
            String dimen_red = attributes.getValue(VALUE);
            if (dimen_red != null) {
                graph.getProjectionData().setDimensionReductionType(DimensionalityReductionType.retrieve(dimen_red));
            }
        } else if (qName.equalsIgnoreCase(RESULTING_DIMEN)) {
            String resul_dimen = attributes.getValue(VALUE);
            if (resul_dimen != null) {
                graph.getProjectionData().setTargetDimension(Integer.parseInt(resul_dimen));
            }
        }
    }

    private ArrayList<Vertex> vertex;
    private Vertex tempVertex;
    private Graph graph;
    private Connectivity con;
    private ArrayList<Edge> edges;
    private HashMap<Long, Vertex> vertexIndex;
    //graph
    private static final String GRAPH = "graph";
    private static final String DESCRIPTION = "description";
    private static final String VERTEX = "vertex";
    private static final String VALID = "valid";
    private static final String NAME = "name";
    private static final String LABEL = "label";
    private static final String ID = "id";
    private static final String X_COORDINATE = "x-coordinate";
    private static final String Y_COORDINATE = "y-coordinate";
    private static final String URL = "url";
    private static final String VALUE = "value";
    private static final String SCALAR = "scalar";
    private static final String EDGES = "edges";
    private static final String EDGE = "edge";
    private static final String SOURCE = "source";
    private static final String TARGET = "target";
    private static final String LENGTH = "length";
    //projection parameters
    private static final String SOURCE_TYPE = "source-type";
    private static final String PROJECTION_TECHNIQUE = "projection-technique";
    private static final String DISTANCE_TYPE = "distance-type";
    private static final String SOURCE_FILE = "source-file";
    private static final String NUMBER_ITERATIONS = "number-iterations";
    private static final String FRACTION_DELTA = "fraction-delta";
    private static final String PROJECTION_TYPE = "projection-type";
    private static final String LUHN_LOWER_CUT = "luhn-lower-cut";
    private static final String LUHN_UPPER_CUT = "luhn-upper-cut";
    private static final String NUMBER_GRAMS = "number-grams";
    private static final String COMPRESSOR_TYPE = "compressor-type";
    private static final String CLUSTER_FACTOR = "cluster-factor";
    private static final String NUMBER_NEIGHBORS = "number-neighbors";
    private static final String NUMBER_CONTROL_POINTS = "number-control-points";
    private static final String NUMBER_OBJECTS = "number-objects";
    private static final String NUMBER_DIMENSIONS = "number-dimensions";
    private static final String DIMEN_REDUCTION = "dimensionality-reduction";
    private static final String RESULTING_DIMEN = "resulting-dimensions";
}
