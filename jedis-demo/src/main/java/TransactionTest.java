import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

public class TransactionTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("lllllan.cn", 6379);

        jedis.auth("[password]");

        Transaction multi = jedis.multi();

        try {
            multi.set("name", "lllllan");
            multi.set("age", "22");
            List<Object> exec = multi.exec();

            for (Object object : exec) {
                System.out.println(object.toString());
            }
        } catch (Exception e) {
            multi.discard();
            e.printStackTrace();
        }

    }
}
