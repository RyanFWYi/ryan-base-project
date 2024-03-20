package base;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class demo {

    @Cacheable
    public String demo() {
        return "demo";
    }

}
