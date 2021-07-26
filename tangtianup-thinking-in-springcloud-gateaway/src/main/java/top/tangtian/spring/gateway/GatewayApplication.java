package top.tangtian.spring.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("path_route", r -> r.path("/get")
//						.filters(f -> f.addRequestHeader("author","tangtian"))
//						.uri("http://httpbin.org"))
//				.route("host_route", r -> r.host("*.myhost.org")
//						.uri("http://httpbin.org"))
//				.route("rewrite_route", r -> r.host("*.rewrite.org")
//						.filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
//						.uri("http://httpbin.org"))
////				.route("hystrix_route", r -> r.host("*.hystrix.org")
////						.filters(f -> f.hystrix(c -> c.setName("slowcmd")))
////						.uri("http://httpbin.org"))
////				.route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
////						.filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
////						.uri("http://httpbin.org"))
////				.route("limit_route", r -> r
////						.host("*.limited.org").and().path("/anything/**")
////						.filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
////						.uri("http://httpbin.org"))
//				.build();
//	}
}
