package edu.rutgers.se.svm;

/**
 * <code>KernelManager</code> provides the custom kernel function to <code>svm</code>.
 *  Reference from github -libsvm-kernel-master
 */
public class KernelManager {
    static private CustomKernel customKernel;

    public static CustomKernel getCustomKernel() {
        return customKernel;
    }

    /**
     * Registers the custom kernel
     * @param customKernel 
     */
    public static void setCustomKernel(CustomKernel customKernel) {
        KernelManager.customKernel = customKernel;
    }
    
}
