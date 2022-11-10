package top.tangtian.mysqlexercise;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.tangtian.mysqlexercise.mapper")
public class MysqlexerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlexerciseApplication.class, args);
	}

}
