package hu.doxasoft.buzz.services;

import hu.doxasoft.buzz.enums.InputCSVHeaders;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class InputFileReaderService {

    public Iterable<CSVRecord> getRecords(String inputFile) throws IOException {
        Reader in;
        try {
            in = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            System.err.format("\nThe input file does not exist! (%s)\n\n", inputFile);
            throw e;
        }

        try {
            return CSVFormat.RFC4180
                    .withHeader(InputCSVHeaders.class)
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(in);
        } catch (IOException e) {
            System.err.format("\nThe input file could not be parsed! (%s)\n\n", inputFile);
            throw e;
        }

    }

}
