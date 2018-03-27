import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


class Tree {
	
	private class Node {
		private int start, end;
		private int maxFull, leftCellsUsed, rightCellsUsed;
		private int maxEmpty, leftCellsEmpty, rightCellsEmpty;
		
		public Node(int start, int end) {
			this.start = start;
			this.end = end;
			this.maxFull = 0;
			this.leftCellsUsed = 0;
			this.rightCellsUsed = 0;
			this.maxEmpty = end - start + 1;
		}		
		
		public void fill(int mf, int l, int r) {
			maxFull = mf;
			leftCellsUsed = l;
			rightCellsUsed = r;
		}
		
		@Override
		public String toString() {
			String res = "[" + start + "; " + end + "]";
			//if (start == end) {
				res += " (" + maxFull  + ", " + leftCellsUsed + ", " + rightCellsUsed + ")";
			//}
			return res;
		}
				
	}
	
	private Node[] data;
	//private final int N = 1048576; //так удобнее
	private final int N = 8;
	private final int length = 2 * N;
	private int curFull = 0, bestFull = 0;
	private int curEmpty = 0, bestEmpty = 0;
	
	
	public Tree() {
		data = new Node[length];
		addNode(1, N, 1);
	}
	
	private void addNode(int start, int end, int index) {
		data[index] = new Node(start, end);
		if (start < end) {
			int middle = (start + end) / 2;
			addNode(start, middle, 2 * index);
			addNode(middle + 1, end, 2 * index + 1);			
		}
	}
	
	public void fill(int a, int b) {
		//System.out.println("fill " + a + " " + b);
		int start = N + a - 1;
		int end = N + b - 1;
		for (int i = start; i <= end; i++) {
			data[i].fill(1, 1, 1);
		}
		start /= 2;
		end /= 2;
		
		int mf, l, r;
		Node leftChild, rightChild;
		
		while (start > 0 && end > 0) {
			for (int i = start; i <= end; i++) {
				leftChild = data[2 * i];
				rightChild = data[2 * i + 1];
				mf = Math.max(Math.max(leftChild.maxFull, rightChild.maxFull),  leftChild.rightCellsUsed + rightChild.leftCellsUsed);
				l = leftChild.leftCellsUsed;
				r = rightChild.rightCellsUsed;
				if (leftChild.leftCellsUsed == leftChild.end - leftChild.start + 1) {
					l += rightChild.leftCellsUsed;
				}
				if (rightChild.rightCellsUsed == rightChild.end - rightChild.start + 1) {
					r += leftChild.rightCellsUsed;
				}
				data[i].fill(mf, l, r);
			}
			start /= 2;
			end /= 2;
		}
		
		//debugPrint();
	}
		
	public int[] findFullAndEmpty(int a, int b) {
		//System.out.println("W " + a + " " + b);
		curFull = 0;
		bestFull = 0;
		curEmpty = 0;
		bestEmpty = 0;
		bestFull = findHelper(a, b, 1)[0];
		return new int[]{bestEmpty, bestFull};
	}
	
	private int[] findHelper(int a, int b, int index) {
		//System.out.println("find [" + a + "; " + b + "] in [" + data[index].start + "; " + data[index].end + "]");
		//здесь на входе [a; b] содержится в [data[index].start; data[index].end]
		if (a == data[index].start && b == data[index].end) {
			return new int[]{data[index].maxFull, data[index].leftCellsUsed, data[index].rightCellsUsed};
		}
		int middle = (data[index].start + data[index].end) / 2;
		if (b <= middle) {
			return findHelper(a, b, 2 * index);
		}
		if (a > middle) {
			return findHelper(a, b, 2 * index + 1);
		}
		
		//надо разбить отрезок
		int[] left = findHelper(a, middle, 2 * index);
		int[] right = findHelper(middle + 1, b, 2 * index + 1);
		int mf, l, r;
		
		mf = Math.max(Math.max(left[0], right[0]), left[2] + right[1]);
		l = left[1];
		r = right[2];
		if (l == middle - a + 1) {
			l += right[1];
		}
		if (r == b - middle) {
			r += left[2];
		}
		
		return new int[]{mf, l, r};
	}

	public void debugPrint() {
		int bound = 1;
		int cur = 1;
		for (int i = 1; i < length; i++) {
			System.out.print(data[i] + " ");
			if (bound == cur) {
				bound *= 2;
				cur = 0;
				System.out.println("");
			}
			cur++;
		}
	}
	
}


public class Main {

    private static final String INPUT_FILE_NAME = "fence.in";
    private static final String OUTPUT_FILE_NAME = "fence.out";
    private static StringBuffer sb;
    private static Tree segments;
    private static int[] test = new int[1000000];
    private static int length = 0;
    private static int index;
    
	public static void main(String[] args) {
		/*for (int i = 0; i < 10; i++) {
			index = i + 1;
			generateRandomData();
			initData();
			readData();
			System.out.println("test " + index + " finished");
		}*/
		
		//System.out.println("end of tests");
		initData();
		readData();
		outputData();
	}
	
	private static void generateRandomData() {
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
	}

	
	private static void initData() {
		segments = new Tree();
		sb = new StringBuffer();
		/*length = 0;
        for (int i = 0; i < 1000000; i++) {
    		test[i] = 0;
    	}*/
	}
	
    private static void readData() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(INPUT_FILE_NAME));
            //scanner = new Scanner(new File("test" + index + ".txt"));
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
            		segments.fill(a, b);
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
        System.out.println(sb.toString());
    }    
    
    
    /*private static void print() {
    	System.out.println("index: " + index);
    	Iterator<Integer[]> iter = pairs.iterator();
    	while (iter.hasNext()) {
    		Integer[] curPair = iter.next();
    		System.out.println(curPair[0] + " - " + curPair[1]);
    	}
    	System.out.println("======================");
    }*/
    
    private static void find(int a, int b) {
    	int[] res = segments.findFullAndEmpty(a, b);
		/*int[] expected = findTest(a, b);
		if (res[1] != expected[0] || res[0] != expected[1]) {
			System.out.println("ASSERTION ERROR FOUND WRONG");
			System.out.println("W " + a + " " + b);
			System.out.println("actual: "+ res[1] + " - " + res[0] + ", expected: " + expected[0] + " - " + expected[1]);
			//print();			
		}*/
    	//print();
    	sb.append(res[0] + " " + res[1] + "\n");
    }
    
    private static int[] findTest(int a, int b) {
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
    }
}
