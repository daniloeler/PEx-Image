/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package visualizer.featureextraction;

/**
 *
 * @author Danilo
 */
import ij.ImagePlus;
        
abstract public class Feature {
	/** O objeto é protected, mas poderia ser public também, isso porque vou extender em cada um dos Extratores */
	protected double featuresExtractor[];
	protected String featuresNames[];

	/** Método que será abstraído em cada extrator implementado */
	abstract double[] extract(ImagePlus imp);

	/** Retorna um vetor com o nome das características referente a tal classe */
	public String[] getNames() {
		return featuresNames;
	}
}

