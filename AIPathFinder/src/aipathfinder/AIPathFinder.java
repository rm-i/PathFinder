/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aipathfinder;

import processing.core.PApplet;

/**
 *
 * @author Ramya, Shahab
 */
public class AIPathFinder extends PApplet {

    public static void main(String[] args) {
        PApplet.main("aipathfinder.AIPathFinder");
    }

    //Used to resize and scale:
    private final int SCALEFACTOR = 1;
    private final int NUMROW = 160 / SCALEFACTOR;
    private final int NUMCOL = 120 / SCALEFACTOR;
    private final float MUDRAN = 31 / SCALEFACTOR;
    private final int WATERMOVE = 20 / SCALEFACTOR;
    private final int NUMWATER = 4 / SCALEFACTOR;
    private final int RIVERLEN = 100 / SCALEFACTOR;
    private final int MUDTIMES = 8 / SCALEFACTOR;
    private final int RECTSIZE = 6 * SCALEFACTOR;
    private final int LINESTROKEWEIGHT = 2 * SCALEFACTOR;

    //global variables:
    private final int[] OFFCSTART;
    private final int[] OFFCGOAL;
    private Cell STARTCELL;
    private Cell ENDCELL;
    private final Cell[][] CELLS;
    private boolean goalNotFound;
    private PQ headCheckedList;
    private PQ headExpandedList;
    private PQ headPathList;

    //color and state of cells:
    int dead0 = color(0);
    int river1 = color(135, 206, 250);
    int mud2 = color(174, 104, 11);
    int alive3 = color(255);
    int red5 = color(255, 0, 0);
    int green6 = color(0, 255, 0);
    int mudw7 = color(73, 0, 0);
    int check = color(80, 40, 160);
    int expanded = color(4, 160, 150);
    int pathColor = color(40, 160, 150);

    public AIPathFinder() {
        this.OFFCSTART = new int[2];
        this.OFFCGOAL = new int[2];
        this.CELLS = new Cell[NUMROW][NUMCOL];
        this.goalNotFound = true;
    }

    /**
     *
     */
    @Override
    public void settings() {
        size(NUMROW * RECTSIZE + RECTSIZE, NUMCOL * RECTSIZE + RECTSIZE);
    }

