package sbu.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixMultiplication {

    static List<List<Integer>> resultMatrix;

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable {
        List<List<Integer>> tempMatrixProduct;
        List<List<Integer>> matrixX;
        List<List<Integer>> matrixY;
        int startRow;
        int startColumn;

        public BlockMultiplier(List<List<Integer>> matrixX, List<List<Integer>> matrixY, int startRow, int startColumn) {
            // TODO
            this.matrixX = matrixX;
            this.matrixY = matrixY;
            this.startRow = startRow;
            this.startColumn = startColumn;
        }

        @Override
        public void run() {
            /*
            TODO
                Perform the calculation and store the final values in tempMatrixProduct
            */

            int p = matrixX.size();
            int q = matrixY.size();
            int r = matrixY.getFirst().size();

            tempMatrixProduct = new ArrayList<>();

            for (int i = 0; i < p / 2; i++) {
                tempMatrixProduct.add(new ArrayList<>());
                for (int j = 0; j < r / 2; j++) {
                    tempMatrixProduct.get(i).add(0);
                }
            }

            for (int i = startRow; i < startRow + p / 2; i++) {
                for (int j = startColumn; j < startColumn + r / 2; j++) {
                    int value = 0;
                    for (int k = 0; k < q; k++) {
                        value += matrixX.get(i).get(k) * matrixY.get(k).get(j);
                    }
                    tempMatrixProduct.get(i - startRow).set(j - startColumn, value);
                }
            }
        }
    }

    /*
    Matrix A is of the form p x q
    Matrix B is of the form q x r
    both p and r are even numbers
    */
    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B) {
        /*
        TODO
            Parallelize the matrix multiplication by dividing tasks between 4 threads.
            Each thread should calculate one block of the final matrix product. Each block should be a quarter of the final matrix.
            Combine the 4 resulting blocks to create the final matrix product and return it.
         */

        int p = matrix_A.size();
        int q = matrix_B.size();
        int r = matrix_B.getFirst().size();

        BlockMultiplier[] blockMultipliers = new BlockMultiplier[4];
        blockMultipliers[0] = new BlockMultiplier(matrix_A, matrix_B, 0, 0);
        blockMultipliers[1] = new BlockMultiplier(matrix_A, matrix_B, 0, r / 2);
        blockMultipliers[2] = new BlockMultiplier(matrix_A, matrix_B, p / 2, 0);
        blockMultipliers[3] = new BlockMultiplier(matrix_A, matrix_B, p / 2, r / 2);

        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(blockMultipliers[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ie) {
                System.out.println(ie.getMessage());
            }
        }

        for (int i = 0; i < p / 2; i++) {
            blockMultipliers[0].tempMatrixProduct.get(i).addAll(blockMultipliers[1].tempMatrixProduct.get(i));
            blockMultipliers[2].tempMatrixProduct.get(i).addAll(blockMultipliers[3].tempMatrixProduct.get(i));
        }

        blockMultipliers[0].tempMatrixProduct.addAll(blockMultipliers[2].tempMatrixProduct);
        resultMatrix = blockMultipliers[0].tempMatrixProduct;

        return resultMatrix;
    }

    public static void main(String[] args) {
        // Test your code here
    }
}
