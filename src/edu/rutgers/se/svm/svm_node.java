package edu.rutgers.se.svm;

/**
 * 
 *  Reference from github -libsvm-kernel-master
 *
 */
public class svm_node implements java.io.Serializable {

    public Object data;

    public svm_node() {
    }

    public svm_node(Object data) {
        this.data = data;
    }        
}
