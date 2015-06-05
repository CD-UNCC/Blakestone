package dev.blakestone.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlakestoneObj {
	// REGEX FORMATS
	private final static String INTEGER_REGEX = "(?:%d\\[.+\\]|\\d+)";
	private final static String FLOATING_POINT_REGEX = "(?:%f(?:\\.\\d+)?\\[.+\\]|\\d*\\.\\d+)";
	private final static String STRING_REGEX = "(?:%s\\/.+\\/|%s\\[.+\\]|\".+\")";
	
	// PATTERNS
	private final static Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);
	private final static Pattern FLOATING_POINT_PATTERN = Pattern.compile(FLOATING_POINT_REGEX);
	private final static Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
	
	private final static Pattern INDENT_PATTERN = Pattern.compile("\\s+");
	private final static Pattern COMMENT_PATTERN = Pattern.compile("\\s*#.*");
	private final static Pattern KEY_PATTERN = Pattern.compile("(\\w[\\w\\d]+):");
	private final static Pattern KEY_VALUE_PATTERN = Pattern.compile("(\\w[\\w\\d]*):\\s*(.+)");
	private final static Pattern VALUE_PATTERN = Pattern.compile(String.format("(%s|%s|%s)",
			STRING_REGEX, FLOATING_POINT_REGEX, INTEGER_REGEX ));
	private final static Pattern INDEX_PATTERN = Pattern.compile(":");
	private final static Pattern INDEX_VALUE_PATTERN = Pattern.compile(":\\s*(.+)");
	
	private HashMap<String, Object> keyVals;
	private ArrayList<Object>[] arr;
	
	public BlakestoneObj(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		String indent = null;
		Matcher m;
		
		
		
		while ((line = reader.readLine()) != null) {
			if (indent == null) {
				m = INDENT_PATTERN.matcher(line);
				if (m.find())
					indent = m.group();
			}
			
			String type = "value";
			
			int level = 0;
			if (indent != null)
				while (line.startsWith(indent)) {
					level++;
					line = line.substring(indent.length());
				}
			
			m = COMMENT_PATTERN.matcher(line);
			if (m.find())
				line = line.substring(0, m.start());
			
			line = line.trim();
			
			if (KEY_PATTERN.matcher(line).matches())
				type = "key";
			else if (INDEX_PATTERN.matcher(line).matches())
				type = "index";
			else if ((m = INDEX_VALUE_PATTERN.matcher(line)).matches())
				type = "index-value";
			else if ((m = KEY_VALUE_PATTERN.matcher(line)).matches())
				type = "key-value";
			
			if (type.endsWith("value")) {
				String value;
				
				if (type.equals("key-value"))
					value = m.group(2);
				else if (type.equals("index-value"))
					value = m.group(1);
				else
					value = line;
				
				m = VALUE_PATTERN.matcher(value);				
				
				if (m.find()) {
					if (STRING_PATTERN.matcher(value).matches())
						type += "-str";
					else if (INTEGER_PATTERN.matcher(value).matches())
						type += "-int";
					else if (FLOATING_POINT_PATTERN.matcher(value).matches())
						type += "-flt";
				}
			}		
				
			System.out.printf("%2d (%S) | %s%n", level, type, line);
		}
	}
	
	public BlakestoneObj(File f) throws FileNotFoundException, IOException { this(new FileInputStream(f)); }
}
