package craftsmen.associates.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
	public static String readerToString(InputStream input) throws IOException {
		StringBuilder builder = new StringBuilder();
		try (BufferedReader isr2 = new BufferedReader(new InputStreamReader(input))) {
			String read = "";
			do {
				read = isr2.readLine();
				builder.append(read);
			} while (isReadable(read));
		}
		System.out.println(builder);
		return builder.toString();
	}

	private static boolean isReadable(String read) {
		return (read != null && read.length() > 1);
	}

}
