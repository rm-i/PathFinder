/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aipathfinder;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 *
 * @author Ramya
 */
public class Cell {

    private int x;
    private int y;
    private double w;
    private double hCost;
    private double gCost;
    private double totalCost;
    private boolean expanded;
    private int weight;
    private int vcount;

    public Cell() {
        this.w = 1;
        this.totalCost = 0;
        this.weight = 0;
        this.vcount = 0;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;

    }

    public int getVcount() {
        return vcount;
    }

    public void setVcount(int vcount) {
        this.vcount = vcount;
    }

    @Override
    public String toString() {
        return "Cell [weight=" + weight + ", vcount=" + vcount + "]";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double gethCost() {
        return hCost;
    }

    public double getgCost() {
        return gCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public double getCost(Cell prev, Cell goal, int search) {

        this.expanded = true;
        
        if (weight == -1) {
            //check for movemnet:
            boolean digonal;
            digonal = !(prev.x == x || prev.y == y);
            double travelCost = -1;
            //moving digonally:
            if (digonal) {
                //moving between two mud cells
                if (prev.weight == 1 && this.weight == 1) {
                    travelCost = sqrt(8);
                }
                //moving between 1 mud and blank
                if ((prev.weight == 1 && this.weight == 3) || (prev.weight != 3 && this.weight == 1)) {
                    travelCost = (sqrt(2) + sqrt(8)) / 2;
                }
            } else {
                //moving between two mud cells
                if (prev.weight == 1 && this.weight == 1) {
                    travelCost = 2;
                }
                //moving between 1 mud and blank
                if ((prev.weight == 1 && this.weight == 3) || (prev.weight != 3 && this.weight == 1)) {
                    travelCost = 1.5;
                }
                //moving between two blank river cells
                if (prev.weight == 2 && this.weight == 2) {
                    travelCost = .25;

                }
                //moving between mud river and blank river
                if ((prev.weight == 4 && this.weight == 2) || (prev.weight == 2 && this.weight == 4)) {
                    travelCost = .375;
                }
                //moving between two mud river cells
                if (prev.weight == 4 && this.weight == 4) {
                    travelCost = .5;
                }
            }
            gCost = prev.gCost + travelCost;
            int diffx = abs (goal.x - this.x);
            int diffy = abs (goal.y = this.y);
            int diffxy = abs(diffx - diffy);
            hCost = sqrt(2) * min(diffx, diffy) + diffxy;
            
            //if uniformed-cost serch
            if (search == 1){
                totalCost = gCost;
            }
            if (search == 2){
                totalCost = gCost+hCost;
            }
            if (search == 3){
                totalCost = gCost + w * hCost;
            }
        }
        return totalCost;
    }
}
