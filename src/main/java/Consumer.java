import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    static String topic = "demo_java";


    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id","first-group");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("demo_topic"));

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    // Insert data into PostgreSQL
                    String sql = "INSERT INTO kafka (message) VALUES (?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                        preparedStatement.setString(1, record.value());
                        preparedStatement.executeUpdate();
                        System.out.println("Received and stored message: " + record.value());
                    } catch (SQLException e) {
                        System.out.println("SQL Error: "+e);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
