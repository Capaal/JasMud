import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and Reader.
import java.util.*; // Needed for StringTokenizer.

public class SendMessage {

	protected BufferedReader in;
	protected PrintWriter out;
	protected Socket incoming;
	protected final int width = 80;
	
	public SendMessage(Socket incoming) {
		this.incoming = incoming;
		// Following creates the reader and writer for each user.
		try {
			if (incoming != null) {
				this.in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
				this.out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()));
			}
		} catch (Exception e) {
		}
	}
	// Prints message to user, does not "return" to next line.
	public void printMessageLine(String msg) {
		out.print(msg);
		out.flush();
	}
	
	// Prints any message sent to it.
	public void printMessage(String msg) {
/*		int currentWidth = 0;
		StringTokenizer st = new StringTokenizer(msg);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (currentWidth + token.length() >= width) {
				out.println();
				currentWidth = 0;
			}
			out.print(token + " ");
			currentWidth += token.length();			
		}
		out.println();*/
	//	long start = System.nanoTime();
		out.println(msg);
//		long end = System.nanoTime();
//		long elapsedTime = end - start;
//		System.out.println(elapsedTime*(10E-7) + " milliseconds.");
		out.flush();
	}
	
	// Sends back what user types to be analyzed.
	public String getMessage() {
		try {
			String msg = in.readLine();
			return msg;
		}
		catch (IOException e) {
		}
		return null;
	}
	
	// Just a method to easily print a blank line to the user.
	public void printSpace() {
		out.println("");
		out.flush();
	}
}	