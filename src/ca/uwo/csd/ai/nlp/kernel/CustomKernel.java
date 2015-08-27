package ca.uwo.csd.ai.nlp.kernel;

import ca.uwo.csd.ai.nlp.libsvm.svm_node;

/**
 * https://github.com/syeedibnfaiz/libsvm-java-kernel/
 * Interface for a custom kernel function
 * @author Syeed Ibn Faiz
 */
public interface CustomKernel {
    double evaluate(svm_node x, svm_node y);
}
