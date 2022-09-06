package com.imooc.manager.service;

import com.imooc.entity.Product;
import com.imooc.entity.enums.ProductStatus;
import com.imooc.manager.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品服务类
 */
@Service
public class ProductService {
    private static Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repository;

    /**
     * 创建产品
     *
     * @param product
     * @return
     */
    public Product addProduct(Product product) {
        LOG.debug("创建产品，参数:{}", product);
        // 数据校验
        checkProduct(product);

        //设置默认值
        setDefault(product);
        Product result = repository.save(product);

        LOG.debug("创建产品, 结果:{}", result);
        return result;
    }

    /**
     * 设置默认值
     * 创建时间、更新时间
     * 投资步长、锁定期、状态
     *
     * @param product
     */
    private void setDefault(Product product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() == null) {
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() == null) {
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() == null) {
            product.setLockTerm(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AUDITING.name());
        }
    }

    /**
     * 产品数据校验
     * 1. 非空数据
     * 2. 收益率要0-30以内
     * 3. 投资步长需为整数
     *
     * @param product
     */
    private void checkProduct(Product product) {
        Assert.notNull(product.getId(), "编号不可为空");
        //其他非空校验

        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate()) < 0 && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误");

        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) == 0, "投资步长需为整数");

    }

    /**
     * 查询单个产品
     *
     * @param id 产品编号
     * @return 返还对应产品或者null
     */
    public Product findOne(String id) {
        Assert.notNull(id, "需要产品编号参数");
        LOG.debug("查询单个产品，id={}", id);

        Product product = repository.findById(id).orElse(null);

        LOG.debug("查询单个产品,结果={}", product);

        return product;
    }

    /**
     * 分页查询产品
     *
     * @param idList
     * @param minRewardRate
     * @param maxRewardRate
     * @param statusList
     * @param pageable
     * @return
     */
    public Page<Product> query(List<String> idList,
                               BigDecimal minRewardRate, BigDecimal maxRewardRate,
                               List<String> statusList,
                               Pageable pageable) {

        LOG.debug("查询产品,idList={},minRewardRate={},maxRewardRate={},statusList={},pageable={}", idList, minRewardRate, maxRewardRate, statusList, pageable);
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression<String> idCol = root.get("id");
                Expression<BigDecimal> rewardRateCol = root.get("rewardRate");
                Expression<String> statusCol = root.get("status");
                List<Predicate> predicates = new ArrayList<>();
                if (idList != null && idList.size() > 0) {
                    predicates.add(idCol.in(idList));
                }
                if (minRewardRate != null && BigDecimal.ZERO.compareTo(minRewardRate) < 0) {
                    predicates.add(cb.ge(rewardRateCol, minRewardRate));
                }
                if (maxRewardRate != null && BigDecimal.ZERO.compareTo(maxRewardRate) < 0) {
                    predicates.add(cb.le(rewardRateCol, maxRewardRate));
                }
                if (statusList != null && statusList.size() > 0) {
                    predicates.add(statusCol.in(statusList));
                }

                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<Product> page = repository.findAll(specification, pageable);

        LOG.debug("查询产品,结果={}", page);
        return page;
    }

    public static void main(String[] args){
        System.out.println("create".hashCode());
        System.out.println("findOne22".hashCode());
        System.out.println("createException".hashCode());
    }

}
