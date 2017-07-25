package minerful.logparser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import minerful.concept.AbstractTaskClass;
import minerful.concept.TaskCharArchive;
import minerful.io.encdec.TaskCharEncoderDecoder;

public class StringLogParser extends AbstractLogParser implements LogParser {
    StringEventClassifier strEventClassifier;
    
	protected StringLogParser(TaskCharEncoderDecoder taChaEncoDeco,
			TaskCharArchive taskCharArchive, List<LogTraceParser> traceParsers,
			StringEventClassifier strEventClassifier) {
		super(taChaEncoDeco, taskCharArchive, traceParsers);
		this.strEventClassifier = strEventClassifier;
	}

    public StringLogParser(String[] strings, LogEventClassifier.ClassificationType evtClassType) throws Exception {
        this.init(evtClassType);
        
        super.archiveTaskChars(this.parseLog(strings));
	}
	
	public StringLogParser(File stringsLogFile, LogEventClassifier.ClassificationType evtClassType) throws Exception {
        if (!stringsLogFile.canRead()) {
        	throw new IllegalArgumentException("Unparsable log file: " + stringsLogFile.getAbsolutePath());
        }

        init(evtClassType);
        
        super.archiveTaskChars(this.parseLog(stringsLogFile));
	}

	private void init(LogEventClassifier.ClassificationType evtClassType) {
		this.taChaEncoDeco = new TaskCharEncoderDecoder();
        this.traceParsers = new ArrayList<LogTraceParser>();
        this.strEventClassifier = new StringEventClassifier(evtClassType);
	}
	
	protected Collection<AbstractTaskClass> parseLog(String[] strings) {
		for (String strLine : strings) {
        	strLine = strLine.trim();

        	this.updateTraceMetrics(strLine);
        	this.updateTraceParsers(strLine);
        	this.updateClasses(strLine);
		}
        return this.strEventClassifier.getTaskClasses();
	}

	private void updateTraceParsers(String strLine) {
		this.traceParsers.add(new StringTraceParser(strLine, this));
	}

	private void updateClasses(String strLine) {
		for (char chr : strLine.toCharArray()) {
			this.strEventClassifier.classify(chr);
		}
	}

	private void updateTraceMetrics(String strLine) {
		updateMaximumTraceLength(strLine.length());
		updateMinimumTraceLength(strLine.length());
		updateNumberOfEvents(strLine.length());
	}

	@Override
	protected Collection<AbstractTaskClass> parseLog(File stringsLogFile) throws Exception {
        FileInputStream fstream = new FileInputStream(stringsLogFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine = br.readLine();
        
        while (strLine != null) {
        	strLine = strLine.trim();
        	
        	updateTraceMetrics(strLine);
        	updateTraceParsers(strLine);
            updateClasses(strLine);

            strLine = br.readLine();
        }
        in.close();
        return this.strEventClassifier.getTaskClasses();
	}

	@Override
	public LogEventClassifier getEventClassifier() {
		return this.strEventClassifier;
	}

	@Override
	protected AbstractLogParser makeACopy(
			TaskCharEncoderDecoder taChaEncoDeco,
			TaskCharArchive taskCharArchive, List<LogTraceParser> traceParsers) {
		return new StringLogParser(taChaEncoDeco, taskCharArchive, traceParsers, strEventClassifier);
	}
}