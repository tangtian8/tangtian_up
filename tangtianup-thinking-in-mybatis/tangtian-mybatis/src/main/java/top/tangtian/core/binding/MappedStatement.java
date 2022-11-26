package top.tangtian.core.binding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author tangtian
 * @description
 * @date 2022/11/25 8:21
 */
public class MappedStatement {
    private Map<String,List<MapperMethod>> mapperMethods = new HashMap<>();

    public MappedStatement(Map<String,List<MapperMethod>> mapperMethods) {
        this.mapperMethods = mapperMethods;
    }

    public MapperMethod getSql(String nameSpaceId,String sqlId){
        if (nameSpaceId == null){
            throw new RuntimeException("sqlId is null");
        }
        List<MapperMethod> mapperMethods = this.mapperMethods.get(nameSpaceId);
        Optional<MapperMethod> any = mapperMethods.stream().filter(e -> sqlId.equals(e.getId())).findAny();
        if (any.isPresent()){
            return any.get();
        }else {
            throw new RuntimeException("sqlId is not exit");
        }
    }

    public MapperMethod getSql(String sqlIndex){
        if (sqlIndex == null){
            throw new RuntimeException();
        }
        int i = sqlIndex.lastIndexOf(".");
        String nameSpaceId = sqlIndex.substring(0,i);
        String sqlId = sqlIndex.substring(i + 1);
        return getSql(nameSpaceId,sqlId);
    }
}
