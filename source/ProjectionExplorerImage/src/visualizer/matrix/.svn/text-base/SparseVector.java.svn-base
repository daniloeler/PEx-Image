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

package visualizer.matrix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import visualizer.util.Pair;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class SparseVector extends Vector {

    public SparseVector(float[] vector) {
        this.create(vector, null, 0.0f);
    }

    public SparseVector(float[] vector, String id) {
        this.create(vector, id, 0.0f);
    }

    public SparseVector(float[] vector, float klass) {
        this.create(vector, null, klass);
    }

    public SparseVector(float[] vector, String id, float klass) {
        this.create(vector, id, klass);
    }

    public SparseVector(ArrayList<Pair> values, String id, float klass, int size) {
        assert (values != null) : "ERROR: vector can not be null!";

        this.id = id;
        this.klass = klass;
        this.size = size;

        this.index = new int[values.size()];
        this.values = new float[values.size()];

        int length = this.index.length;
        for (int i = 0; i < length; i++) {
            this.index[i] = values.get(i).index;
            this.values[i] = values.get(i).value;
        }

        this.updateNorm = true;
    }

    @Override
    public float dot(Vector vector) {
        assert (this.size == vector.size) : "ERROR: vectors of different sizes!";
        float dot = 0.0f;

        if (vector instanceof SparseVector) {
            int length = this.index.length;
            int vlength = ((SparseVector) vector).index.length;
            int[] vindex = ((SparseVector) vector).index;
            float[] vvalues = vector.values;

            if (length > 0 && vlength > 0 && index[0] <= vindex[vlength - 1]) {
                for (int i = 0, j = 0; i < length; i++) {
                    while (j + 1 <= vlength && vindex[j] < this.index[i]) {
                        j++;
                    }

                    if (j >= vlength) {
                        break;
                    } else if (this.index[i] == vindex[j]) {
                        dot += this.values[i] * vvalues[j];
                        j++;
                    }
                }
            }
        } else {
            int length = this.index.length;
            for (int i = 0; i < length; i++) {
                dot += this.values[i] * vector.values[this.index[i]];
            }
        }

        return dot;
    }

    @Override
    public void normalize() {
        assert (this.norm() > 0.0f) : "ERROR: it is not possible to normalize a null vector!";

        if (this.norm() > DELTA) {
            int length = this.values.length;
            for (int i = 0; i < length; i++) {
                values[i] = values[i] / this.norm();
            }

            this.norm = 1.0f;

        } else {
            this.norm = 0.0f;
        }
    }

    @Override
    protected void create(float[] vector, String id, float klass) {
        assert (vector != null) : "ERROR: vector can not be null!";

        this.id = id;
        this.klass = klass;
        this.size = vector.length;

        ArrayList<Integer> index_aux = new ArrayList<Integer>();
        ArrayList<Float> values_aux = new ArrayList<Float>();

        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > 0.0f) {
                index_aux.add(i);
                values_aux.add(vector[i]);
            }
        }

        this.index = new int[index_aux.size()];
        this.values = new float[values_aux.size()];

        int length = this.index.length;
        for (int i = 0; i < length; i++) {
            this.index[i] = index_aux.get(i);
            this.values[i] = values_aux.get(i);
        }

        this.updateNorm = true;
    }

    @Override
    protected void updateNorm() {
        this.norm = 0.0f;

        int length = this.values.length;
        for (int i = 0; i < length; i++) {
            this.norm += this.values[i] * this.values[i];
        }

        this.norm = (float) Math.sqrt(this.norm);
        this.updateNorm = false;
    }

    public float sparsity() {
        if (this.size > 0) {
            return 1.0f - ((float) this.index.length / (float) this.size);
        } else {
            return 1.0f;
        }
    }

    @Override
    public float[] toArray() {
        float[] vector = new float[this.size];
        Arrays.fill(vector, 0.0f);

        int length = this.index.length;
        for (int i = 0; i < length; i++) {
            vector[this.index[i]] = this.values[i];
        }

        return vector;
    }

    @Override
    public float getValue(int index) {
        assert (index < this.size) : "ERROR: index out of bounds!";

        for (int i = 0; i < this.index.length; i++) {
            if (this.index[i] == index) {
                return this.values[i];
            } else if (this.index[i] > index) {
                break;
            }
        }

        return 0.0f;
    }

    @Override
    public void setValue(int index, float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(BufferedWriter out) throws IOException {
        out.write(this.id);
        out.write(";");

        for (int i = 0; i < this.values.length; i++) {
            out.write(Integer.toString(this.index[i]));
            out.write(":");
            out.write(Float.toString(this.values[i]));
            out.write(";");
        }

        out.write(Float.toString(this.klass));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SparseVector clone = new SparseVector(new float[]{0});
        clone.norm = this.norm;
        clone.size = this.size;
        clone.id = this.id;
        clone.klass = this.klass;

        if (this.index != null) {
            clone.index = new int[this.index.length];
            System.arraycopy(this.index, 0, clone.index, 0, this.index.length);
        }

        if (this.values != null) {
            clone.values = new float[this.values.length];
            System.arraycopy(this.values, 0, clone.values, 0, this.values.length);
        }

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SparseVector) {
            SparseVector spv = ((SparseVector) obj);

            if (this.size != spv.size) {
                return false;
            }

            int length = this.index.length;
            int spvlength = spv.index.length;

            for (int i = 0; i < length; i++) {
                if (spvlength > i) {
                    if (this.index[i] != spv.index[i] ||
                            Math.abs(this.values[i] - spv.values[i]) > DELTA) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            return (Math.abs(spv.norm - this.norm) <= DELTA);
        } else if (obj instanceof DenseVector) {
            DenseVector dv = ((DenseVector) obj);

            if (this.size != dv.size) {
                return false;
            }

            float[] values_aux = this.values;

            for (int i = 0; i < values_aux.length; i++) {
                if (Math.abs(values_aux[i] - dv.values[i]) > DELTA) {
                    return false;
                }
            }

            return (Math.abs(dv.norm - this.norm) <= DELTA);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = 5 + (int) this.norm;
        result += 7 * size;
        result += 7 * (int) this.klass;
        result += 3 * this.id.hashCode();
        return result;
    }

    /**
     * This method it is only to be used if you know what you are doing.
     * @return The index of the values stored in this vector.
     */
    public int[] getIndex() {
        return index;
    }

    protected int[] index;
}