    /**
     *
     */
    @Override
    public void setup() {

        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                rect(i * RECTSIZE, j * RECTSIZE, RECTSIZE, RECTSIZE);
                CELLS[i][j] = new Cell();
                CELLS[i][j].setWeight(3);
                CELLS[i][j].setX(i);
                CELLS[i][j].setY(j);

                //making blocked cells:
                int probBlank = (int) random(1, 6);
                if (probBlank == 2) {
                    CELLS[i][j].setWeight(-1);
                }
            }
        }

        //making the mud2:
        int tempM = MUDTIMES;
        while (tempM > 0) {
            int ranX = (int) random(0, NUMROW);
            int ranY = (int) random(0, NUMCOL);
            int startX = (int) (ranX - MUDRAN / 2);
            int endX = (int) (ranX + MUDRAN / 2);
            int startY = (int) (ranY - MUDRAN / 2);
            int endY = (int) (ranY + MUDRAN / 2);
            if (startX < 0) {
                startX = 0;
            }
            if (endX > NUMROW - 1) {
                endX = NUMROW - 1;
            }
            if (startY < 0) {
                startY = 0;
            }
            if (endY > NUMCOL - 1) {
                endY = NUMCOL - 1;
            }
            for (int i = (int) startX; i <= (int) endX; i++) {
                for (int j = (int) startY; j <= (int) endY; j++) {
                    if (CELLS[i][j].getWeight() != -1) {
                        int probMud = (int) random(2, 4);
                        CELLS[i][j].setWeight(probMud);
                    }
                }
            }
            tempM--;
        }

        //making the water:
        for (int u = NUMWATER; u > 0; u--) {
            int[] start = new int[2];
            int xORy = (int) random(0, 4);
            switch (xORy) {
                case 0:
                    start[0] = 0;
                    start[1] = (int) random(0, NUMCOL);
                    break;
                case 1:
                    start[0] = (int) random(0, NUMROW);
                    start[1] = 0;
                    break;
                case 2:
                    start[0] = NUMROW - 1;
                    start[1] = (int) random(0, NUMCOL);
                    break;
                case 3:
                    start[0] = (int) random(0, NUMROW);
                    start[1] = NUMCOL - 1;
                    break;
                default:
                    break;
            }
            int len = RIVERLEN;
            while (len > 0) {
                boolean rejected = false;
                int[] endmov = new int[2];
                int hORv = (int) random(0, 4);
                switch (hORv) {
                    case 0:
                        endmov[0] = start[0] - WATERMOVE;
                        endmov[1] = start[1];
                        break;
                    case 1:
                        endmov[0] = start[0];
                        endmov[1] = start[1] - WATERMOVE;
                        break;
                    case 2:
                        endmov[0] = start[0] + WATERMOVE;
                        endmov[1] = start[1];
                        break;
                    case 3:
                        endmov[0] = start[0];
                        endmov[1] = start[1] + WATERMOVE;
                        break;
                    default:
                        break;
                }
                if (endmov[0] < 0 || endmov[0] >= NUMROW || endmov[1] < 0 || endmov[1] >= NUMCOL) {
                    rejected = true;
                }
//                else {
//               if (!rejected) {
//                   int biggerX = max(start[0], endmov[0]);
//                    int smallerX = min(start[0], endmov[0]);
//                    int biggerY = max(start[1], endmov[1]);
//                    int smallerY = min(start[1], endmov[1]);
//                    for (int i = smallerX; i <= biggerX; i++) {
//                        for (int j = smallerY; j <= biggerY; j++) {
//                            if (CELLS[i][j].getWeight() == 1) {
//                                rejected = true;
//                            }
//                        }
//                    }
//                }

                if (!rejected) {
                    int biggerX = max(start[0], endmov[0]);
                    int smallerX = min(start[0], endmov[0]);
                    int biggerY = max(start[1], endmov[1]);
                    int smallerY = min(start[1], endmov[1]);
                    for (int i = smallerX; i <= biggerX; i++) {
                        for (int j = smallerY; j <= biggerY; j++) {
                            if (CELLS[i][j].getWeight() == 2) {
                                CELLS[i][j].setWeight(7);
                            } else if(CELLS[i][j].getWeight() == 1){
                                len++;
                            } else {
                                CELLS[i][j].setWeight(1);
                            }
                            len--;
                        }
                    }
                    start = endmov;
                }
            }
        }
        //make goal:
        while (true) {
            int grX, grY;
            if ((int) random(0, 2) == 1) {
                grX = (int) random(0, 4);
            } else {
                grX = (int) random(NUMROW - 4, NUMROW);
            }
            if ((int) random(0, 2) == 1) {
                grY = (int) random(0, 4);
            } else {
                grY = (int) random(NUMCOL - 4, NUMCOL);
            }
            if (CELLS[grX][grY].getWeight() == 3 || CELLS[grX][grY].getWeight() == 2) {
                CELLS[grX][grY].setWeight(6);
                OFFCGOAL[0] = grX;
                OFFCGOAL[1] = grY;
                ENDCELL = CELLS[OFFCGOAL[0]][OFFCGOAL[1]];
                break;
            }
        }
        //make start
        while (true) {
            int grX, grY;
            if ((int) random(0, 2) == 1) {
                grX = (int) random(0, 4);
            } else {
                grX = (int) random(NUMROW - 4, NUMROW);
            }
            if ((int) random(0, 2) == 1) {
                grY = (int) random(0, 4);
            } else {
                grY = (int) random(NUMCOL - 4, NUMCOL);
            }
            if (CELLS[grX][grY].getWeight() == 3 || CELLS[grX][grY].getWeight() == 2) {
                CELLS[grX][grY].setWeight(5);
                OFFCSTART[0] = grX;
                OFFCSTART[1] = grY;
                STARTCELL = CELLS[OFFCSTART[0]][OFFCSTART[1]];
                break;
            }
        }
        System.out.println("_____END!_______");

        move();

    }

    /**
     *
     */
    public void draw() {
        //Draw grid
        for (int i = 0; i < NUMROW; i++) {
            for (int j = 0; j < NUMCOL; j++) {
                switch (CELLS[i][j].getWeight()) {
                    case -1:
                        fill(dead0);
                        break;
                    case 1:
                        fill(river1);
                        break;
                    case 2:
                        fill(mud2);
                        break;
                    case 3:
                        fill(alive3);
                        break;
                    case 5:
                        fill(green6);
                        break;
                    case 6:
                        fill(red5);
                        break;
                    case 7:
                        fill(mudw7);
                        break;
                    default:
                        break;
                }
                if (CELLS[i][j].isChecked() && (CELLS[i][j].getWeight() != 5 && CELLS[i][j].getWeight() != 6 && CELLS[i][j].getWeight() != -1)) {
                    fill(check);
                }
                if (CELLS[i][j].isExpanded() && (CELLS[i][j].getWeight() != 5 && CELLS[i][j].getWeight() != 6 && CELLS[i][j].getWeight() != -1)) {
                    fill(expanded);
                }
                if (CELLS[i][j].isSelected() && (CELLS[i][j].getWeight() != 5 && CELLS[i][j].getWeight() != 6 && CELLS[i][j].getWeight() != -1)) {
                    fill(pathColor);
                }

                strokeWeight(1);
                stroke(13, 33, 95);
                rect(i * RECTSIZE, j * RECTSIZE, RECTSIZE, RECTSIZE);

                strokeWeight(LINESTROKEWEIGHT);
                stroke(123, 34, 45);
                line(OFFCSTART[0] * RECTSIZE + (RECTSIZE / 2), OFFCSTART[1] * RECTSIZE + (RECTSIZE / 2), OFFCGOAL[0] * RECTSIZE + (RECTSIZE / 2), OFFCGOAL[1] * RECTSIZE + (RECTSIZE / 2));
            }
        }

    }

    public void move() {
        double cost = STARTCELL.getCost(STARTCELL, ENDCELL, 2);
        STARTCELL.setExpanded(true);

//        PQ headExpandedList = new PQ(STARTCELL, cost, null);
//        headExpandedList.cell = STARTCELL;
//        headExpandedList.cost = cost;
//        System.out.println("newItem:" +  headExpandedList);
        //add childern:
        Cell around = new Cell();
        Cell expandingCell = STARTCELL;

        //int y = 700;
        while (goalNotFound) {
            //y--;
         //   System.out.println("Goal: " + goalNotFound);
            if (inBound(expandingCell.getX() - 1, expandingCell.getY() - 1)) {
                around = CELLS[expandingCell.getX() - 1][expandingCell.getY() - 1];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX() - 1, expandingCell.getY())) {
                around = CELLS[expandingCell.getX() - 1][expandingCell.getY()];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX() - 1, expandingCell.getY() + 1)) {
                around = CELLS[expandingCell.getX() - 1][expandingCell.getY() + 1];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX(), expandingCell.getY() - 1)) {
                around = CELLS[expandingCell.getX()][expandingCell.getY() - 1];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX(), expandingCell.getY() + 1)) {
                around = CELLS[expandingCell.getX()][expandingCell.getY() + 1];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX() + 1, expandingCell.getY() - 1)) {
                around = CELLS[expandingCell.getX() + 1][expandingCell.getY() - 1];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX() + 1, expandingCell.getY())) {
                around = CELLS[expandingCell.getX() + 1][expandingCell.getY()];
                headCheckedList = checkKids(around, expandingCell);
            }
            if (inBound(expandingCell.getX() + 1, expandingCell.getY() + 1)) {
                around = CELLS[expandingCell.getX() + 1][expandingCell.getY() + 1];
                headCheckedList = checkKids(around, expandingCell);
            }

            if (headCheckedList != null) {
                if (headCheckedList.cell.getWeight() ==1){
            System.out.println("On water");
        }
        else if (headCheckedList.cell.getWeight() == 2){
            System.out.println("On mud");
        }
                headCheckedList.cell.setExpanded(true);
                PQ newItem = new PQ(headCheckedList.cell, headCheckedList.cost, null);
                headExpandedList = insertInOrder(headExpandedList, newItem);
                expandingCell = headCheckedList.cell;
                headCheckedList = deleteFisrt(headCheckedList);
//            System.out.println("Checked List:" + headCheckedList);
//            System.out.println("Expanded List:" + headExpandedList);

            } else {
                System.out.println("No PATH FOUND???");
                System.exit(0);
            }
        }
       // System.out.println("Goal: " + goalNotFound);
    }

    private int numKids(Cell c) {
        if ((c.getX() - 1 < 0) && (c.getY() - 1 < 0)) {
            return 3;
        } else if ((c.getX() + 1 >= NUMROW) && (c.getY() + 1 >= NUMCOL)) {
            return 3;
        } else if ((c.getX() - 1 < 0) && (c.getY() + 1 >= NUMCOL)) {
            return 3;
        } else if ((c.getX() + 1 >= NUMROW) && (c.getY() - 1 < 0)) {
            return 3;
        } else if ((c.getX() + 1 >= NUMROW)) {
            return 5;
        } else if ((c.getY() + 1 >= NUMCOL)) {
            return 5;
        } else if ((c.getX() - 1 < 0)) {
            return 5;
        } else if ((c.getY() - 1 < 0)) {
            return 5;
        }
        return 8;
    }

    private boolean inBound(int x, int y) {
        return !(x < 0 || x >= NUMROW || y < 0 || y >= NUMCOL);
    }

    private static PQ insertInOrder(PQ list, PQ toAdd) {
        PQ currentNode = list;
        //Add at front
        if (list == null) {
            list = toAdd;
            return list;
        } else if (list.cost >= toAdd.cost) {
            toAdd.next = list;
            return toAdd;
        }
        //Add in middle
        while (currentNode != null) {
            PQ prevNode = currentNode;
            currentNode = currentNode.next;
            if (currentNode != null && prevNode.cost <= toAdd.cost && currentNode.cost >= toAdd.cost) {
                prevNode.next = toAdd;
                toAdd.next = currentNode;
                return list;
            }
        }
        //Add at end
        currentNode = list;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        currentNode.next = toAdd;
        return list;
    }

    private static PQ deleteFisrt(PQ list) {
        return list.next;
    }

    private static boolean inList(PQ list, Cell c) {
        PQ currNode = list;

        while (currNode != null) {
            if (currNode.cell.getX() == c.getX() && currNode.cell.getY() == c.getY()) {
                return true;
            }
            currNode = currNode.next;
        }
        return false;
    }

    private static PQ changeCost(PQ list, Cell c, double cost) {
        PQ currNode = list;
        while (currNode != null) {
            if (currNode.cell.getX() == c.getX() && currNode.cell.getY() == c.getY()) {
                currNode.cell.updateCost(cost);
            }
            currNode = currNode.next;
        }
        return list;
    }

    public PQ checkKids(Cell around, Cell expandingCell) {
        double cost = around.getCost(expandingCell, ENDCELL, 4);
        if (around.getWeight() == 6) {
            goalNotFound = false;
            //temp.insert(cost, around);
        }  
        else if (cost != -1 && cost != 5) {
            if (inList(headExpandedList, around)) {
                changeCost(headExpandedList, around, cost);
            } else if (inList(headCheckedList, around)) {
                changeCost(headCheckedList, around, cost);
            } else {
                PQ newItem = new PQ(around, cost, null);
                headCheckedList = insertInOrder(headCheckedList, newItem);
            }

        }
        return headCheckedList;

    }

}
