# 高性能MYSQL

## mysql架构

### mysql的逻辑架构

![mysql服务器架构逻辑视图](https://img-blog.csdnimg.cn/20181031090752784.png)

### 并发控制

#### 共享锁（读锁）、排它锁（写锁）

#### 锁粒度

表锁

行锁

#### 事务

```mysql
start transaction;
select balance from checking where customer_id = 10233276;
update checking set balance = balance - 200.00 where customer_id = 10233276;
update savings set balance = balance + 200 where customer_id = 10233276;
commit;
```

原子性

一致性

隔离性

#### 隔离级别

什么是脏读

一个事务读到了另一个未提交事务修改过的数据

![](https://cdn.learnku.com/uploads/images/202002/04/32495/Wcv8DTijTL.png!large)



什么是不可重复读

一个事务只能读到另一个已经提交的事务修改过的数据，并且其他事务每对该数据进行一次修改并提交后，该事务都能查询得到最新值。

![](https://cdn.learnku.com/uploads/images/202002/05/32495/YdNemia6wc.png!large)

什么是幻读

一个事务先根据某些条件查询出一些记录，之后另一个事务又向表中插入了符合这些条件的记录，原先的事务再次按照该条件查询时，能把另一个事务插入的记录也读出来。

![](https://cdn.learnku.com/uploads/images/202002/04/32495/0sCtxw1Jno.png!large)

| 隔离级          | 脏读可能性 | 不可重复度可能性 | 幻读可能性 | 加锁读 |
| --------------- | ---------- | ---------------- | ---------- | ------ |
| READ UNCOMMITED | 是         | 是               | 是         | 否     |
| READ COMMIRTED  | 否         | 是               | 是         | 否     |
| REPEATABLE READ | 否         | 否               | 是         | 否     |
| SERIALIZABLE    | 否         | 否               | 否         | 是     |

读未提交

![](https://cdn.learnku.com/uploads/images/202002/05/32495/iL6jfZxiHJ.png!large)

读已提交（READ COMMITTED）

![](https://cdn.learnku.com/uploads/images/202002/05/32495/BsMcuysaIB.png!large)

可重复读（REPEATABLE READ）

![](https://cdn.learnku.com/uploads/images/202002/05/32495/yjRtVOpMBZ.png!large)

可串行化（SERIALIZABLE）

脏读、不可重复读、幻读都不会发生，通过加锁实现（读锁和写锁）。

#### 死锁

