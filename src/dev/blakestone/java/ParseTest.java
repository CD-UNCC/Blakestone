package dev.blakestone.java;

import java.io.File;

public class ParseTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//BlakestoneObj obj = BlakestoneObj.read(new File("blakestone.bs"));
			//System.out.println(obj);
			//System.out.println("-");
			
			System.out.println(BlakestoneObj.loadIntoClass(TestClass.class, new File("classTemplate.bs")));
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

}
