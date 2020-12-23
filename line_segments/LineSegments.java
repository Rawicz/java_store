import struct.Duet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Main class of the programm for working with line segments.
 * Current version allows only one operation:
 * @see #countUnion method
 */
public class LineSegments {
	public static void main (String[] args) {
		ArrayList<Duet<Integer>> segments = new ArrayList<Duet<Integer>>();
		java.util.Scanner ins = new java.util.Scanner(System.in);
		java.util.Scanner line;
		int firstEdge, secondEdge; // variables for quick check of the order
		System.out.println("Enter the coordinates of the line segments, lesser coordinate first:");
		while(true) {
			line = new java.util.Scanner(ins.nextLine());
			try {
				if (line.hasNext()) {
					firstEdge = line.nextInt();
					secondEdge = line.nextInt();
					if (firstEdge > secondEdge)
						throw new RuntimeException("Put the left edge first!");
					segments.add(new Duet<Integer>(firstEdge, secondEdge));
				}
				else break;
			} catch(NoSuchElementException e) {
				System.out.println("Wrong input!");
			} catch(RuntimeException e) {
				System.out.println(e.getMessage());
			}
		} 
		System.out.println("The length of the segments union is:\n" + countUnion(segments));
	}

	/**
	 * Counts the summary length of the union of given line segments.
	 * It's a union, not just a sum of lengths, so conjunctions are not overlapping.
	 * The algorithm uses two sorts with asymptotics O(N*log N) and one-side pass with O(N).
	 * The summary asymptotic of the algorithm is O(N*log N).
	 * @param segments line segments represented by ArrayList of pairs or coordinated (which should be integers)
	 * @return integer which represents the length of the described union.
	 */
public static int countUnion(ArrayList<Duet<Integer>> segments) {
		int[] leftEdges = new int[segments.size()];
		int[] rightEdges = new int[segments.size()];
		int counter = 0; // counter of left edges which were not followed by any right edges
		int i = 0; int j = 0; // dummy indexes for future use
		int union = 0; // this variable will be returned

		for (Duet<Integer> segment : segments) { // putting edges in two arrays
			leftEdges[i] = segment.getFirst();
			rightEdges[i] = segment.getLast();
			i++;
		}
		i = 0;

		Arrays.sort(leftEdges); Arrays.sort(rightEdges); // sorting the arrays of edges

		int rightMargin; // represents the right margin of the current set of conjuncted segments
		int leftMargin = leftEdges[i]; counter++; i++; // represents the left margin, first being defined here
		while (i < segments.size()) {
			if (leftEdges[i] < rightEdges[j]) {
				counter++; i++;
			} else {
				counter--;
				if (counter == 0) {
					rightMargin = rightEdges[j];
					union += (rightMargin - leftMargin);
					leftMargin = leftEdges[i]; counter++; i++;
				}
				j++;
			}
		}
		union += (rightEdges[rightEdges.length - 1] - leftMargin);
		return union;
	}
}
