/*
	Ben Tufano
	Huffman Compression/De-Compression Script
	--------------------------------------------------------------------
	Usage:

		java Huffman - C:\path\to\file
			= This Compresses a File at the designated path
		java Huffman + C:\path\to\file
			= This De-Compresses a File at the designated path
*/

// Import Section
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

// Public Huffman Class
public class Huffman
{
	// Declaration of import arrays
	private static int[] freq = new int[256];
	private static String[] code = new String[256];

	// Huffman Methods
	private static Node buildTree()
	{
		// Declaration
		PriorityQueue<Node> pq = new PriorityQueue<Node>();

		// Insert each occuring frequencies
		for(char i = 0; i < freq.length; i++)
			if(freq[i] > 0)
				pq.add(new Node(i, freq[i], null, null));

		// Build our tree one combination at a time
		while(pq.size() > 1)
		{
			Node left = pq.remove();
			Node right = pq.remove();
			pq.add(new Node((char)0, left.freq + right.freq, left, right));
		}

		// Return our final root Node
		return pq.remove();
	}
	private static void buildCodes(String[] codes, Node n, String combo)
	{
		if(!n.isLeaf())
		{
			buildCodes(codes, n.left, combo + '0');
			buildCodes(codes, n.right, combo + '1');
		}
		else
			codes[n.ch] = combo;
	}

	// Compress and De-Compress Methods
	private static void compress(String inputFile)
	{
		// Declaration
		int newByte = 0;

		try
		{
			// Open our target file
			BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(inputFile));

			// Gather frequencies of each byte
			while((newByte = fileReader.read()) != -1)
				freq[(char)newByte]++;

			// Close the Reader
			fileReader.close();

			// Build our Codes
			buildCodes(code, buildTree(), "");

			// Write our key to the new File
			BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(inputFile + ".hzip"));

			for(char i = 0; i < code.length; i++)
				if(code[i] != null)
				{
					fileWriter.write((int)';');

					for(int j = 0; j < code[i].length(); j++)
						fileWriter.write((int)code[i].charAt(j));

					fileWriter.write((int)':');
					fileWriter.write((int)i);
				}

			// Close our Key Writer
			fileWriter.close();

			// Re-Open our File
			fileReader = new BufferedInputStream(new FileInputStream(inputFile));
			BitOutputStream bitWriter = new BitOutputStream(new FileOutputStream(inputFile + ".hzip", true));

			// Write each byte indivually into bits
			while((newByte = fileReader.read()) != -1)
			{
				String combo = code[(char)newByte];

				for(int i = 0; i < combo.length(); i++)
					bitWriter.write(combo.charAt(i) == '0' ? false : true);
			}

			// Close down our streams
			fileReader.close();
			bitWriter.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	private static void deCompress(String inputFile)
	{
		// Declaration
		int newByte = 0;
		int keyBytes = 0;
		String currentCode = "";

		try
		{
			BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(inputFile + ".hzip"));

			while((newByte = fileReader.read()) == ';')
			{
				String combo = "";

				while((newByte = fileReader.read()) != ':')
					combo = combo + (char)newByte;

				code[(char)fileReader.read()] = combo;
				keyBytes += (3 + combo.length());
			}

			keyBytes += 2;
			fileReader.close();

			BitInputStream bitReader = new BitInputStream(new FileInputStream(inputFile + ".hzip"));
			BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(inputFile));

			for(int i = 0; i < (keyBytes * 8); i++)
				bitReader.read();

			while(bitReader.isReady())
			{
				currentCode = currentCode + (bitReader.read() ? '1' : '0');

				for(int i = 0; i < code.length; i++)
					if(code[i] != null && code[i].equals(currentCode))
					{
						fileWriter.write(i);
						currentCode = "";
						break;
					}
			}

			bitReader.close();
			fileWriter.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}

	// Main Method
	public static void main(String[] args)
	{
		String inputFile = "";

		for(int i = 1; i < args.length; i++)
			inputFile = inputFile + args[i];

		if(args[0].equals("-")) compress(inputFile);
		else if(args[0].equals("+")) deCompress(inputFile);
		else System.out.println("Incorrect Usage");
	}
}
