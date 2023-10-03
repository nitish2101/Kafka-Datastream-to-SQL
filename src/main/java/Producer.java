import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class Producer {

    public static void main(String[] args) {


        String topic = "demo_topics";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        Scanner sc = new Scanner(System.in);

        for(int i=0;i<10;i++){
            ProducerRecord<String,String> producerRecord =
                    new ProducerRecord<>(topic,i);
            try{
                producer.send(producerRecord);
            }catch(Exception e){
                System.out.println("Error: "+e);
            }
        }



        producer.flush();
        producer.close();

    }
}
