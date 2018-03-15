package hu.doxasoft.buzz;

public class OrderProcessingApplication {
    private static final String CONFIG_FILE_NAME = "hibernate.cfg.xml";
    private static final String INPUT_FILE_NAME = "data.csv";
    private static final String OUTPUT_FILE_NAME = "out.csv";

    public static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : INPUT_FILE_NAME;
        String outputFile = args.length > 1 ? args[1] : OUTPUT_FILE_NAME;
        OrderProcessingApplication app = new OrderProcessingApplication();
        app.run(inputFile, outputFile);
    }

    private void run(String inputFile, String outputFile) {
        System.out.println("Hello World!");
    }
}
