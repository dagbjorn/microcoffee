package study.microcoffee.location.repository;

public interface LocationRepository {

    Object findNearestCoffeeShop(double latitude, double longitude, long maxDistance);
}
