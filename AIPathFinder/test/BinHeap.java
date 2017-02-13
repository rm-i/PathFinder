///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package aipathfinder;
//
//import java.util.Arrays;
//import java.util.NoSuchElementException;
//
///**
// *
// * @author Ramya
// */
//public class BinHeap {
//
//    /**
//     * The number of children each node has *
//     */
//    private static final int SURROUNDINGCELLS = 8;
//    private int heapSize;
//    private final int[] cost;
//    private Cell[] theCell;
//     BinHeap next;
//
//    /**
//     * Constructor
//     *
//     * @param capacity *
//     */
//    public BinHeap(int capacity) {
//        heapSize = 0;
//        cost = new int[capacity];
//        theCell = new Cell[capacity];
//        next = null;
//        Arrays.fill(cost, -1);
//    }
//
//    /**
//     * Function to check if cost is empty
//     *
//     * @return *
//     */
//    public boolean isEmpty() {
//        return heapSize == 0;
//    }
//
//    /**
//     * Check if cost is full
//     *
//     * @return *
//     */
//    public boolean isFull() {
//        return heapSize == cost.length;
//    }
//
//    /**
//     * Clear cost
//     */
//    public void makeEmpty() {
//        heapSize = 0;
//    }
//
//    /**
//     * Function to get index parent of i *
//     */
//    private int parent(int i) {
//        return (i - 1) / SURROUNDINGCELLS;
//    }
//
//    /**
//     * Function to get index of k th child of i *
//     */
//    private int kthChild(int i, int k) {
//        return SURROUNDINGCELLS * i + k;
//    }
//
//    /**
//     * Function to insert element
//     *
//     * @param x
//     */
//    public void insert(int x, Cell c) {
//        if (isFull()) {
//            throw new NoSuchElementException("Overflow Exception");
//        }
//        /**
//         * Percolate up *
//         */
//        cost[heapSize++] = x;
//        theCell[heapSize++] = c;
//        heapifyUp(heapSize - 1);
//        
//        
//    }
//
//    /**
//     * Function to find least element
//     *
//     * @return *
//     */
//    public int findMinCost() {
//        if (isEmpty()) {
//            throw new NoSuchElementException("Underflow Exception");
//        }
//        return cost[0];
//    }
//    
//    public Cell findMinCell(){
//         if (isEmpty()) {
//            throw new NoSuchElementException("Underflow Exception");
//        }
//        return theCell[0];
//    }
//
//    /**
//     * Function to delete min element
//     *
//     * @return *
//     */
//    public int deleteMin() {
//        int keyItem = cost[0];
//        delete(0);
//        return keyItem;
//    }
//
//    /**
//     * Function to delete element at an index
//     *
//     * @param ind
//     * @return *
//     */
//    public int delete(int ind) {
//        if (isEmpty()) {
//            throw new NoSuchElementException("Underflow Exception");
//        }
//        int keyItem = cost[ind];
//        cost[ind] = cost[heapSize - 1];
//        heapSize--;
//        heapifyDown(ind);
//        return keyItem;
//    }
//
//    /**
//     * Function heapifyUp *
//     */
//    private void heapifyUp(int childInd) {
//        int tmp = cost[childInd];
//        while (childInd > 0 && tmp < cost[parent(childInd)]) {
//            cost[childInd] = cost[parent(childInd)];
//            childInd = parent(childInd);
//        }
//        cost[childInd] = tmp;
//    }
//
//    /**
//     * Function heapifyDown *
//     */
//    private void heapifyDown(int ind) {
//        int child;
//        int tmp = cost[ind];
//        while (kthChild(ind, 1) < heapSize) {
//            child = minChild(ind);
//            if (cost[child] < tmp) {
//                cost[ind] = cost[child];
//            } else {
//                break;
//            }
//            ind = child;
//        }
//        cost[ind] = tmp;
//    }
//
//    /**
//     * Function to get smallest child *
//     */
//    private int minChild(int ind) {
//        int bestChild = kthChild(ind, 1);
//        int k = 2;
//        int pos = kthChild(ind, k);
//        while ((k <= SURROUNDINGCELLS) && (pos < heapSize)) {
//            if (cost[pos] < cost[bestChild]) {
//                bestChild = pos;
//            }
//            pos = kthChild(ind, k++);
//        }
//        return bestChild;
//    }
//
//    /**
//     * Function to print cost *
//     */
//    public void printHeap() {
//        System.out.print("\nHeap = ");
//        for (int i = 0; i < heapSize; i++) {
//            System.out.print("TotalCost: " + cost[i] + ". Cell: " + theCell.toString() + "\n\t");
//            
//        }
//        System.out.println();
//    }
//}
