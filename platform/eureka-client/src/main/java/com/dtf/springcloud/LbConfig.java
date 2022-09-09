package com.dtf.springcloud;

import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author dtf
 * @desc load balance config
 * @date 2022-06-13 10:38
 */
@Configuration
@RibbonClient(name = "eureka-client", configuration = RandomRule.class)
public class LbConfig {
//
//    @Bean
//    public IRule getLoadBalancer() {
//        return new RandomRule();
//    }

}
