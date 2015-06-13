package dev.blakestone.java;

@BSClass
public class TestClass {
	@BSField("firstInt")
	private int intField;
	
	@BSField("firstString")
	private String usefulName = "\"\"";
	
	private double ignoredTemplate;
	
	@Override
	public String toString() {
		return "TestClass [intField=" + intField + ", usefulName=" + usefulName
				+ ", ignoredTemplate=" + ignoredTemplate + "]";
	}
	
	
}
