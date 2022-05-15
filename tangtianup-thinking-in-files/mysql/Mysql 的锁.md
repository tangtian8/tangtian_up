# Mysql 的锁

## 锁机制

### 共享锁与排他锁

- 共享锁（读锁）：其他事务可以读，但不能写。
- 排他锁（写锁） ：其他事务不能读取，也不能写。

## 粒度锁

MySQL 不同的存储引擎支持不同的锁机制，所有的存储引擎都以自己的方式显现了锁机制，服务器层完全不了解存储引擎中的锁实现：

- MyISAM 和 MEMORY 存储引擎采用的是表级锁（table-level locking）
- BDB 存储引擎采用的是页面锁（page-level locking），但也支持表级锁
- InnoDB 存储引擎既支持行级锁（row-level locking），也支持表级锁，但默认情况下是采用行级锁。

默认情况下，表锁和行锁都是自动获得的， 不需要额外的命令。

但是在有的情况下， 用户需要明确地进行锁表或者进行事务的控制， 以便确保整个事务的完整性，这样就需要使用事务控制和锁定语句来完成。

## InnoDB行级锁和表级锁

#### InnoDB锁模式

InnoDB 实现了以下两种类型的**行锁**：

- 共享锁（S）：允许一个事务去读一行，阻止其他事务获得相同数据集的排他锁。
  - select * from table lock in share mode。
- 排他锁（X）：允许获得排他锁的事务更新数据，阻止其他事务取得相同数据集的共享读锁和排他写锁。
  - select * from table for update。

为了允许行锁和表锁共存，实现多粒度锁机制，InnoDB 还有两种内部使用的意向锁（Intention Locks），这两种意向锁都是**表锁**：

- 意向共享锁（IS）：事务打算给数据行加行共享锁，事务在给一个数据行加共享锁前必须先取得该表的 IS 锁。
- 意向排他锁（IX）：事务打算给数据行加行排他锁，事务在给一个数据行加排他锁前必须先取得该表的 IX 锁。

下面命令或表都可以查看当前锁的请求：

```mysql
SHOW FULL PROCESSLIST;
SHOW ENGINE INNODB STATUS;
SELECT * FROM information_schema.INNODB_TRX;
SELECT * FROM information_schema.INNODB_LOCKS;
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```



排他锁和共享锁的兼容性：

![](https://pic3.zhimg.com/80/v2-1ded734c93e82d2bb758db003a39d64e_720w.jpg)

InnoDB还支持多粒度（granular）锁定，允许事务同时存在行级锁和表级锁，这种额外的锁方式，称为意向锁（Intention Lock）。意向锁是将锁定的对象分为多个层次（如下图），意向锁意味着事务希望在更细粒度（fine granularity）上进行加锁。

![](https://pic2.zhimg.com/80/v2-3658fa83cd78b7c09d6b060f288fa115_720w.jpg)

#### 一致性非锁定读

一致性的非锁定读（consistent nonlocking read）是指InnoDB通过行多版本控制（multi versioning）的方式来读取当前执行时间数据库中行的数据。如果读取的行正在执行DELETE或UPDATE操作，这时不会去等待行上锁的释放。而是去读取行的一个快照数据（之前版本的数据）。

一个行记录多个快照数据，一般称这种技术为行多版本技术。由此带来的并发控制，称之为多版本并发控制（Multi Version Concurrency Control，MVCC）。

![](https://pic2.zhimg.com/80/v2-5b56e37c782e9f9a17cd867f2442c605_720w.jpg)

称为非锁定读，因为不需要等待访问的行上X锁的释放。实现方式是通过undo段来完成。而**undo**用来在事务中回滚数据，快照数据本身没有额外的开销，也不需要上锁，因为没有事务会对历史数据进行修改操作。非锁定读机制极大地提高了数据库的并发性。

在不同事务隔离级别下，读取的方式不同，并不是在每个事务隔离级别下都是采用非锁定的

在事务隔离级别READ COMMITTED和REPEATABLE READ下，InnoDB使用非锁定的一致性读。但对快照数据的定义不相同。在READ COMMITTED事务隔离级别下，对于快照数据，非一致性读总是读取被锁定行的最新一份快照数据。而在REPEATABLE READ事务隔离级别下，对于快照数据，非一致性读总是读取事务开始时的行数据版本。

#### 自增长与锁

插入操作会依据这个自增长的计数器值加1赋予自增长列。这个实现方式称做AUTO-INC Locking,为了提高插入的性能，锁不是在一个事务完成后才释放，而是在完成对自增长值插入的SQL语句后立即释放。
