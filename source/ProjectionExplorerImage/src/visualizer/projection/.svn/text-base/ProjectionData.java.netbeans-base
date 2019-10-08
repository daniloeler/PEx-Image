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
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.projection;

import visualizer.dimensionreduction.DimensionalityReductionType;
import visualizer.matrix.normalization.NormalizationType;
import visualizer.projection.distance.DissimilarityType;
import visualizer.projection.distance.kolmogorov.CompressorType;
import visualizer.projection.lsp.ControlPointsType;
import visualizer.textprocessing.transformation.MatrixTransformationType;
import visualizer.textprocessing.stemmer.StemmerType;
import visualizer.datamining.clustering.HierarchicalClusteringType;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ProjectionData {

    public SourceType getSourceType() {
        return source;
    }

    public void setSourceType(SourceType source) {
        this.source = source;
    }

    public ProjectionType getProjectionType() {
        return this.projTech;
    }

    public void setProjectionType(ProjectionType projTech) {
        this.projTech = projTech;
    }

    public DissimilarityType getDissimilarityType() {
        return distanceType;
    }

    public void setDissimilarityType(DissimilarityType distanceType) {
        this.distanceType = distanceType;
    }

    public ProjectorType getProjectorType() {
        return this.projector;
    }

    public void setProjectorType(ProjectorType projector) {
        this.projector = projector;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public int getLunhLowerCut() {
        return lunhLowerCut;
    }

    public void setLunhLowerCut(int lunhLowerCut) {
        this.lunhLowerCut = lunhLowerCut;
    }

    public int getLunhUpperCut() {
        return lunhUpperCut;
    }

    public void setLunhUpperCut(int lunhUpperCut) {
        this.lunhUpperCut = lunhUpperCut;
    }

    public int getNumberLines() {
        return numberLines;
    }

    public void setNumberLines(int numberLines) {
        this.numberLines = numberLines;
    }

    public int getNumberIterations() {
        return numberIterations;
    }

    public void setNumberIterations(int numberIterations) {
        this.numberIterations = numberIterations;
    }

    public float getFractionDelta() {
        return fractionDelta;
    }

    public void setFractionDelta(float fractionDelta) {
        this.fractionDelta = fractionDelta;
    }

    public float getClusterFactor() {
        return clusterFactor;
    }

    public void setClusterFactor(float clusterFactor) {
        this.clusterFactor = clusterFactor;
    }

    public int getNumberGrams() {
        return numberGrams;
    }

    public void setNumberGrams(int numberGrams) {
        this.numberGrams = numberGrams;
    }

    public int getKnnNumberNeighbors() {
        return knnNumberNeighbors;
    }

    public void setKnnNumberNeighbors(int knnNumberNeighbors) {
        this.knnNumberNeighbors = knnNumberNeighbors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberNeighborsConnection() {
        return numberNeighborsConnection;
    }

    public void setNumberNeighborsConnection(int numberNeighborsConnection) {
        this.numberNeighborsConnection = numberNeighborsConnection;
    }

    public int getNumberControlPoints() {
        return numberControlPoint;
    }

    public void setNumberControlPoints(int numberControlPoint) {
        this.numberControlPoint = numberControlPoint;
    }

    public ControlPointsType getControlPointsChoice() {
        return controlPointsChoice;
    }

    public void setControlPointsChoice(ControlPointsType controlPointsChoice) {
        this.controlPointsChoice = controlPointsChoice;
    }

    public CompressorType getCompressorType() {
        return comptype;
    }

    public void setCompressorType(CompressorType comptype) {
        this.comptype = comptype;
    }

    public String getDistanceMatrixFilename() {
        return dmatFilename;
    }

    public void setDistanceMatrixFilename(String dmatFilename) {
        this.dmatFilename = dmatFilename;
    }

    public int getNumberObjects() {
        return numberObjects;
    }

    public void setNumberObjects(int numberObjects) {
        this.numberObjects = numberObjects;
    }

    public int getNumberDimensions() {
        return numberDimensions;
    }

    public void setNumberDimensions(int numberDimensions) {
        this.numberDimensions = numberDimensions;
    }

    public String getDocsTermsFilename() {
        return docsTermsFilename;
    }

    public void setDocsTermsFilename(String docsTermsFilename) {
        this.docsTermsFilename = docsTermsFilename;
    }

    public HierarchicalClusteringType getHierarchicalClusteringType() {
        return hierarchicalClusteringType;
    }

    public void setHierarchicalClusteringType(HierarchicalClusteringType hierarchicalClusteringType) {
        this.hierarchicalClusteringType = hierarchicalClusteringType;
    }

    public String getTitlesFile() {
        return titlesFile;
    }

    public void setTitlesFile(String titlesFile) {
        this.titlesFile = titlesFile;
    }

    public MatrixTransformationType getMatrixTransformationType() {
        return mattype;
    }

    public void setMatrixTransformationType(MatrixTransformationType mattype) {
        this.mattype = mattype;
    }

    public StemmerType getStemmer() {
        return stemmer;
    }

    public void setStemmer(StemmerType stemmer) {
        this.stemmer = stemmer;
    }

    public boolean isUseStopword() {
        return useStopword;
    }

    public boolean isUseWeight() {
        return useWeight;
    }

    public void setUseWeight(boolean useWeight) {
        this.useWeight = useWeight;
    }

    public void setUseStopword(boolean useStopword) {
        this.useStopword = useStopword;
    }

    public int getTargetDimension() {
        return targetDimension;
    }

    public void setTargetDimension(int targetDimension) {
        this.targetDimension = targetDimension;
    }

    public DimensionalityReductionType getDimensionReductionType() {
        return dimenType;
    }

    public void setDimensionReductionType(DimensionalityReductionType dimenType) {
        this.dimenType = dimenType;
    }

    public NormalizationType getNormalization() {
        return normalization;
    }

    public void setNormalization(NormalizationType normalization) {
        this.normalization = normalization;
    }

    public boolean isCreateDelaunay() {
        return createDelaunay;
    }

    public void setCreateDelaunay(boolean createDelaunay) {
        this.createDelaunay = createDelaunay;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ProjectionData pdata = new ProjectionData();
        pdata.source = this.source;
        pdata.projTech = this.projTech;
        pdata.distanceType = this.distanceType;
        pdata.sourceFile = this.sourceFile;
        pdata.titlesFile = this.titlesFile;
        pdata.numberIterations = this.numberIterations;
        pdata.fractionDelta = this.fractionDelta;
        pdata.projector = this.projector;
        pdata.knnNumberNeighbors = this.knnNumberNeighbors;
        pdata.description = this.description;
        pdata.hierarchicalClusteringType = this.hierarchicalClusteringType;
        pdata.dmatFilename = this.dmatFilename;
        pdata.docsTermsFilename = this.docsTermsFilename;
        pdata.lunhLowerCut = this.lunhLowerCut;
        pdata.lunhUpperCut = this.lunhUpperCut;
        pdata.numberLines = this.numberLines;
        pdata.numberGrams = this.numberGrams;
        pdata.mattype = this.mattype;
        pdata.comptype = this.comptype;
        pdata.clusterFactor = this.clusterFactor;
        pdata.numberNeighborsConnection = this.numberNeighborsConnection;
        pdata.numberControlPoint = this.numberControlPoint;
        pdata.controlPointsChoice = this.controlPointsChoice;
        pdata.numberObjects = this.numberObjects;
        pdata.numberDimensions = this.numberDimensions;
        pdata.useStopword = this.useStopword;
        pdata.targetDimension = this.targetDimension;
        pdata.useWeight = this.useWeight;
        pdata.dimenType = this.dimenType;
        pdata.normalization = this.normalization;
        pdata.createDelaunay = this.createDelaunay;
        pdata.stemmer = this.stemmer;

        return pdata;
    }

    //indicates the type of data used to create the projection
    private SourceType source = SourceType.NONE;
    //diss used to calculate distances over ArrayLists
    private DissimilarityType distanceType = DissimilarityType.NONE;
    //projection techqnique used
    private ProjectionType projTech = ProjectionType.NONE;
    //General use
    private String sourceFile = "";
    private String titlesFile = "";
    //Used to indicate the names of distance matrix/documents x terms matrix
    private String dmatFilename = "";
    private String docsTermsFilename = "";
    private int numberIterations = 50;
    private float fractionDelta = 8.0f;
    private ProjectorType projector = ProjectorType.NONE;
    private int knnNumberNeighbors = 2;
    private boolean createDelaunay = true;
    private String description = "";
    private HierarchicalClusteringType hierarchicalClusteringType = HierarchicalClusteringType.NONE;
    //Used only by the corpora pre-processing
    private int lunhLowerCut = 10;
    private int lunhUpperCut = -1;
    private int numberLines = 0;
    private int numberGrams = 1;
    private MatrixTransformationType mattype = MatrixTransformationType.TF_IDF;
    private StemmerType stemmer = StemmerType.PORTER;
    private boolean useStopword = true;
    private boolean useWeight = false;
    //Used only when NCD is used
    private CompressorType comptype = CompressorType.BZIP2;
    //Used only by projCus projectionTechnique
    private float clusterFactor = 4.5f;
    //Used only by LSP
    private int numberNeighborsConnection = 10;
    private int numberControlPoint = 10;
    private ControlPointsType controlPointsChoice = ControlPointsType.KMEDOIDS;
    //Extra information
    private int numberObjects = 0;
    private int numberDimensions = 0;
    //dimensionality reduction parameters
    private int targetDimension = 0; //target dimension to reduce
    private DimensionalityReductionType dimenType = DimensionalityReductionType.NONE;
    //normalization
    private NormalizationType normalization = NormalizationType.NONE;
}
