package top.tangtian.mongoDemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import top.tangtian.mongoDemo.pojo.Users;

public interface UserRepository extends MongoRepository<Users,String> {
}
