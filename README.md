# Command Option Parser for Java

> All the functions are written in a way where it either successfully executes or just throw exception. These includes validation. If invalid parameter or options are available function will throw exception. 

## Usage Example:

```java

import java.util.ArrayList;

import com.simple.mind.optionreader.OptionParser;
import com.simple.mind.optionreader.annotations.Options;

public class Example {
	public static class Arguments {
		// this is not an optional parameter; if empty, error will be thrown

		ArrayList<String> fileNames;

		@Options(defaultValues = "false")
		boolean debug;
	}

	public static void main(String[] args) throws Exception {
		Arguments a = ParseOption(args, Arguments.class);
		System.out.println("Print all files");
		for (String fName : a.fileNames) {
			System.out.println("> " + fName);
		}
		System.out.println("Debug Value: " + a.debug);
	}
}

```
### Execution

```sh
 java -classpath "$(pwd)/target/*" com.simple.mind.optionreader.example.Example --fileNames  name.json --fileNames phone.json --fileNames "address.json" "email-address.json" --file_names "additional.json"
```

### Result

```
Print all files
> name.json
> phone.json
> address.json
> email-address.json
> additional.json
Debug Value: false
```
