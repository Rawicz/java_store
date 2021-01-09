package ufos;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.NoSuchElementException;

/**
 * The programm for the problem #1470 at acm.timus.ru .
 * Asymptotics is log(N)*log(N)*log(N)
 * Memory usage is N*N*N
 */
public class UFOs {
	static int bs0[][][];
	static int N; // The side of a cube space in which the calculations are being done

	public static void main (String[] args) {
		N = 128;
		bs0 = new int[N][N][N];

		System.out.println("Welcome! This is UFO counter programm!");
		System.out.println("1 x y z V:\nAdds V UFOs to x:y:z sector (can be negative)");
		System.out.println("2 x1 y1 z1 x2 y2 z2:\nCounts the total number of UFOs between x1:y1:z1 and x2:y2:z2 sectors inclusively");
		System.out.println("3:\nExits the programm");
		java.util.Scanner input = new java.util.Scanner(System.in);
		java.util.Scanner line;
		int sumInArea;

		boolean open = true;
		while(open) {
			try {
				System.out.println();
				line = new java.util.Scanner(input.nextLine());
				System.out.println();
				int n = line.nextInt();
				switch(n) {
					case 1:
						biAdd(line.nextInt(), line.nextInt(), line.nextInt(), line.nextInt());
						System.out.println("The info on UFOs has successfully changed.");
						break;
					case 2:
						int x1 = line.nextInt();
						int y1 = line.nextInt();
						int z1 = line.nextInt();
						int x2 = line.nextInt();
						int y2 = line.nextInt();
						int z2 = line.nextInt();
						// The long expression below counts the number of UFOs based on counter function
						// ( Which counts the number not from the specified point but from the zero )
						sumInArea = biCount(x2, y2, z2) - biCount(x1, y2, z2) - biCount(x2, y1, z2) - biCount(x2, y2, z1) + biCount(x2, y1, z1) + biCount(x1, y2, z1) + biCount(x1, y1, z2) - biCount(x1, y1, z1);
						System.out.println("The total quantity of UFOs in the specified area: " + sumInArea);
						break;
					case 3:
						System.out.println("Goodbye!");
						open = false;
						break;
					default:
						System.out.println("Please enter the valid option! (1,2 or 3)");
						break;
				}
			} catch(NoSuchElementException e) {
				System.out.println("Please check the input carefully!");
			} catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Function that records the apeearing of UFOs at given coordinates.
	 * Does so by adding the number of UFOs to a binary partition 3D matrix.
	 * The coordinates are:
	 * @param x
	 * @param y
	 * @param z
	 * @param value is the number of UFOs appeared.
	 */
	public static void biAdd(int x, int y, int z, int value) throws IllegalArgumentException {
		ArrayList<Integer> yValues = new ArrayList<Integer>(N);
		ArrayList<Integer> zValues = new ArrayList<Integer>(N);
		// default values for 1 step of for iteration:
		int axis = y;
		Consumer<Integer> addition = (n) -> yValues.add(n);
		for (int step = 1; step <= 3; step++) {
			switch(step) {
				case 1:
					break;
				case 2:
					axis = z;
					addition = (n) -> zValues.add(n);
					break;
				case 3:
					axis = x;
					addition = (n) -> {
						for (Integer yValue : yValues)
							for (Integer zValue : zValues) {
								bs0[n][yValue][zValue] += value;
								if (bs0[n][yValue][zValue] < 0) throw new ArithmeticException("Perhaps there's a negative amount of UFOs somewhere");
							}
					};
					break;
			}
			if (axis >= N || axis < 0) throw new IllegalArgumentException("The specified coordinates are beyond the borders!");
			int index = 0;
			int bottom = 0;
			int top = N;
			int augment = 1; // this will be changed after the first iteration, but should be NOT zero.
			while (augment > 0) {
				try {
					if (axis <= index) {
						addition.accept(index);
						augment = (index- bottom)/2;
						top = index;
						index = bottom + augment;
					} else {
						augment = (top - index)/2;
						bottom = index;
						index = bottom + augment;
					}
				} catch(ArithmeticException e) {
					int exceptionPoint = index;
					index = 0; bottom = 0; top = N; augment =1;
					addition = (n) -> {
						for (Integer yValue : yValues)
							for (Integer zValue : zValues) {
								bs0[n][yValue][zValue] -= value;
								if (n == exceptionPoint) throw new IllegalArgumentException("The total of UFOs can't be negative!");
							}
					};
				}
			}
		}
	}

	/**
	 * Function that counts the summary number of all UFOs from the [0, 0, 0,] coordinates to the specified ones.
	 * Does so by summarizing values of the binary space 3D matrix.
	 * The coordinates are:
	 * @param x
	 * @param y
	 * @param z
	 * @return the calculated nmber.
	 */
	public static int biCount(int x, int y, int z) {
		ArrayList<Integer> yValues = new ArrayList<Integer>(N);
		ArrayList<Integer> zValues = new ArrayList<Integer>(N);
		AtomicInteger sum = new AtomicInteger(0);
		// default values for 1 step of for iteration:
		int axis = y;
		Consumer<Integer> addition = (n) -> yValues.add(n);
		for (int step = 1; step <= 3; step++) {
			switch(step) {
				case 1:
					break;
				case 2:
					axis = z;
					addition = (n) -> zValues.add(n);
					break;
				case 3:
					axis = x;
					addition = (n) -> {
						for (Integer yValue : yValues)
							for (Integer zValue : zValues)
								sum.set(sum.get() +  bs0[n][yValue][zValue]);
					};
					break;
			}
			int index = 0;
			int bottom = 0;
			int top = N;
			int augment = 1; // this will be changed after the first iteration, but should be NOT zero.
			while (augment > 0) {
				if (axis >= index) {
					addition.accept(index);
					augment = (top - index)/2;
					bottom = index;
					index = bottom + augment;
				} else {
					augment = (index - bottom)/2;
					top = index;
					index = bottom + augment;
				}
			}
		}
		return sum.get();
	}
}
