/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aipathfinder;

import java.util.Random;
import processing.core.PApplet;

/**
 *
 * @author Ramya, Shahab
 */
public class AIPathFinder extends PApplet {

    public static void main(String[] args) {
        PApplet.main("aipathfinder.AIPathFinder");
    }

    int px = 0;
    int py = 0;
    private final int NUMROW = 40;
    private final int NUMCOL = 30;
    private final float MUDRAN = 31 / 4;
    private Cell[][] cells;

    float probabilityOfAliveAtStart = 80;
    float probabilityOfMudAtStart = 50;
    float probabilityOfRiverAtStart = (float) 99;

    int alive = color(255);
    int mud = color(174, 104, 11);
    int river = color(135, 206, 250);
    int dead = color(0);
    int red = color(255, 0, 0);
    int green = color(0, 255, 0);
    int checked = color(105, 105, 105);

    private boolean start = true;
    private boolean end = true;
    private int[] startloc = new int[4];
    private int[] endloc = new int[4];
    private int[] currentloc = new int[4];

    public void settings() {
        size(1000, 751);
    }

    public void setup() {
        // Instantiate arrays 
        cells = new Cell[NUMROW+1][NUMCOL+1];

        for (int i = 0; i <= NUMROW; i++) {
            for (int j = 0; j <= NUMCOL; j++) {
                cells[i][j] = new Cell();
                cells[i][j].setWeight(3);
                rect(i * 25, j * 25, 25, 25);
                //making blocked cells:
                int probBlank = (int) random(1, 6);
                if (probBlank == 2) {
                    cells[i][j].setWeight(-1);
                }
            }
        }
        //making the mud:
        int MUDTIMES = 2;
        while (MUDTIMES > 0) {
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
                    int probMud = (int) random(2, 4);
                    cells[i][j].setWeight(probMud);
                }
            }
            MUDTIMES--;
        }

        //making the water:
        int NUMWATER = 1;
        while (NUMWATER > 0) {
            boolean rejected = false;

            int xORy = (int) random(0, 2);
            int start;
            int riverLen = 0;
            if (xORy == 1) {
                start = (int) random(0, NUMCOL);
                for (int i = start; i < 5; i++) {
                    riverLen++;
                    if ((i < NUMCOL) && (riverLen < 25)) {
                        rejected = true;
                    }
                    cells[0][i].setWeight(1);
                }
            } else {
                start = (int) random(0, NUMROW);
                for (int i = start; i < 5; i++) {
                    riverLen++;
                    if ((i < NUMROW) && (riverLen < 25)) {
                        rejected = true;
                    }
                    cells[i][0].setWeight(1);
                }
            }

            if (!rejected) {
                NUMWATER--;
            }
        }
    }

    public boolean seen(int[] seen, int j) {
        boolean bool = false;
        for (int i = 0; i < seen.length; i++) {
            if (j == seen[i]) {
                bool = true;
            }
        }
        return bool;
    }

    public void draw() {
        //Draw grid
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 30; j++) {
                if (cells[i][j].getWeight() == -1) {
                    fill(dead); // If alive
                } else if (cells[i][j].getWeight() == 1) {
                    fill(river);
                } else if (cells[i][j].getWeight() == 2) {
                    fill(mud);
                } else {
                    fill(alive); // If dead
                }

                if (start) {
                    Random r = new Random();
                    if (i + j == r.nextInt(70) && cells[i][j].getWeight() > 2) {
                        fill(green);
                        startloc[0] = i;
                        startloc[1] = j;
                        startloc[2] = (i * 25) + (25 / 2);
                        startloc[3] = (j * 25) + (25 / 2);
                        if (i == 0) {
                            startloc[2] = (25 / 2);
                        }
                        if (j == 0) {
                            startloc[3] = (25 / 2);
                        }
                        start = false;
                    }
                } else if (end) {
                    Random r = new Random();
                    if (i + j == r.nextInt(70) && i != startloc[0] && j != startloc[1] && cells[i][j].getWeight() > 2) {
                        fill(red);
                        endloc[0] = i;
                        endloc[1] = j;
                        endloc[2] = (i * 25) + (25 / 2);
                        endloc[3] = (j * 25) + (25 / 2);
                        if (i == 0) {
                            endloc[2] = (25 / 2);
                        }
                        if (j == 0) {
                            endloc[3] = (25 / 2);
                        }
                        end = false;
                    }
                }
                rect(i * 25, j * 25, 25, 25);
            }
        }

        for (int i = 0; i < 40 * 2; i++) {
            for (int j = 0; j < 30 * 2; j++) {
                if (i % 2 != 0 && j % 2 != 0) {
                    fill(checked);
                    px = (i * 25) / 2;
                    py = (j * 25) / 2;
                    if (cells[i / 2][j / 2].getWeight() == -1) {
                        text("-1", px, py);
                    } else if (cells[i / 2][j / 2].getWeight() == 1) {
                        text("1", px, py);
                    } else if (cells[i / 2][j / 2].getWeight() == 2) {
                        text("2", px, py);
                    } else {
                        text("3", px, py);
                    }
                    //point(px,py);
                    strokeWeight(1);
                }
            }
        }
        System.out.println(startloc[0]+" "+startloc[1]+" "+endloc[0]+" "+endloc[1]);
        //System.out.println(startloc[2] + " " + startloc[3] + " " + endloc[2] + " " + endloc[3]);
        strokeWeight(3);
        currentloc[0] = startloc[0];
        currentloc[1] = startloc[1];
        currentloc[2] = startloc[2];
        currentloc[3] = startloc[3];
        int[] optimalloc = new int[4];
        int[] tloc = new int[4];
        int[][] np = nextPoints(currentloc);
        while (true) {
        	int i = (currentloc[2] - (25 / 2) / 25) / 25;
            int j = (currentloc[3] - (25 / 2) / 25) / 25;
            int diffx = abs(endloc[0] - i);
            int diffy = abs(endloc[1] - j);
            int odiffx = diffx;
            int odiffy = diffy;
            int tdiffx = diffx;
            int tdiffy = diffy;
            for (int k = 0; k < np.length; k++) {
            	if(k==0){
            		tloc[2] = currentloc[2];
                    tloc[3] = currentloc[3];
            	}
                if (currentloc[2] >= 0 && currentloc[3] >= 0 && currentloc[2] < 1000 && currentloc[3] < 751) {
                    currentloc[2] = np[k][0];
                    currentloc[3] = np[k][1];
                    i = (currentloc[2] - (25 / 2) / 25) / 25;
                    j = (currentloc[3] - (25 / 2) / 25) / 25;
                    cells[i][j].setVcount(cells[i][j].getVcount()+1);
                    diffx = abs(endloc[0] - i);
                    diffy = abs(endloc[1] - j);
                    System.out.println("oDiff X: "+odiffx+" oDiff Y: "+odiffy);
                    System.out.println("tDiff X: " + tdiffx + " tDiff Y: " + tdiffy);
                    System.out.println("Diff X: " + diffx + " Diff Y: " + diffy);
                    System.out.println("Weight: "+ cells[i][j].getWeight());
                    if (!(cells[i][j].getWeight()<0) && (cells[i][j].getVcount()<2) && !(startloc[2] == currentloc[2] && startloc[3] == currentloc[3]) && !(optimalloc[2] == tloc[2] && optimalloc[3] == tloc[3]) && !(diffx==tdiffx && diffy==tdiffy) && !(diffx==odiffx && diffy==odiffy) && (diffx<odiffx && diffy<odiffy) || (diffx>odiffx && diffy<odiffy && (abs(diffx-odiffx)==0 && abs(diffy-odiffy)==1||abs(diffx-odiffx)==1 && abs(diffy-odiffy)==0||abs(diffx-odiffx)==1 && abs(diffy-odiffy)==1)) || (diffx<odiffx && diffy>odiffy && (abs(diffx-odiffx)==0 && abs(diffy-odiffy)==1||abs(diffx-odiffx)==1 && abs(diffy-odiffy)==0||abs(diffx-odiffx)==1 && abs(diffy-odiffy)==1))||
                    		((diffx==odiffx)&&(diffy<odiffy))||((diffx<odiffx)&&(diffy==odiffy))){
                    	optimalloc[2]=currentloc[2];
                    	optimalloc[3]=currentloc[3];
                    	tdiffx=odiffx;
                    	tdiffy=odiffy;
                    	odiffx=diffx;
                    	odiffy=diffy;
                    	if(!(cells[i][j].getWeight()<0)){
                    		break;
                    	}
                    } else {
                    	if(k==np.length-1 && optimalloc[2] == tloc[2] && optimalloc[3] == tloc[3]){
                    		np = nextPoints(optimalloc);
                    	}
                        System.out.println("Didnt match anything!");
                    }
                    cells[i][j].setVcount(0);
                    //line(currentloc[2], currentloc[3], startloc[2], startloc[3]);
                    
                    System.out.println("Current X: " + i + " Current Y: " + j + "\n");
                }
            }
            int n = (optimalloc[2] - (25 / 2) / 25) / 25;
            int m = (optimalloc[3] - (25 / 2) / 25) / 25;
            int s1 = (startloc[2] - (25 / 2) / 25) / 25;
            int s2 = (startloc[3] - (25 / 2) / 25) / 25;
            int e1 = (endloc[2] - (25 / 2) / 25) / 25;
            int e2 = (endloc[3] - (25 / 2) / 25) / 25;
            int vcount = cells[n][m].getVcount();
            //System.out.println("VCount: " + vcount);
            cells[n][m].setVcount(vcount += 1);
            np = nextPoints(optimalloc);
            System.out.println("\n");
            System.out.println("Optimal X: " + n + " Optimal Y: " + m);
            System.out.println("Start X: " + s1 + " Start Y: " + s2);
            System.out.println("End X: " + e1 + " End Y: " + e2 + "\n");
            line(startloc[2], startloc[3], optimalloc[2], optimalloc[3]);
            i = (startloc[2] - (25 / 2) / 25) / 25;
            j = (startloc[3] - (25 / 2) / 25) / 25;
            if (startloc[2] == optimalloc[2] && startloc[3] == optimalloc[3] && cells[i][j].getVcount() > 1) {
                break;
            } else if (startloc[2] == optimalloc[2] && startloc[3] == optimalloc[3] && cells[i][j].getVcount() < 1) {
                np = nextPoints(optimalloc);
            }
            startloc[2] = optimalloc[2];
            startloc[3] = optimalloc[3];
            if (startloc[2] == endloc[2] && startloc[3] == endloc[3]) {
                break;
            }
        }
        System.out.println("End!");
        //line(startloc[2],startloc[3],endloc[2],endloc[3]);
        noLoop();
    }

    private int[][] nextPoints(int[] currentloc) {
        int[][] nextpoints = new int[8][2];
        int x = currentloc[2];
        int y = currentloc[3];
        int i = (x - (25 / 2) / 25) / 25;
        int j = (y - (25 / 2) / 25) / 25;
        int disp = 25;

        //Right
        if (i < NUMCOL-1) {
            System.out.println("Right: " + cells[i + 1][j]);
            if (cells[i + 1][j].getWeight() >= 0) {
                nextpoints[0][0] = x + disp;
                nextpoints[0][1] = y;
            } else {
                nextpoints[0][0] = x;
                nextpoints[0][1] = y;
            }
        }

        //Down
        if (j < NUMROW-1) {
            System.out.println("Down: " + cells[i][j + 1]);
            if (cells[i][j + 1].getWeight() >= 0) {
                nextpoints[1][0] = x;
                nextpoints[1][1] = y + disp;
            } else {
                nextpoints[1][0] = x;
                nextpoints[1][1] = y;
            }
        }

        //Left
        if (i > 0) {
            System.out.println("Left: " + cells[i - 1][j]);
            if (cells[i - 1][j].getWeight() >= 0) {
                nextpoints[2][0] = x - disp;
                nextpoints[2][1] = y;
            } else {
                nextpoints[2][0] = x;
                nextpoints[2][1] = y;
            }
        }

        //Up
        if (j > 0) {
            System.out.println("Up: " + cells[i][j - 1]);
            if (cells[i][j - 1].getWeight() >= 0) {
                nextpoints[3][0] = x;
                nextpoints[3][1] = y - disp;
            } else {
                nextpoints[3][0] = x;
                nextpoints[3][1] = y;
            }
        }

        //Down and Right
        if (i < NUMROW-1 && j < NUMCOL-1) {
            System.out.println("Down and Right: " + cells[i + 1][j + 1]);
            if (cells[i + 1][j + 1].getWeight() >= 0) {
                nextpoints[4][0] = x + disp;
                nextpoints[4][1] = y + disp;
            } else {
                nextpoints[4][0] = x;
                nextpoints[4][1] = y;
            }
        }

        //Down and Left
        if (i > 0 && j < NUMCOL-1) {
            System.out.println("Down and Left: " + cells[i - 1][j + 1]);
            if (cells[i - 1][j + 1].getWeight() >= 0) {
                nextpoints[5][0] = x - disp;
                nextpoints[5][1] = y + disp;
            } else {
                nextpoints[5][0] = x;
                nextpoints[5][1] = y;
            }
        }

        //Up and Right
        if (i < NUMROW-1 && j > 0) {
            System.out.println("Up and Right: " + cells[i + 1][j - 1]);
            if (cells[i + 1][j - 1].getWeight() >= 0) {
                nextpoints[6][0] = x + disp;
                nextpoints[6][1] = y - disp;
            } else {
                nextpoints[6][0] = x;
                nextpoints[6][1] = y;
            }
        }

        //Up and Left
        if (i > 0 && j > 0) {
            System.out.println("Up and Left: " + cells[i - 1][j - 1]);
            if (cells[i - 1][j - 1].getWeight() >= 0) {
                nextpoints[7][0] = x - disp;
                nextpoints[7][1] = y - disp;
            } else {
                nextpoints[7][0] = x;
                nextpoints[7][1] = y;
            }
        }
        System.out.println("\n");
        return nextpoints;
    }

}
