package hu.doxasoft.buzz;

import hu.doxasoft.buzz.entities.Order;
import hu.doxasoft.buzz.entities.OrderItem;
import hu.doxasoft.buzz.entities.OutputLine;
import hu.doxasoft.buzz.enums.InputCSVHeaders;
import hu.doxasoft.buzz.enums.OrderItemStatus;
import hu.doxasoft.buzz.enums.ProcessStatus;
import hu.doxasoft.buzz.exceptions.AlreadyInDatabaseException;
import hu.doxasoft.buzz.services.InputFileReaderService;
import hu.doxasoft.buzz.services.OutputFileWriterService;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;
import java.io.*;

public class OrderProcessingApplication {
    private static final String CONFIG_FILE_NAME = "hibernate.cfg.xml";
    private static final String INPUT_FILE_NAME = "data.csv";
    private static final String OUTPUT_FILE_NAME = "out.csv";

    private static SessionFactory factory;

    public static void main(String[] args) {
        String inputFile = args.length > 0 ? args[0] : INPUT_FILE_NAME;
        String outputFile = args.length > 1 ? args[1] : OUTPUT_FILE_NAME;

        OrderProcessingApplication app = new OrderProcessingApplication();
        try {
            app.run(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            factory.close();
        }
    }

    private void run(String inputFile, String outputFile) throws Exception {
        init();

        Iterable<CSVRecord> records = new InputFileReaderService().getRecords(inputFile);
        OutputFileWriterService out = new OutputFileWriterService(outputFile);
        try {
            for (CSVRecord record : records) {
                OutputLine line = processRecord(record);
                out.append(line);
            }
        } finally {
            out.close();
        }
    }

    private void init() throws Exception {
        File configFile = new File(CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            throw new Exception(String.format("The configuration file does not exist! (%s)", CONFIG_FILE_NAME));
        }
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(configFile) // configures settings from hibernate.cfg.xml
                .build();
        try {
            factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }
    }

    private OutputLine processRecord(CSVRecord record) {
        int lineNumber = Integer.parseInt(record.get(InputCSVHeaders.LineNumber));

        String message;
        try {
            Order order = getOrder(record);
            OrderItem item = getOrderItem(record);
            order.addItem(item);

            message = persistToDB(order, item);
//        } catch (AlreadyInDatabaseException e) {
//            message = e.getMessage();
        } catch (Exception e) {
            message = e.getMessage();
        }

        ProcessStatus status = "".equals(message) ? ProcessStatus.OK : ProcessStatus.ERROR;

        return new OutputLine(lineNumber, status, message);
    }

    private String persistToDB(Order order, OrderItem item) {
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        try {
            manager.persist(order);
            manager.persist(item);
            manager.getTransaction().commit();
            return "";
        } catch (Exception e) {
            manager.getTransaction().rollback();
            return e.getMessage();
        } finally {
            manager.close();
        }
    }

    private OrderItem getOrderItem(CSVRecord record) throws AlreadyInDatabaseException {
        int orderItemId = Integer.parseInt(record.get(InputCSVHeaders.OrderItemId));
        EntityManager manager = factory.createEntityManager();
        OrderItem item = manager.find(OrderItem.class, orderItemId);
        manager.close();

        if (item != null) {
            String message = String.format("An order item with this ID has already been added. (%d)", orderItemId);
            throw new AlreadyInDatabaseException(message);
        }

        return new OrderItem(
                orderItemId,
                Double.parseDouble(record.get(InputCSVHeaders.SalePrice)),
                Double.parseDouble(record.get(InputCSVHeaders.ShippingPrice)),
                record.get(InputCSVHeaders.SKU),
                OrderItemStatus.valueOf(record.get(InputCSVHeaders.Status))
        );
    }

    private Order getOrder(CSVRecord record) {
        // NOTE: in the task specification it is required to each line has different OrderId
        //       but then there is no way to have an order with multiple OrderItems. So now
        //       I've decided to be smarter than the task but if that is really the desired
        //       functionality than a similar check is needed here than in getOrderItem()

        EntityManager manager = factory.createEntityManager();
        Order order = manager.find(Order.class, Integer.parseInt(record.get(InputCSVHeaders.OrderId)));
        manager.close();

        return order != null ? order : new Order(
                Integer.parseInt(record.get(InputCSVHeaders.OrderId)),
                record.get(InputCSVHeaders.BuyerName),
                record.get(InputCSVHeaders.BuyerEmail),
                record.get(InputCSVHeaders.OrderDate),
                record.get(InputCSVHeaders.Address),
                Integer.parseInt(record.get(InputCSVHeaders.Postcode))
        );
    }
}
