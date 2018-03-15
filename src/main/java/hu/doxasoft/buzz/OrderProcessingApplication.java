package hu.doxasoft.buzz;

import hu.doxasoft.buzz.entities.Order;
import hu.doxasoft.buzz.entities.OrderItem;
import hu.doxasoft.buzz.enums.InputCSVHeaders;
import hu.doxasoft.buzz.enums.OrderItemStatus;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class OrderProcessingApplication {
    private static final String CONFIG_FILE_NAME = "hibernate.cfg.xml";
    private static final String INPUT_FILE_NAME = "data.csv";
    private static final String OUTPUT_FILE_NAME = "out.csv";

    public static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : INPUT_FILE_NAME;
        String outputFile = args.length > 1 ? args[1] : OUTPUT_FILE_NAME;
        OrderProcessingApplication app = new OrderProcessingApplication();

        try {
            app.run(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run(String inputFile, String outputFile) throws IOException {
        System.out.println("Hello World!");

        Iterable<CSVRecord> records = readInputFile(inputFile);
        Writer out = new FileWriter(outputFile);
        for (CSVRecord record : records) {
            processRecord(record);
        }
    }

    private void processRecord(CSVRecord record) {
        int lineNumber = Integer.parseInt(record.get(InputCSVHeaders.LineNumber));
        Order order = getOrder(record);
        OrderItem item = getOrderItem(record);
        order.addItem(item);
    }

    private OrderItem getOrderItem(CSVRecord record) {
        return new OrderItem(
                Integer.parseInt(record.get(InputCSVHeaders.OrderItemId)),
                Double.parseDouble(record.get(InputCSVHeaders.SalePrice)),
                Double.parseDouble(record.get(InputCSVHeaders.ShippingPrice)),
                record.get(InputCSVHeaders.SKU),
                OrderItemStatus.valueOf(record.get(InputCSVHeaders.Status))
        );
    }

    private Order getOrder(CSVRecord record) {
        // TODO load or create (according to the specifications: successful load means error)
        return new Order();
    }

    private Iterable<CSVRecord> readInputFile(String inputFile) throws IOException {
        Reader in = null;
        try {
            in = new FileReader(inputFile);
        } catch (FileNotFoundException e) {
            System.err.format("\nThe input file does not exist! (%s)\n\n", inputFile);
            throw e;
        }

        try {
            return CSVFormat.RFC4180.withHeader(InputCSVHeaders.class).parse(in);
        } catch (IOException e) {
            System.err.format("\nThe input file was not unparsable! (%s)\n\n", inputFile);
            throw e;
        }

    }
}
