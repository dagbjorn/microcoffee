package study.microcoffee.location.repository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Repository
public class MongoLocationRepository implements LocationRepository {

    private final MongoDbFactory mongo;

    @Autowired
    public MongoLocationRepository(MongoDbFactory mongo) {
        this.mongo = mongo;
    }

    public Object findNearestCoffeeShop(double latitude, double longitude, long maxDistance) {
        DBCollection collection = mongo.getDb().getCollection("coffeeshop");

//      {location: {$near: {$geometry: {type:        'Point',
//                                      coordinates: {longitude, latitude}},
//                          $maxDistance: maxDistance}
//                 }
//      }

        DBObject coffeeShop = collection.findOne(new BasicDBObject( //
            "location", //
            new BasicDBObject( //
                "$near", //
                new BasicDBObject( //
                    "$geometry", //
                    new BasicDBObject("type", "Point").append("coordinates", Arrays.asList(longitude, latitude))) //
                .append("$maxDistance", maxDistance))));

        return coffeeShop;
    }
}
