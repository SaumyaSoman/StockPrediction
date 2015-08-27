package ca.uwo.csd.ai.nlp.libsvm;

/**
 * 
https://github.com/syeedibnfaiz/libsvm-java-kernel/
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
