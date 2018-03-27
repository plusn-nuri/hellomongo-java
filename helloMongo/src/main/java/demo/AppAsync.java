package demo;

import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class AppAsync {

        public static void main(String[] args) {
                try {
                        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://localhost"));

                        CodecRegistry codecRegistry = fromRegistries(MongoClients.getDefaultCodecRegistry(),
                                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

                        MongoDatabase database = mongoClient.getDatabase("demo");

                        MongoCollection<Pet> petsCollection = database.getCollection("pets", Pet.class)
                                        .withCodecRegistry(codecRegistry);

                        Pet pet = new Pet("big", PetKind.BIRD, 2);

                        petsCollection.insertOne(pet, operationCompleteCallback);

                        petsCollection.find(eq("name", "big")).first(petResultCallback);

                        petsCollection.find(and(gte("legCount", 1), lt("legCount", 7))).forEach(printBlock,
                                        new SingleResultCallback<Void>() {

                                                public void onResult(Void result, Throwable t) {
                                                        System.out.println("Consumed last result");
                                                }
                                        });

                        System.in.read();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private static SingleResultCallback<Void> operationCompleteCallback = new SingleResultCallback<Void>() {
                public void onResult(Void result, Throwable t) {
                        System.out.println("operationCompleteCallback:: " + result.toString());
                }
        };

        final static SingleResultCallback<Pet> petResultCallback = new SingleResultCallback<Pet>() {
                public void onResult(Pet result, Throwable t) {
                        System.out.println("SingleResultCallback:: " + result);
                }
        };

        final static Block<Pet> printBlock = new Block<Pet>() {
                public void apply(Pet pet) {
                        System.out.println("printBlock:: " + pet);
                }
        };

}