package imgts;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.FilenameUtils;

import com.drew.imaging.ImageProcessingException;

public class Imgts {
	
	public static void main(String... args) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		int secondOffset = 0;
		String[] inputNames = args;
		
		imgts(dateFormat, secondOffset, inputNames);
	}
	
	public static void imgts(DateFormat dateFormat, int secondOffset, String[] inputNames) throws ImageProcessingException, IOException {
		TimeZone timeZone = dateFormat.getTimeZone();
		Map<String, Date> timestamps = readTimestamps(inputNames, timeZone);
		
		Set<Date> duplicates = findDuplicateValues(timestamps);
		
		Map<Date, Integer> duplicationCounts = new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		for (Map.Entry<String, Date> entry : timestamps.entrySet()) {
			String input = entry.getKey();
			Date timestamp = entry.getValue();
			
			String output = input;
			if (timestamp != null) {
				String duplicationMarker = "";
				if (duplicates.contains(timestamp)) {
					int duplicationCount = duplicationCounts.getOrDefault(timestamp, 0);
					duplicationCounts.put(timestamp, duplicationCount + 1);
					
					duplicationMarker = String.valueOf((char) ('a' + duplicationCount));
				}
				
				calendar.setTime(timestamp);
				calendar.add(Calendar.SECOND, secondOffset);
				Date offsetTimestamp = calendar.getTime();
				output = dateFormat.format(offsetTimestamp) + duplicationMarker;
			}
			
			// Should really check if there already is something with name 'output', but meh...
			printEntry(input, output);
		}
	}
	
	private static Map<String, Date> readTimestamps(String[] inputNames, TimeZone timeZone) throws ImageProcessingException, IOException {
		Map<String, Date> timestamps = new LinkedHashMap<>();
		for (String inputName : inputNames) {
			Date timestamp = ImageTimestampReader.readTimestamp(inputName, timeZone);
			timestamps.put(inputName, timestamp);
		}
		return timestamps;
	}
	
	private static Set<Date> findDuplicateValues(Map<String, Date> timestamps) {
		Set<Date> seen = new HashSet<>();
		Set<Date> duplicates = new HashSet<>();
		for (Map.Entry<String, Date> entry : timestamps.entrySet()) {
			Date timestamp = entry.getValue();
			boolean added = seen.add(timestamp);
			if (!added) {
				duplicates.add(timestamp);
			}
		}
		return duplicates;
	}
	
	private static void printEntry(String input, String output) {
		String extension = FilenameUtils.getExtension(input);
		String formattedOutput = output + '.' + extension;
		System.out.println(input + '\t' + formattedOutput);
	}
}
