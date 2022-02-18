/*
	Ben Tufano
	Bit OutputStream
*/

// Import Section
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream
{
	// Declaration
	private OutputStream out;
	private boolean[] currentByte = new boolean[8];
	private int count = 0;

	// Constructor
	public BitOutputStream(OutputStream out) { this.out = out; }

	// Write Bit Method
    public void write(boolean inBit) throws IOException
    {
		this.currentByte[8 - ++this.count] = inBit;

		if (this.count == 8)
		{
			int num = 0;

			for (int index = 0; index < 8; index++)
				num = 2 * num + (this.currentByte[index] ? 1 : 0);

			this.out.write(num - 128);

			this.count = 0;
		}
	}

	// To close the Stream
    public void close() throws IOException
    {
		int num = 0;

		for (int index = 0; index < 8; index++)
			num = 2 * num + (this.currentByte[index] ? 1 : 0);

		this.out.write(num - 128);

		this.out.close();
    }

}
