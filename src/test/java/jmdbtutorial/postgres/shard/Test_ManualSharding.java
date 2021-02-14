package jmdbtutorial.postgres.shard;

import jmdbtutoroal.postgres.shard.DbShard;
import org.junit.Test;

public class Test_ManualSharding {

    @Test
    public void create_3_shards() {
        String connectionUrl = "jdbc:postgresql://localhost/?user=postgres";
        DbShard shard1 = DbShard.initialise(connectionUrl);


    }


}
