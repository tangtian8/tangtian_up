package top.tangtian.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @description
 * @author tangtian
 * @date 2022/12/12 8:04
 */
public class DemoApplicationListener implements ApplicationListener<SpringApplicationEvent> {
    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent) {
        if (springApplicationEvent instanceof ApplicationStartingEvent) {
            System.out.println(" DemoApplicationListener 监听 ApplicationStartingEvent 事件");
        }
    }
}
