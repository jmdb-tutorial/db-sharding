package jmdbtutorial.postgres.shard;

import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.floorMod;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

public class Test_ShardingDistribution {


    @Test
    public void should_distribute_based_on_hash() {

        String shopId_1 = "shop-A";
        List<Order> orders_shop_1 = generateOrders(shopId_1, 10000);

        String shopId_2 = "shop-B";
        List<Order> orders_shop_2 = generateOrders(shopId_2, 1000);

        ShardedInMemoryDb inMemoryDb = new ShardedInMemoryDb(8);
        Stream.of(orders_shop_1, orders_shop_2)
                .flatMap(List::stream)
                .collect(toList())
                .forEach(inMemoryDb::insertOrder);

        debugShards(inMemoryDb);
    }

    private void debugShards(ShardedInMemoryDb inMemoryDb) {
        for (Shard shard : inMemoryDb.getShards()) {
            out.println(format("Shard [%d] ->", shard.shardKey));
            for (Shop shop : shard.getShops()) {
                out.println(format("  -> Shop [%s] -> Order Count = %d", shop.id, shop.orderCount()));
            }
        }
    }

    private static void distributeOrdersToShards(ShardedInMemoryDb inMemoryDb,
                                                 List<Order> orders) {
        for (Order order : orders) {
            inMemoryDb.insertOrder(order);
        }
    }


    private static List<Order> generateOrders(String shopId, int numberOfOrders) {
        List<Order> orders = new ArrayList<>();

        for (int orderNumber = 0; orderNumber < numberOfOrders; orderNumber++) {
            orders.add(new Order(shopId, shopId + orderNumber));
        }

        return orders;
    }

    public static class OrderShardKey {
        public final String shopId;
        public final String orderNumber;

        public OrderShardKey(String shopId, String orderNumber) {
            this.shopId = shopId;
            this.orderNumber = valueOf(orderNumber);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderShardKey orderShardKey = (OrderShardKey) o;
            return shopId.equals(orderShardKey.shopId) &&
                    orderNumber.equals(orderShardKey.orderNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shopId, orderNumber);
        }
    }

    public static class Order {
        public final OrderShardKey shardKey;

        public Order(String shopId, String orderId) {
            shardKey = new OrderShardKey(shopId, orderId);
        }

        public String getShopId() {
            return shardKey.shopId;
        }

        public String getOrderNumber() {
            return shardKey.orderNumber;
        }
    }

    public static class ShardedInMemoryDb {

        Map<Integer, Shard> shards = new HashMap<>();

        public ShardedInMemoryDb(int numberOfShards) {
            for (int i = 0; i <= numberOfShards; i++) {
                int shardKey = i + 1;
                shards.put(shardKey, new Shard(shardKey));
            }
        }

        private Integer calculateShardKey(Object shardKey) {
            int hashCode = shardKey.hashCode();
            return floorMod(hashCode, numberOfShards()) + 1;
        }


        public int numberOfShards() {
            return shards.size();
        }

        public Shard getShard(Integer shardKey) {
            return shards.get(shardKey);
        }

        public Collection<Shard> getShards() {
            return shards.values();
        }

        private void insertOrder(Order order) {
            Integer shardKey = calculateShardKey(order.shardKey);
            Shard shard = getShard(shardKey);
            if (!shard.containsShop(order.getShopId())) {
                shard.registerShop(order.getShopId());
            }
            shard.getShop(order.getShopId())
                    .addOrder(order);
        }

    }

    public static class Shard {
        Map<String, Shop> shops = new HashMap<>();
        public final Integer shardKey;

        public Shard(Integer shardKey) {

            this.shardKey = shardKey;
        }

        public boolean containsShop(String shopId) {
            return shops.containsKey(shopId);
        }

        public void registerShop(String shopId) {
            shops.put(shopId, new Shop(shopId));
        }

        public Shop getShop(String shopId) {
            return shops.get(shopId);
        }

        public Collection<Shop> getShops() {
            return shops.values();
        }
    }

    public static class Shop {
        private final List<Order> orders = new ArrayList<>();

        public final String id;

        public Shop(String id) {
            this.id = id;
        }

        public void addOrder(Order order) {
            orders.add(order);
        }

        public int orderCount() {
            return orders.size();
        }
    }
}
