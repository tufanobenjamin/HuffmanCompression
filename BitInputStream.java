/*
    Ben Tufano
    Bit InputStream
*/

// Import Section
import java.io.IOException;
import java.io.InputStream;

// Stream Class
public class BitInputStream
{
    // Declaration
    private InputStream in;
    private int num = 0;
    private int count = 8;

    // Constructor
    public BitInputStream(InputStream in) { this.in = in; }

    // Reading a Bit Method
    public boolean read() throws IOException
    {
        if (this.count == 8)
        {
            this.num = this.in.read() + 128;
            this.count = 0;
        }

        boolean x = (num % 2 == 1);
        num /= 2;
        this.count++;

        return x;
    }

    // Check to see if there are more bits/bytes
    boolean isReady() throws IOException { return (this.in.available() > 0); }

    // Closing the Stream
    public void close() throws IOException { this.in.close(); }
}
