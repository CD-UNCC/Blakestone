package dev.blakestone.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;
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
	private ArrayList<Object> arr;
	
	public BlakestoneObj() {
		keyVals = new HashMap<>();
		arr = new ArrayList<>();
	}
	
	public void add(Object obj) {
		arr.add(obj);
	}
	
	public void add(String key, Object value) {
		keyVals.put(key, value);
	}
	
	public Object get(String key) {
		return keyVals.get(key);
	}
	
	public Object get(int index) {
		return arr.get(index);
	}
	
	public int size() { return arr.size(); }
	
	@Override
	public String toString() {
		String result = "";
		
		for (int i = 0; i < arr.size(); i++)
			if (arr.get(i) instanceof BlakestoneObj) {
				result += i + ": \n";
				for (String line : arr.get(i).toString().split("\\r?\\n"))
					result += "   " + line + "\n";
			} else
				result += i + ": " + arr.get(i).toString() + "\n";
				
		ArrayList<String> sortedKeys = new ArrayList<>(keyVals.keySet());
		Collections.sort(sortedKeys);
		
		for (String key : sortedKeys)
			if (keyVals.get(key) instanceof BlakestoneObj) {
				result += key + ": \n";
				for (String line : keyVals.get(key).toString().split("\\r?\\n"))
					result += "   " + line + "\n";
			} else
				result += key + ": " + keyVals.get(key).toString() + "\n";
		
		return result;
	}
	
	public static BlakestoneObj read(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		String indent = null;
		Matcher m;
		
		int level, lastLevel = 0;
		
		Stack<BlakestoneObj> currentParent = new Stack<>();
		currentParent.push(new BlakestoneObj());
		
		while ((line = reader.readLine()) != null) {
			if (indent == null) {
				m = INDENT_PATTERN.matcher(line);
				if (m.find())
					indent = m.group();
			}
			
			String type = "value";
			
			level = 0;
			if (indent != null)
				while (line.startsWith(indent)) {
					level++;
					line = line.substring(indent.length());
				}
			
			
			if (level < lastLevel)
				currentParent.pop();
				
			lastLevel = level;
			
			m = COMMENT_PATTERN.matcher(line);
			if (m.find())
				line = line.substring(0, m.start());
			
			line = line.trim();
			
			if ((m = KEY_PATTERN.matcher(line)).matches())
				type = "key";
			else if ((m = INDEX_PATTERN.matcher(line)).matches())
				type = "index";
			else if ((m = INDEX_VALUE_PATTERN.matcher(line)).matches())
				type = "index-value";
			else if ((m = KEY_VALUE_PATTERN.matcher(line)).matches())
				type = "key-value";
			
			if (type.endsWith("value")) {
				String value;
				String key = null;
				
				if (type.equals("key-value")) {
					value = m.group(2);
					key = m.group(1);
				}
				else if (type.equals("index-value"))
					value = m.group(1);
				else {
					type = "index-value";
					value = line;
				}					
				
				m = VALUE_PATTERN.matcher(value);				
				
				if (m.find()) {
					Object obj = null;
					
					if (STRING_PATTERN.matcher(value).matches())
						obj = value.substring(1, value.length() - 1);
					else if (INTEGER_PATTERN.matcher(value).matches())
						obj = value.startsWith("%") ? -1 : Integer.parseInt(value);
					else if (FLOATING_POINT_PATTERN.matcher(value).matches())
						obj = value.startsWith("%") ? -0.1 : Float.parseFloat(value);
					
					if (type.startsWith("index"))
						currentParent.peek().add(obj);
					else
						currentParent.peek().add(key, obj);
				}
			} else {
				BlakestoneObj newTop = new BlakestoneObj();
				if (type.equals("key")) 
					currentParent.peek().add(m.group(1), newTop);
				else
					currentParent.peek().add(newTop);
			
				currentParent.push(newTop);
			}
		}
		
		while (currentParent.size() > 1)
			currentParent.pop();
		
		return currentParent.pop();
	}
	
	public static BlakestoneObj read(File f) throws IOException, FileNotFoundException { return read(new FileInputStream(f)); }
}
