package processes;
import interfaces.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UsefulCommands {
	// Converts incoming word so that only numbers are returned.
	public static String getOnlyNumerics(String str) {
	   	if (str == null) {
	   		return null;
	   	}
		StringBuffer strBuff = new StringBuffer();
	    char c;
	    for (int i = 0; i < str.length() ; i++) {
	    	c = str.charAt(i);
 			if (Character.isDigit(c)) {
 				strBuff.append(c);
 			}
  		}
  		return strBuff.toString();
	}
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
	
	// Converts incoming word so that numbers are stripped out and only letters are returned.
	public static String getOnlyStrings(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer strBuff = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if (!Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString();
	}
	
	// Returns a set stringTokenizer.
	public static StringTokenizer getST(String fullCommand) {
		StringTokenizer st = new StringTokenizer(fullCommand);
		st.nextToken();
		return st;
	}
	
	public final class ANSI {

		public static final String SANE = "\u001B[0m";
		public static final String HIGH_INTENSITY = "\u001B[1m";
		public static final String LOW_INTESITY = "\u001B[2m";
		
		public static final String ITALIC = "\u001B[3m";
		public static final String UNDERLINE = "\u001B[4m";
		public static final String BLINK = "\u001B[5m";
		public static final String RAPID_BLINK = "\u001B[6m";
		public static final String REVERSE_VIDEO = "\u001B[7m";
		public static final String INVISIBLE_TEXT = "\u001B[8m";

		public static final String BLACK = "\u001B[30m";
		public static final String RED = "\u001B[31m";
		public static final String GREEN = "\u001B[32m";
		public static final String YELLOW = "\u001B[33m";
		public static final String BLUE = "\u001B[34m";
		public static final String MAGENTA = "\u001B[35m";
		public static final String CYAN = "\u001B[36m";
		public static final String WHITE = "\u001B[37m";

		public static final String BACKGROUND_BLACK = "\u001B[40m";
		public static final String BACKGROUND_RED = "\u001B[41m";
		public static final String BACKGROUND_GREEN = "\u001B[42m";
		public static final String BACKGROUND_YELLOW = "\u001B[43m";
		public static final String BACKGROUND_BLUE = "\u001B[44m";
		public static final String BACKGROUND_MAGENTA = "\u001B[45m";
		public static final String BACKGROUND_CYAN = "\u001B[46m";
		public static final String BACKGROUND_WHITE = "\u001B[47m";

		private ANSI() {} // disable automatic constructor
	}
	
	public static boolean checkIfValidCharacters(String testString) {
		Pattern pattern = Pattern.compile("[;'~#@*+%{}<>\\[\\]|\"\\_^!=]");
		Matcher matcher = pattern.matcher(testString);
		if (testString.matches(".*\\d.*") || matcher.find() || testString.equals("")) {
			return false;
		}
		return true;
	}
	
	//duplicate of messageOthers in Skills
	public static void messageOthers(Mobile currentPlayer, String msg, List<Mobile> toIgnore) {
		for (Mobile h : currentPlayer.getContainer().viewMobiles().values()) {
			if (h.isControlled()) {
				Boolean shouldTell = true;
				if (h.equals(currentPlayer)) {
					shouldTell = false;
				} else {
					for (Mobile m : toIgnore) {
						if (h.equals(m)) {
							shouldTell = false;
						}
					}
				}
				if (shouldTell) {
					h.tell(msg);
					h.displayPrompt();
				}
			}			
		}
	}
}
