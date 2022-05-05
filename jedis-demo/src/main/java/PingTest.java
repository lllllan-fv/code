import redis.clients.jedis.Jedis;

public class PingTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("lllllan.cn", 6379);

        jedis.auth("[password]");

        System.out.println(jedis.ping());

    }
}
