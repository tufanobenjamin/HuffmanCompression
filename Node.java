/*
	Ben Tufano
	Node Class for Huffman Compression/De-Compression
*/

// Import Section
import java.lang.Comparable;

// Node Class
public class Node implements Comparable<Node>
{
	// Declaration
	Node left, right;
	int freq;
	char ch;

	// Constructor
	Node(char ch, int freq, Node left, Node right)
	{
		this.ch = ch;
		this.freq = freq;
		this.left = left;
		this.right = right;
	}

	// Is Leaf Method
	boolean isLeaf() { return (this.left == null && this.right == null); }

	// Comparable Support
	public int compareTo(Node n) { return (this.freq - n.freq); }
}
