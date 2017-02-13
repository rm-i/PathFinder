/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aipathfinder;

/**
 *
 * @author Ramya
 */
public class PQ {
    Cell cell;
    double cost;
    PQ next;
    
    PQ(Cell coord, double price, PQ child) {
        this.next = child;
        this.cost = price;
        this.cell = coord;
        
    }

    @Override
    public String toString() {
        return "PQ{" + "Cost= " + cost + ". Cell=" + cell + " next=\n\t" + next + '}';
    }
    
    
    
}
