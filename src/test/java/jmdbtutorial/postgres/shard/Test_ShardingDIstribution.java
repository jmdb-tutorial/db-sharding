package jmdbtutorial.postgres.shard;

import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.floorMod;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Test_ShardingDIstribution {


    @Test
    public void should_distribute_based_on_hash() {
        Map<Integer, Map<String, List<Order>>> shards = new HashMap<>();
        final int NUM_SHARDS = 8;

        String shopId_1 = "shop-01";
        List<Order> orders_shop_1 = generateOrders(shopId_1, 1000);

        String shopId_2 = "shop-02";
        List<Order> orders_shop_2 = generateOrders(shopId_2, 1000);


        List<Order> allOrders = Stream.of(orders_shop_1, orders_shop_2)
                .flatMap(List::stream)
                .collect(toList());

        distributeOrdersToShards(shards, NUM_SHARDS, allOrders);

        debugShards(shards);
    }

    private void debugShards(Map<Integer, Map<String, List<Order>>> shards) {
        for (Integer shardKey : shards.keySet()) {
            out.println(format("Shard [%d] -->", shardKey));
            Map<String, List<Order>> shops = shards.get(shardKey);
            for (String shopId : shops.keySet()) {
                List<Order> orders = shops.get(shopId);
                out.println(format("  --> Shop [%s] --> Count = %d", shopId.toString(), orders.size()));
            }


        }
    }

    private static void distributeOrdersToShards(Map<Integer, Map<String, List<Order>>> shards,
                                                 int NUM_SHARDS,
                                                 List<Order> orders) {
        for (Order order : orders) {
            Integer shardKey = calculateShardKey(order, NUM_SHARDS);
            if (!shards.containsKey(shardKey)) {
                shards.put(shardKey, new HashMap<>());
            }
            Map<String, List<Order>> shops = shards.get(shardKey);
            if (!shops.containsKey(order.shopId)) {
                shops.put(order.shopId, new ArrayList<Order>());
            }
            shops.get(order.shopId).add(order);
        }
    }

    private static Integer calculateShardKey(Order order, int num_shards) {
        int hashCode = order.hashCode();
        return floorMod(hashCode, num_shards);
    }

    private static List<Order> generateOrders(String shopId, int numberOfOrders) {
        List<Order> orders = new ArrayList<>();

        for (int orderNumber = 0; orderNumber < numberOfOrders; orderNumber++) {
            orders.add(new Order(shopId, shopId + orderNumber));
        }

        return orders;
    }


    public static class Order {
        public final String shopId;
        public final String orderNumber;

        public Order(String shopId, String orderNumber) {
            this.shopId = shopId;
            this.orderNumber = valueOf(orderNumber);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order order = (Order) o;
            return shopId.equals(order.shopId) &&
                    orderNumber.equals(order.orderNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shopId, orderNumber);
        }
    }

    public static class Shard {

    }

    public static class Shop {

    }
}
