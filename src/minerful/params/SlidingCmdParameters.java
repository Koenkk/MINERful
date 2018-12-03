package minerful.params;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class SlidingCmdParameters extends ParamsManager {
	public static final String SLIDING_STEP_PARAM_NAME = "sliBy";
	public static final int DEFAULT_SLIDING_STEP = 1;

	public static final String STICK_TAIL_PARAM_NAME = "sliStick";
	public static final boolean DEFAULT_STICKY_TAIL_POLICY = false;

	public static final String INTERMEDIATE_OUTPUT_PARAM_NAME = "sliOut";

	/** Sets how long is the step to slide the window on the event log. The default is {@link SlidingCmdParameters#DEFAULT_SLIDING_STEP DEFAULT_SLIDING_STEP} */
    public Integer slidingStep;
    /** The file where to store as a CSV file the constraints' support while MINERful slides over the traces. */
    public File intermediateOutputCsvFile;
    /** Determines whether to stick the tail at the beginning, so that the sliding corresponds to the expansion of the window. The default is {@link SlidingCmdParameters#DEFAULT_STICKY_TAIL_POLICY DEFAULT_STICKY_TAIL_POLICY} */
    public Boolean stickTail;
    
	public SlidingCmdParameters() {
		super();
		slidingStep = DEFAULT_SLIDING_STEP;
		intermediateOutputCsvFile = null;
		stickTail = DEFAULT_STICKY_TAIL_POLICY;
	}


    public SlidingCmdParameters(Options options, String[] args) {
    	this();
        // parse the command line arguments
    	this.parseAndSetup(options, args);
	}

    public SlidingCmdParameters(String[] args) {
    	this();
        // parse the command line arguments
    	this.parseAndSetup(new Options(), args);
	}

	@Override
	protected void setup(CommandLine line) {
        this.slidingStep =
        		Integer.valueOf(line.getOptionValue(
        						SLIDING_STEP_PARAM_NAME,
        						this.slidingStep.toString()
        						)
        				);
        if (slidingStep < 0) {
        	throw new IllegalArgumentException("The sliding window step should be an integer higher than, or equal to, 0");
        }
        String intermediateOutputCsvFilePath = line.getOptionValue(INTERMEDIATE_OUTPUT_PARAM_NAME);
        if (intermediateOutputCsvFilePath != null) {
            this.intermediateOutputCsvFile = new File(intermediateOutputCsvFilePath);
            if (this.intermediateOutputCsvFile.isDirectory()) {
                throw new IllegalArgumentException("Unwritable file: " + this.intermediateOutputCsvFile);
            }
        }
        this.stickTail = line.hasOption(STICK_TAIL_PARAM_NAME);
	}
	
	@SuppressWarnings("static-access")
	public static Options parseableOptions() {
		Options options = new Options();
        options.addOption(
        		OptionBuilder
        		.hasArg().withArgName("num")
        		.withLongOpt("slide-by")
        		.withDescription("sliding window step, in number of traces (must be higher than 0)" + printDefault(DEFAULT_SLIDING_STEP))
        		.withType(new Integer(0))
        		.create(SLIDING_STEP_PARAM_NAME)
        		);
        options.addOption(
        		OptionBuilder
        		.withLongOpt("stick-tail")
        		.withDescription("block the tail and slide only the head (increasing the window length at every step)" + printDefault(DEFAULT_STICKY_TAIL_POLICY))
        		.create(STICK_TAIL_PARAM_NAME)
        		);
       options.addOption(
        		OptionBuilder
        		.hasArg().withArgName("file")
                .isRequired(true)
        		.withLongOpt("sliding-results-out")
        		.withDescription("path of the file in which the values of constraints' measures are written")
        		.create(INTERMEDIATE_OUTPUT_PARAM_NAME)
        		);
       return options;
	}
}