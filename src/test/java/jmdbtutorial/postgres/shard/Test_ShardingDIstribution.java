package jmdbtutorial.postgres.shard;

import org.junit.Test;

import java.util.*;

import static java.lang.Math.floorMod;
import static java.lang.String.valueOf;

public class Test_ShardingDIstribution {


    @Test
    public void should_distribute_based_on_hash() {
        Map<Integer, List<Order>> shards = new HashMap<>();
        final int NUM_SHARDS = 8;

        UUID shopId_1 = UUID.randomUUID();
        List<Order> orders = generateOrders(shopId_1, 10000);

        distributeOrdersToShards(shards, NUM_SHARDS, orders);

        debugShards(shards);
    }

    private void debugShards(Map<Integer, List<Order>> shards) {
        for (Integer shardKey : shards.keySet()) {
            List<Order> orders = shards.get(shardKey);
            System.out.println(String.format("Shard [%d] --> Count = %d", shardKey, orders.size()));
        }
    }

    private static void distributeOrdersToShards(Map<Integer, List<Order>> shards, int NUM_SHARDS, List<Order> orders) {
        for (Order order: orders) {
            Integer shardKey = calculateShardKey(order, NUM_SHARDS);
            if (!shards.containsKey(shardKey)) {
                shards.put(shardKey, new ArrayList<>());
            }

            shards.get(shardKey).add(order);
        }
    }

    private static Integer calculateShardKey(Order order, int num_shards) {
        int hashCode = order.hashCode();
        return floorMod(hashCode, num_shards);
    }

    private static List<Order> generateOrders(UUID shopId, int numberOfOrders) {
        List<Order> orders = new ArrayList<>();

        for (int orderNumber = 0; orderNumber < numberOfOrders; orderNumber++) {
            orders.add(new Order(shopId, orderNumber));
        }

        return orders;
    }


    public static class Order {
        public final UUID shopId;
        public final String orderNumber;

        public Order(UUID shopId, int orderNumber) {
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
}
