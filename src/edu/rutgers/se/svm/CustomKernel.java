package edu.rutgers.se.svm;


/**
 * Interface for a custom kernel function
 *  Reference from github -libsvm-kernel-master
 */
public interface CustomKernel {
    double evaluate(svm_node x, svm_node y);
}
