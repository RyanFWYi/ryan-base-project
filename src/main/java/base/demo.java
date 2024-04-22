package base;

import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class demo {

    public String demo() {
        return "demo";
    }

    public static void main(String[] args) {
//        Object o = new Object();
//        Thread thread = new Thread(() -> {
//            synchronized (o) {
//                System.out.println("新线程获取锁时间：" + LocalDateTime.now() +
//                        "新线程名称：" + Thread.currentThread().getName());
//                try {
//                    o.wait(1000);
//                    System.out.println("新线程获取释放锁锁时间：" + LocalDateTime.now() +
//                            "新线程名称：" + Thread.currentThread().getName());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        synchronized (o) {
//            System.out.println("主线程获取释放锁锁时间： " + LocalDateTime.now() +
//                    "主线程名称：" + Thread.currentThread().getName());
//        }

        List<Long> aaa = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);
        aaa.iterator().forEachRemaining(v -> {
            Map<Long, Long> map = new HashMap<>();
            if (MapUtil.isEmpty(map)) map.put(v, v);
            else map.put(v, v);
            if (v.equals(3L)) {
                aaa.remove(3L);
            }
            System.out.println(v + 1);
        });
        System.out.println(aaa);

    }

}
