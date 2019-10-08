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

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class DenseVector extends Vector {

    public DenseVector(float[] vector) {
        this.create(vector, null, 0.0f);
    }

    public DenseVector(float[] vector, String id) {
        this.create(vector, id, 0.0f);
    }

    public DenseVector(float[] vector, float klass) {
        this.create(vector, null, klass);
    }

    public DenseVector(float[] vector, String id, float klass) {
        this.create(vector, id, klass);
    }

    @Override
    public float dot(Vector vector) {
        assert (this.size == vector.size) : "ERROR: vectors of different sizes!";

        float dot = 0.0f;

        if (vector instanceof DenseVector) {
            int length = this.values.length;
            for (int i = 0; i < length; i++) {
                dot += this.values[i] * vector.values[i];
            }
        } else {
            int length = ((SparseVector) vector).index.length;
            for (int i = 0; i < length; i++) {
                dot += this.values[((SparseVector) vector).index[i]] * vector.values[i];
            }
        }

        return dot;
    }

    @Override
    public void normalize() {
        assert (this.norm() != 0.0f) : "ERROR: it is not possible to normalize a null vector!";

        if (this.norm() > DELTA) {
            int length = this.values.length;
            for (int i = 0; i < length; i++) {
                this.values[i] = this.values[i] / this.norm();
            }
            this.norm = 1.0f;
        } else {
            this.norm = 0.0f;
        }
    }

    @Override
    public float[] toArray() {
        float[] array = new float[this.values.length];
        System.arraycopy(this.values, 0, array, 0, this.values.length);

        return array;
    }

    @Override
    public float getValue(int index) {
        assert (index <= this.size) : "ERROR: vector can not be null!";

        return this.values[index];
    }

    @Override
    public void setValue(int index, float value) {
        assert (index <= this.size) : "ERROR: vector can not be null!";

        this.updateNorm = true;
        this.values[index] = value;
    }

    @Override
    public void write(BufferedWriter out) throws IOException {
        out.write(this.id);
        out.write(";");

        for (int i = 0; i < this.values.length; i++) {
            out.write(Float.toString(this.values[i]));
            out.write(";");
        }

        out.write(Float.toString(this.klass));
    }

    @Override
    protected void create(float[] vector, String id, float klass) {
        assert (vector != null) : "ERROR: vector can not be null!";

        this.values = vector;
        this.size = vector.length;
        this.id = id;
        this.klass = klass;

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

    @Override
    public Object clone() throws CloneNotSupportedException {
        DenseVector clone = new DenseVector(new float[]{0});
        clone.norm = this.norm;
        clone.size = this.size;
        clone.id = this.id;
        clone.klass = this.klass;

        if (this.values != null) {
            clone.values = new float[this.values.length];
            System.arraycopy(this.values, 0, clone.values, 0, this.values.length);
        }

        return clone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DenseVector) {
            DenseVector dv = ((DenseVector) obj);

            if (this.size != dv.size) {
                return false;
            }

            for (int i = 0; i < this.values.length; i++) {
                if (Math.abs(this.values[i] - dv.values[i]) > DELTA) {
                    return false;
                }
            }

            return (Math.abs(dv.norm - this.norm) <= DELTA);

        } else if (obj instanceof SparseVector) {
            SparseVector sv = ((SparseVector) obj);

            if (this.size != sv.size) {
                return false;
            }

            float[] values_aux = sv.values;

            for (int i = 0; i < this.values.length; i++) {
                if (Math.abs(this.values[i] - values_aux[i]) > DELTA) {
                    return false;
                }
            }

            return (Math.abs(sv.norm - this.norm) <= DELTA);
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

}
