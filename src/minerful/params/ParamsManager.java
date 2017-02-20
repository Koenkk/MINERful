package minerful.params;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public abstract class ParamsManager {
    public static final String EXPERIMENTAL_DEVELOPMENT_STAGE_MESSAGE = 
			"*** WARNING: experimental development stage of implementation!";
	private static final int DEFAULT_PROMPT_WIDTH = 160;
    protected HelpFormatter helpFormatter = new HelpFormatter();

    public ParamsManager() {
    	helpFormatter.setWidth(DEFAULT_PROMPT_WIDTH);
	}

	public void printHelp() {
    	this.printHelp(this.listParseableOptions());
    }

    public void printHelp(Options options) {
    	helpFormatter.printHelp("cmd_name", options, true);
    }
    
    public void printHelpForWrongUsage(String errorMessage, Options options) {
    	System.err.println("Wrong usage: " + errorMessage);
    	this.printHelp(options);
    }
    
    public void printHelpForWrongUsage(String errorMessage) {
    	this.printHelpForWrongUsage(errorMessage, this.listParseableOptions());
    }

    public Options addParseableOptions(Options options) {
        Options myOptions = listParseableOptions();
        for (Object myOpt : myOptions.getOptions()) {
            options.addOption((Option) myOpt);
        }
        return options;
    }

    protected void parseAndSetup(Options otherOptions, String[] args) {
        // create the command line parser
        CommandLineParser parser = new PosixParser();
        Options options = addParseableOptions(otherOptions);
        try {
        	CommandLine line = parser.parse(options, args, false);
        	setup(line);
        } catch (ParseException exp) {
            System.err.println("Unexpected exception:" + exp.getMessage());
        }
    }

    public Options listParseableOptions() {
    	return parseableOptions();
    }
    
    /**
     * Meant to be hidden by extending classes!
     */
    private static Options parseableOptions() {
		return new Options();
	}

	protected abstract void setup(CommandLine line);
                
    protected static String fromStringToEnumValue(String token) {
    	if (token != null)
    		return token.trim().toUpperCase().replace("-", "_");
    	return null;
	}

    protected static String fromEnumValueToString(Object token) {
		return token.toString().trim().toLowerCase().replace("_", "-");
	}
    
    public static String printDefault(Object defaultValue) {
    	return ".\nDefault is: '" + defaultValue.toString() + "'"; 
    }

	public static String printValues(Object... values) {
        StringBuilder valuesStringBuilder = new StringBuilder();

        if (values.length > 1) {
        	valuesStringBuilder.append("{");
        }

        for (int i = 0; i < values.length; i++) {
            valuesStringBuilder.append("'");
            valuesStringBuilder.append(fromEnumValueToString(values[i]));
            valuesStringBuilder.append("'");
            if (i < values.length -1) {
                valuesStringBuilder.append(",");
            }
        }

        if (values.length > 1) {
        	valuesStringBuilder.append("}");
        }

        return valuesStringBuilder.toString();
    }

    protected static String attachInstabilityWarningToDescription(String description) {
    	return EXPERIMENTAL_DEVELOPMENT_STAGE_MESSAGE + "\n" + description;
    }
}