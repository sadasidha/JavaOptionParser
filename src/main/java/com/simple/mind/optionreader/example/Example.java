package com.simple.mind.optionreader.example;

import java.util.ArrayList;

import com.simple.mind.optionreader.OptionsParser;
import com.simple.mind.optionreader.annotations.Options;

public class Example {
	public static class Arguments {
		// this is not an optional parameter; if empty, error will be thrown

		ArrayList<String> fileNames;

		@Options(defaultValues = "false")
		boolean debug;
	}

	public static void main(String[] args) throws Exception {
		Arguments a = OptionsParser.ParseOption(args, Arguments.class);
		System.out.println("Print all files");
		for (String fName : a.fileNames) {
			System.out.println("> " + fName);
		}
		System.out.println("Debug Value: " + a.debug);
	}
}
