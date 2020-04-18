package com.simple.mind.optionreader;

import java.util.ArrayList;
import java.util.List;

import com.simple.mind.optionreader.annotations.Options;

public class Arguments {
	@Options(name = { "books", "b" }, optional = true)
	public ArrayList<String> b;
	public List<String> fileNames;
	public boolean debug;
	@Options(name = "debug2")
	public boolean d2bug;
	public Integer in;
	@Options(ignore = true)
	public static boolean ignore;
}
