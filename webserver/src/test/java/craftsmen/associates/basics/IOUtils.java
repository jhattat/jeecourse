package craftsmen.associates.basics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
	public static String readerToString(InputStream input) throws IOException {
		BufferedReader isr2 = new BufferedReader(new InputStreamReader(input));
		
		StringBuilder builder = new StringBuilder();
		String read = "";
		do{
			read = isr2.readLine();
			builder.append(read);
		}while(isReadable(read));
		return builder.toString();
	}

	private static boolean isReadable(String read) {
		return (read != null && read.length()>1);
	}

}
