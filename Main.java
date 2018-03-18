import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;



public class Main {

    private static final String INPUT_FILE_NAME = "fence.in";
    private static final String OUTPUT_FILE_NAME = "fence.out";
    private static StringBuffer sb;
    private static Set<Integer[]> pairs;
    private static Comparator<Integer[]> comp;
    /*private static int[] test = new int[1000000];
    private static int length = 0;
    private static int index;*/
    
	public static void main(String[] args) {
		comp = new Comparator<Integer[]>() {

			@Override
			public int compare(Integer[] arg0, Integer[] arg1) {
				return arg0[0] - arg1[0];
			}
			
		};
		/*for (int i = 0; i < 50; i++) {
			index = i + 1;
			generateRandomData();
			readData();			
		}*/
		readData();
		//System.out.println("end of tests");
		outputData();
	}
	
	/*private static void generateRandomData() {
		Random rand = new Random();
		int num = rand.nextInt(100) + 1;
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("test" + index + ".txt"));
            int a, b;
            for (int i = 0; i < num; i++) {
            	a = rand.nextInt(150) + 1;
            	b = a + rand.nextInt(70) + 1;
                writer.append("L " + a + " " + b + "\n");
            	a = rand.nextInt(150) + 1;
            	b = a + rand.nextInt(70) + 1;
                writer.append("W " + a + " " + b + "\n");
            }
            writer.append("F");
            writer.close();
        }
        catch (IOException e) {}
	}*/

	
    private static void readData() {
    	pairs = new TreeSet<>(comp);
    	sb = new StringBuffer();
        Scanner scanner = null;
        /*for (int i = 0; i < 1000000; i++) {
        	test[i] = 0;
        }*/
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
    	/*if (b > length) {
    		length = b;
    	}
    	for (int i = a - 1; i < b; i++) {
    		test[i] = 1;
    	}*/
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
    	//System.out.println(start + " " + end);
    	if (start != 0 && end != 0) {
        	pairs.removeIf(x -> (x[0] >= finalStart && x[1] <= finalEnd));    		
    	}
    	pairs.add(new Integer[]{newStart, newEnd});
    	//check();
    	//print();
    	//System.out.println("================");
    }
    
    /*private static void check() {
    	Iterator<Integer[]> iter = pairs.iterator();
    	while (iter.hasNext()) {
    		Integer[] curPair = iter.next();
    		for (int i = curPair[0] - 1; i < curPair[1]; i++) {
    			if (test[i] == 0) {
    				System.out.println("ASSERTION ERROR EXTRA FULL");
    				print();
    				break;
    			}
    			else {
    				test[i] = -1;
    			}
    		}
    	}
    	for (int i = 0; i < length; i++) {
    		if (test[i] == 1) {
				System.out.println("ASSERTION ERROR SKIPPED FULL");
				print();
				break;    			
    		}
    	}
    	
    	for (int i = 0; i < length; i++) {
    		if (test[i] == -1) {
    			test[i] = 1;
    		}
    	}
    }
    
    private static void print() {
    	System.out.println("index: " + index);
    	Iterator<Integer[]> iter = pairs.iterator();
    	while (iter.hasNext()) {
    		Integer[] curPair = iter.next();
    		System.out.println(curPair[0] + " - " + curPair[1]);
    	}
    	System.out.println("======================");
    }*/
    
    private static void find(int a, int b) {
		Integer[] curPair;
    	int bestFull = 0;
    	int bestEmpty = 0;
    	int curFull = 0;
    	int curEmpty = 0;
    	boolean someFullCellsProcessed = false;
    	Iterator<Integer[]> iter = pairs.iterator();    
    	Integer[] prevPair = new Integer[]{a, a};
    	while (iter.hasNext()) {
    		curPair = iter.next();
    		if (curPair[1] < a) {
    			continue;
    		}
    		if (curPair[0] > b) {
    			break;
    		}
    		curFull = Math.min(b, curPair[1]) - Math.max(curPair[0], a) + 1;
    		curEmpty = Math.max(curPair[0], a) - prevPair[1] - 1;
    		if (!someFullCellsProcessed) {
    			curEmpty++;
    		}
    		prevPair = curPair;
    		if (bestFull < curFull) {
    			bestFull = curFull;
    		}
    		if (bestEmpty < curEmpty) {
    			bestEmpty = curEmpty;
    		}
    		someFullCellsProcessed = true;
    	}
		curEmpty = b - prevPair[1];
		if (!someFullCellsProcessed) {
			curEmpty++;
		}
		if (curEmpty > bestEmpty) {
			bestEmpty = curEmpty;
		}
		/*int[] expected = findTest(a, b);
		if (bestFull != expected[0] || bestEmpty != expected[1]) {
			System.out.println("ASSERTION ERROR FOUND WRONG");
			System.out.println("W " + a + " " + b);
			System.out.println("actual: "+ bestFull + " - " + bestEmpty + ", expected: " + expected[0] + " - " + expected[1]);
			print();			
		}*/
    	//print();
    	sb.append(bestEmpty + " " + bestFull + "\n");
    }
    
    /*private static int[] findTest(int a, int b) {
    	int bestFull = 0;
    	int bestEmpty = 0;
    	int curFull = 0;
    	int curEmpty = 0;
    	if (test[a - 1] == 0) {
    		curEmpty = 1;
    	}
    	else {
    		curFull = 1;
    	}
    	for (int i = a; i <= b - 1; i++) {
    		if (test[i] == test[i - 1]) {
    			if (test[i] == 0) {
    				curEmpty++;
    			}
    			else {
    				curFull++;
    			}
    		}
    		else {
    			if (test[i - 1] == 0) {
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
		return new int[]{bestFull, bestEmpty};
    }*/
}
