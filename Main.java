import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;



public class Main {

    private static final String INPUT_FILE_NAME = "fence.in";
    private static final String OUTPUT_FILE_NAME = "fence.out";
    private static int[][] full = new int[1000000][2];
    private static StringBuffer sb;
    private static int length = 0;
    private static Set<Integer[]> pairs;
    private static Comparator<Integer[]> comp;
    
	public static void main(String[] args) {
		comp = new Comparator<Integer[]>() {

			@Override
			public int compare(Integer[] arg0, Integer[] arg1) {
				return arg0[0] - arg1[0];
			}
			
		};
		readData();
		outputData();
	}

	
    private static void readData() {
    	pairs = new TreeSet<>(comp);
    	sb = new StringBuffer();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(INPUT_FILE_NAME));
            char command;
            int a, b;
            while (true) {
            	command = scanner.next().charAt(0);
            	if (command == 'F') {
            		break;
            	}            	
            	if (command == 'L') {
            		a = scanner.nextInt();
            		b = scanner.nextInt();
            		fill(a, b);
            	}
            	else if (command == 'W') {
            		a = scanner.nextInt();
            		b = scanner.nextInt();
            		find(a, b);
            	}
            }
            
        } catch (Exception e) {}
    }

    private static void outputData() {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
            writer.append(sb.toString());
            writer.close();
        }
        catch (IOException e) {}
    }
    
    private static void fill(int a, int b) {
    	System.out.println("fill " + a + " - " + b);
    	int i = 0;
    	Integer[] curPair;
    	Iterator<Integer[]> iter = pairs.iterator();
    	int start = 0, end = 0;
    	int newStart = a, newEnd = b;
    	while (iter.hasNext()) {
    		curPair = iter.next();
    		if (start == 0 && curPair[1] >= a - 1) {
    			start = curPair[0];
    			if (start < a) {
    				newStart = start;
    			}
    		}
    		if (curPair[0] <= b + 1) {
    			end = curPair[1];
    			if (end > b) {
    				newEnd = end;
    			}
    		}
    	}
    	final int finalStart = start;
    	final int finalEnd = end;
    	if (start != 0 && end != 0) {
        	pairs.removeIf(x -> x[0] >= finalStart || (finalEnd == 0 ||  x[1] <= finalEnd));    		
    	}
    	pairs.add(new Integer[]{newStart, newEnd});
    	//print();
    }
    
    private static void print() {
    	Iterator<Integer[]> iter = pairs.iterator();
    	while (iter.hasNext()) {
    		Integer[] curPair = iter.next();
    		System.out.println(curPair[0] + " - " + curPair[1]);
    	}
    }
    
    private static void find(int a, int b) {
    	/*System.out.println(a + " - " + b);
    	for (int i = a - 1; i <= b - 1; i++) {
    		System.out.print(cells[i] + " ");
    	}
    	System.out.println("");
    	int bestFull = 0;
    	int bestEmpty = 0;
    	int curFull = 0;
    	int curEmpty = 0;
    	if (cells[a - 1] == 0) {
    		curEmpty = 1;
    	}
    	else {
    		curFull = 1;
    	}
    	for (int i = a; i <= b - 1; i++) {
    		if (cells[i] == cells[i - 1]) {
    			if (cells[i] == 0) {
    				curEmpty++;
    			}
    			else {
    				curFull++;
    			}
    		}
    		else {
    			if (cells[i - 1] == 0) {
    				if (bestEmpty < curEmpty) {
    					bestEmpty = curEmpty;
    				}
    				curEmpty = 0;
    				curFull = 1;
    			}
    			else {
    				if (bestFull < curFull) {
    					bestFull = curFull;
    				}
    				curFull = 0;
    				curEmpty = 1;
    			}
    		}
    	}
		if (bestFull < curFull) {
			bestFull = curFull;
		}
		if (bestEmpty < curEmpty) {
			bestEmpty = curEmpty;
		}    	
    	sb.append(bestEmpty + " " + bestFull + "\n");*/
    }
}
