package hu.doxasoft.buzz.services;

import hu.doxasoft.buzz.entities.OutputLine;
import hu.doxasoft.buzz.enums.OutputCSVHeaders;

import java.io.*;

public class OutputFileWriterService {
    private static final String LINE_FORMAT = "\"%s\";\"%s\";\"%s\"\n";
    private Writer writer;

    public OutputFileWriterService(String outputFile) throws IOException {
        writer = new FileWriter(outputFile);
        writer.append(String.format(
                LINE_FORMAT,
                OutputCSVHeaders.LineNumber,
                OutputCSVHeaders.Status,
                OutputCSVHeaders.Message
        ));
    }

    public void append(OutputLine line) throws IOException {
        writer.append(String.format(LINE_FORMAT, line.getLineNumber(), line.getStatus(), line.getMessage()));
    }

    public void close() throws IOException {
        writer.close();
    }

}
