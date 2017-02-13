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
    private boolean checked;
    private boolean expanded;
    private boolean path;
    private int weight;
    private int vcount;

    public Cell() {
        this.w = 1;
        this.totalCost = 0;
        this.weight = 0;
        this.vcount = 0;
        this.expanded = false;
        this.path = false;
        this.checked = false;
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

    //"[(Positon), weight, expanded, vcount, gCost, hCost, w, totalCost,}\n\t\t[("+
    @Override
    public String toString() {
        return "[X,Y:(" + x + "," + y + "), W:" + weight + ", E:" + expanded + ", VC:"+ vcount + ", G:"+ gCost + ", H:"+ hCost + ", W:"+ w + ", TC:"+ totalCost + "]";
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
    
    public void updateCost(double cost){
        this.totalCost = cost;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isSelected() {
        return path;
    }

    public void setSelected(boolean selected) {
        this.path = selected;
    }

    
    public double getCost(Cell prev, Cell goal, double search) {

        
        this.checked = true;
        if (weight != -1) {
            //check for movemnet:
            boolean digonal;
            digonal = !(prev.x == x || prev.y == y);
            double travelCost=0;
            //moving digonally:
            if (digonal) {
                //moving between two mud cells
                if (prev.weight == 1 && this.weight == 1) {
                    travelCost = sqrt(8);
                }
                //moving between 1 mud and blank
                else if (prev.weight == 1 || this.weight == 1) {
                    travelCost = (sqrt(2) + sqrt(8)) / 2;
                }
                else {
                    travelCost = sqrt(2);
                }
            } else {
                //moving between two mud cells
                if (prev.weight == 1 && this.weight == 1) {
                    travelCost = .25;
                }
                //moving between 1 mud and blank
                else if (prev.weight == 1 || this.weight == 1) {
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
                else{
                    travelCost = 1;
                }
            }
            gCost = prev.gCost + travelCost;
            if(weight == 5){
                gCost = 0;
            }
            
            int diffx = abs (goal.x - this.x);
            int diffy = abs (goal.y - this.y);
//            int diffxy = abs(diffx - diffy);
            hCost = (sqrt((diffx*diffx)+(diffy*diffy)));
            
            //if uniformed-cost serch
            if (search == -11){
                totalCost = hCost;
            } // if A*
            else if (search == -2){
                totalCost = gCost+hCost;
            } //if weighted A*
            else {
                totalCost = gCost + search * hCost;
            }
        }
        else {
            gCost = -1;
            hCost = -1;
            totalCost = -1;
        }
//        System.out.println("Cost: " + totalCost + ". Cell: " + this);
        return totalCost;
    }

   
}
