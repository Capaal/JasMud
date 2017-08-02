package processes;

import java.net.*; // Needed for Socket.
import java.io.*; // Needed for PrintWriter and Reader.

// Socket sending and receiving class.
public class SendMessage {

	protected BufferedReader in;
	protected PrintWriter out;
	protected Socket incoming;
	protected final int width = 80; // Defines max line length. // TODO give extra control to user.
	
	public SendMessage(Socket incoming) throws IOException {
		if (incoming == null) {
			throw new IllegalArgumentException("Socket may not be null.");
		}
		this.incoming = incoming;
		this.in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
		this.out = new PrintWriter(new OutputStreamWriter(incoming.getOutputStream()), true);	
	}
	
	// Prints a message to the user linked with the Socket.
	public void printMessage(String msg) {
		out.println(msg);
		out.flush();
		if (out.checkError()) {
			System.out.println("Sendback error");
		}
	}
	
	// Get what user types to be analyzed.
	public String getMessage() {
		try {
			String msg = in.readLine();
			System.out.println("SendMessage: " + msg);
			if (msg == null) {
				incoming.close();
			}
			return msg;
		}
		catch (IOException e) {
			System.out.println("Sendback error: BufferedReader.");
			try {
				incoming.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return "";
	}
	
	// Just a method to easily print a blank line to the user.
	public void printSpace() {
		out.println("");
		out.flush();
	}
}	