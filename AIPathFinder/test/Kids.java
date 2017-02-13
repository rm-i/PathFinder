/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static java.lang.Math.min;

/**
 *
 * @author Ramya
 */
public class Kids {

    private Cell theCell;
    private double cost;
    private Kids[] next;

    public Kids(int numKids) {
        this.next = new Kids[numKids];
        for (int i = 0; i < numKids; i++){
            next[i] = null;
        }
    }

    public Cell getTheCell() {
        return theCell;
    }

    public void setTheCell(Cell theCell) {
        this.theCell = theCell;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public void insert(Kids k){
        int i = 0;
        while (i < 8 && next[i] != null) {
            i++;
        }
        if (i >= 8 ){
            System.out.println("Error - Not added");
        }
        else{
            next[i] = k;
        }
    }
    
    public Kids getMinKid() {
        double minCost = -1;
        Kids temp = null;
        for (int i = 0; i < 8; i++) {
            if (next[i] != null) {
                if (minCost == -1) {
                    minCost = next[i].cost;
                    temp = next[i];
                } else if (minCost > next[i].cost) {
                    minCost = next[i].cost;
                    temp = next[i];
                }
            }
        }
        return temp;
    }

    public void printList(){
        System.out.print("THE LIST ___ \n");
        System.out.print("TotalCost: " + cost + ". Cell: " + theCell.toString() + "\n\t");
        for (int i = 0; i < 8; i++) {
            if (next[i] != null) {
                System.out.print("TotalCost: " + next[i].cost + ". Cell: " + next[i].theCell.toString() + "\n\t");
            }
            else if (i == 0){
                System.out.println("NO CHILDERN");
            }
        }
        System.out.println("__________");
    
    }
}
