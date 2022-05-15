# InnoDB存储引擎

## Mysql体系结构和存储引擎

![](https://images2015.cnblogs.com/blog/540235/201609/540235-20160927083854563-2139392246.jpg)

（1） Connectors指的是不同语言中与SQL的交互 
（2）Management Serveices & Utilities： 系统管理和控制工具，例如备份恢复、Mysql复制、集群等 
（3）Connection Pool: 连接池：管理缓冲用户连接、用户名、密码、权限校验、线程处理等需要缓存的需求 
（4）SQL Interface: SQL接口：接受用户的SQL命令，并且返回用户需要查询的结果。比如select from就是调用SQL Interface 
（5）Parser: 解析器，SQL命令传递到解析器的时候会被解析器验证和解析。解析器是由Lex和YACC实现的，是一个很长的脚本， 主要功能： 
a . 将SQL语句分解成数据结构，并将这个结构传递到后续步骤，以后SQL语句的传递和处理就是基于这个结构的 
b. 如果在分解构成中遇到错误，那么就说明这个sql语句是不合理的 
（6）Optimizer: 查询优化器，SQL语句在查询之前会使用查询优化器对查询进行优化。他使用的是“选取-投影-联接”策略进行查询。

（7） Cache和Buffer（高速缓存区）： 查询缓存，如果查询缓存有命中的查询结果，查询语句就可以直接去查询缓存中取数据。 
通过LRU算法将数据的冷端溢出，未来得及时刷新到磁盘的数据页，叫脏页。 
这个缓存机制是由一系列小缓存组成的。比如表缓存，记录缓存，key缓存，权限缓存等 
（8）Engine ：存储引擎。存储引擎是MySql中具体的与文件打交道的子系统。也是Mysql最具有特色的一个地方。 
Mysql的存储引擎是插件式的。它根据MySql AB公司提供的文件访问层的一个抽象接口来定制一种文件访问机制（这种访问机制就叫存储引擎） 

## InnoDB存储引擎

### InnoDB存储引擎概述

从Mysql5.5开始默认的表存储引擎

完整支持ACID事务

行锁设计、只是MVCC、支持外键、提供一致性非锁定读。

创始人：Heikki Tuuri-芬兰赫尔辛基（和Linux创始人Linus是校友）

### InnoDB体系架构

![](https://upload-images.jianshu.io/upload_images/5304392-451053fb97782ac9.PNG?imageMogr2/auto-orient/strip|imageView2/2/w/766/format/webp)

内存池负责如下工作：

1. 维护所有进程、线程需要访问的多个内部数据结构
2. 缓存磁盘上的数据，方便快速读取， 同时在对磁盘文件的数据修改之前在这里缓存
3. 重做日志（redo log）缓冲

后台线程主要工作

1. 刷新内存池中的数据
2. 将已修改的数据文件刷新到磁盘文件

### 后台线程

#### Master Thread

主要负责将缓冲池中的数据异步刷新到磁盘，保证数据的一致性

#### IOThread

主要负责IO请求的回调处理

InnoDB 1.0版本开始 readthread 和 writethread 增大到4个

使用：innodb_read_io_threads 和 innodb_write_io_threads参数进行设置

```mysql
show variables like 'innodb_version';
```

```mysql
show variables like 'innodb_%io_threads'
```

```mysql
show engine innodb status;

 =====================================
 2021-08-30 23:01:49 140360359204608 INNODB MONITOR OUTPUT
 =====================================
 Per second averages calculated from the last 9 seconds
 -----------------
 BACKGROUND THREAD
 -----------------
 srv_master_thread loops: 2382 srv_active, 0 srv_shutdown, 1278220 srv_idle
 srv_master_thread log flush and writes: 0
 ----------
 SEMAPHORES
 ----------
 OS WAITARRAY INFO: reservation count 3893
 OS WAIT ARRAY INFO: signal count 3990
 RW-shared spins 0, rounds 0, OS waits 0
 RW-excl spins 0, rounds 0, OS waits 0
 RW-sx spins 0, rounds 0, OS waits 0
 Spin rounds per wait: 0.00 RW-shared, 0.00 RW-excl, 0.00 RW-sx
 ------------
 TRANSACTIONS
 ------------
 Trx id counter 592987
 Purge done for trx's n:o < 592987 undo n:o < 0 state: running but idle
 History list length 0
 LIST OF TRANSACTIONS FOR EACH SESSION:
 ---TRANSACTION 421836521586928, not started
 0 lock struct(s), heap size 1136, 0 row lock(s)
 ---TRANSACTION 421836521586072, not started
 0 lock struct(s), heap size 1136, 0 row lock(s)
 ---TRANSACTION 421836521585216, not started
 0 lock struct(s), heap size 1136, 0 row lock(s)
 ---TRANSACTION 421836521584360, not started
 0 lock struct(s), heap size 1136, 0 row lock(s)
 --------
 FILE I/O
 --------
 I/O thread 0 state: waiting for completed aio requests (insert buffer thread)
 I/O thread 1 state: waiting for completed aio requests (log thread)
 I/O thread 2 state: waiting for completed aio requests (read thread)
 I/O thread 3 state: waiting for completed aio requests (read thread)
 I/O thread 4 state: waiting for completed aio requests (read thread)
 I/O thread 5 state: waiting for completed aio requests (read thread)
 I/O thread 6 state: waiting for completed aio requests (write thread)
 I/O thread 7 state: waiting for completed aio requests (write thread)
 I/O thread 8 state: waiting for completed aio requests (write thread)
 I/O thread 9 state: waiting for completed aio requests (write thread)
 Pending normal aio reads: [0, 0, 0, 0] , aio writes: [0, 0, 0, 0] ,
  ibuf aio reads:, log i/o's:, sync i/o's:
 Pending flushes (fsync) log: 0; buffer pool: 0
 3146 OS file reads, 183548 OS file writes, 127385 OS fsyncs
 0.00 reads/s, 0 avg bytes/read, 0.00 writes/s, 0.00 fsyncs/s
 -------------------------------------
 INSERT BUFFER AND ADAPTIVE HASH INDEX
 -------------------------------------
 Ibuf: size 1, free list len 0, seg size 2, 0 merges
 merged operations:
  insert 0, delete mark 0, delete 0
 discarded operations:
  insert 0, delete mark 0, delete 0
 Hash table size 34679, node heap has 2 buffer(s)
 Hash table size 34679, node heap has 6 buffer(s)
 Hash table size 34679, node heap has 2 buffer(s)
 Hash table size 34679, node heap has 6 buffer(s)
 Hash table size 34679, node heap has 6 buffer(s)
 Hash table size 34679, node heap has 5 buffer(s)
 Hash table size 34679, node heap has 3 buffer(s)
 Hash table size 34679, node heap has 13 buffer(s)
 0.00 hash searches/s, 0.00 non-hash searches/s
 ---
 LOG
 ---
 Log sequence number          515822477
 Log buffer assigned up to    515822477
 Log buffer completed up to   515822477
 Log written up to            515822477
 Log flushed up to            515822477
 Added dirty pages up to      515822477
 Pages flushed up to          515822477
 Last checkpoint at           515822477
 127083 log i/o's done, 0.00 log i/o's/second
 ----------------------
 BUFFER POOL AND MEMORY
 ----------------------
 Total large memory allocated 137035776
 Dictionary memory allocated 1634874
 Buffer pool size   8192
 Free buffers       4186
 Database pages     3963
 Old database pages 1442
 Modified db pages  0
 Pending reads      0
 Pending writes: LRU 0, flush list 0, single page 0
 Pages made young 3189, not young 10369
 0.00 youngs/s, 0.00 non-youngs/s
 Pages read 2843, created 1341, written 41715
 0.00 reads/s, 0.00 creates/s, 0.00 writes/s
 No buffer pool page gets since the last printout
 Pages read ahead 0.00/s, evicted without access 0.00/s, Random read ahead 0.00/s
 LRU len: 3963, unzip_LRU len: 0
 I/O sum[0]:cur[0], unzip sum[0]:cur[0]
 --------------
 ROW OPERATIONS
 --------------
 0 queries inside InnoDB, 0 queries in queue
 0 read views open inside InnoDB
 Process ID=1, Main thread ID=140361046681344 , state=sleeping
 Number of rows inserted 34423, updated 1896, deleted 171, read 23473784
 0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 0.00 reads/s
 Number of system rows inserted 16450, updated 7988, deleted 15786, read 3040066
 0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 0.00 reads/s
 ----------------------------
 END OF INNODB MONITOR OUTPUT
 ============================
 
```

#### Purge Thread

事务被提交后、使用的undolog可能不在需要，需要PurgeThread回收使用并分配的undo页面。

InnoDB1.2开始。InnoDB支持多个PurgeThread

```mysql
show variables like 'innodb_purge_threads'
```

#### Page Cleaner Thread

将之前版本中脏页的刷新操作都放在单独的线程中完成

### 内存

#### 缓冲池

在数据库系统中，由于CPU速度与磁盘速度之间的鸿沟，基于磁盘的数据库系统通常使用缓冲池技术来提高数据库整体性能

对于数据库的页的操作，首先修改在缓冲池的页，然后以一定的频率刷新到磁盘

页从缓冲池刷新到磁盘的操作是通过Checkpoint的机制刷新到磁盘

缓冲池配置通过参数innodb_buffer_pool_size来设置

```mysql
show variables like 'innodb_buffer_pool_size'
innodb_buffer_pool_size	134217728
```

缓冲池缓存的数据类型

1. 索引页
2. 数据页
3. undo页
4. 插入缓冲
5. 自适应哈希索引
6. Innodb存储的锁信息
7. 数据字典信息

![](https://img-blog.csdnimg.cn/20210308234301405.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NhMjFodWFuZw==,size_16,color_FFFFFF,t_70)

允许多个缓冲池实例，每个页通过哈希值平均分配到不同缓冲池的实例中

```mysql
show variables like 'innodb_buffer_pool_instances'
innodb_buffer_pool_instances	1
```

#### LRU、Free、Flush

数据库中的缓冲池是通过LRU（最近最少使用）算法进行管理:最频繁使用的页在LRU列表前端，最少使用的页在LRU列表尾端。当缓冲池不能存放新读取到页时，首先释放LRU尾端的页。

缓冲池页的大小默认16KB

LRU列表加入midpoint位置，新读取到数据，是最新访问的页，并不之间放入LRU列表的首部，放入LRU列表的midpoint位置（midpoint insertion strategy），在默认配置中，该位置在LRU列表长度5/8处，midpoint位置可以通过参数innodb_old_blocks_pct控制

```mysql
show variables like 'innodb_old_blocks_pct'
'innodb_old_blocks_pct', '37'
```

若直接将读取到页放入到LRU的首部,某些SQL操作可能会使缓冲池中的页被刷新出。影响缓冲池的效率（常见的操作为索引或者数据的扫描操作，）

InnoDB存储引擎引入另一个参数进一步管理LRU列表

```mysql
innodb_old_blocks_time
表示页读取到mid位置后需要等待多久才会被加入到LRU列表的热端
```

#### 重做日志缓冲

Innodb首先将重做日志信息放到缓冲区，然后按照一定的频率刷新到重做日志文件。缓冲默认大小8MB由innodb_log_buffer_size控制

重做日志在下列三种 情况下将缓冲池内容刷新到外部磁盘的重做日志文件。

- MasterThread每一秒将重做日志缓冲刷新到重做日志文件
- 每个事务提交时会将重做日志缓冲刷新到重做日志文件
- 当重做日志缓冲池剩余空间小于1/2时候，重做日志缓冲刷新到重做日志文件

#### 额外的内存池

 在InnoDB存储引擎中，对内存的管理通过内存堆的方式进行。在对一些数据结构本身的内存进行分配时，需要从额外的内存池中进行申请，当该区域内存不够时，会从缓冲池中进行申请。

### Checkpoint 技术

缓冲池中的页版本要比磁盘的新，数据库需要将新的版本的页从缓冲池刷新到磁盘。Checkpoint所做的事情是将缓冲池中脏页刷回到磁盘。

事务提交时，先写重做日志，在修改页。（事务持久性的体现）

Checkpoint（检查点）技术目的解决如下问题

1. 缩短数据库的恢复时间；
2. 缓冲池不够时，将脏页刷新到磁盘；
3. 重做日志不可用时，刷新脏页；

Checkpoint每次刷新多少页到磁盘，每次从哪里取脏页，什么时间触发chechPoint。在InnoDB存储引擎内部。有两种

1. sharp CheckPoint
2. fuzzy CheckPoint

SharpCheck发在数据库关闭时候将所有脏页刷新到磁盘。参数innodb_fase_shutdown=1;

Fuzzy Checkpoint

刷新部分脏页

1. MasterThread Checkpoint；
2. FLUSH_LRU_LIST CheckPoint；
3. Async/Sync Flush Checkpoint;
4. Dirty Page too much Checkpoint;

### Master Thread 工作方式



### InnoDB关键特性

- 插入缓冲
- 两次写
- 自适应哈希索引
- 异步IO
- 刷新邻接页

### 启动、关闭和恢复

Innodb_fast_shutdown

1. 0表示在Mysql关闭时，Innodb需要完成full purge和merge insert buffer，并将所有脏页刷新回磁盘
2. 1是参数默认值，不需要full purge和 merge insert buffer  操作，缓冲池的一些脏页数据会刷新回磁盘
3. 2表示不完成full purge 和 merge insert buffer 操作。不用缓冲池的数据到磁盘。日志写入日志文件。这样事务数据不会丢失，但是下次启动会进行恢复数据

innodb_force_recovery  6个非零值：1~6。大的数字表示包含了前面所有小数字表示的影响。

- 1：忽略检查到的corrupt页
- 2：阻止Master Thread 线程的运行，如Master Thread 线程需要进行full purge 操作，这会导致crash
- 3：不进行事务的回滚操作
- 4：不进行插入缓冲的合并操作
- 5：不查看撤销日志（undo Log），InnoDB存储引擎会将未提交事务视为已提交
- 6：不进行前滚操作

## 文件

### 参数文件

#### 动态参数

动态参数可以在Mysql实例运行中进行更改

有些动态参数只能在会话中进行更改，如autocommit

有些动态参数在整个实例生命周期都会生效，如binlog_cache_size

有些动态参数既可以在会话中，又可以在整个实例的生命周期生效，如read_buffer_size

可以通过SET命名对动态参数值进行更改

#### 静态参数

静态参数在整个实例生命周期不得进行更改

### 日志文件

#### 错误日志

错误日志文件对Mysql的启动、运行、关闭进行了记录。

查找错误日志文件目录

```mysql
show variables like 'log_error'
```

#### 二进制日志

二进制日志记录了对MYSQL数据库执行更改的所有操作

##### 二进制日志的作用

恢复、复制、审计

使用事务的表存储引擎,所有未提交的二进制日志会被记录到一个缓存中去，等该事务提交时直接将缓冲中的二进制日志写入二进制文件中。

##### 参数

binlog_cache_size 缓冲的大小

binlog_cache_use  记录使用缓冲写二进制次数

binlog_cache_disk_use 记录使用临时文件写二进制的次数

sync_binlog=[N] 表示每次写缓冲多少次就同步到磁盘

innodb_support_xa  确保二进制日志和InnoDb存储引擎数据文件同步

log-slave-update 用于从节点的复制

##### 二进制文件的格式

binlog_format（动态参数）

对于复制又一定要求：

如果在主服务器运行rand,uuid等函数，可能导致主从服务器表上的数据不一致

InnoDB存储引擎的默认事务隔离级别是REPEATABLE READ ：如果是更轻力度的隔离级别会导致数据丢失的现象，导致主从配置不一样。

###### statement

二进制文件记录的是逻辑SQL语句

###### row 

记录行的更改情况，如果设置了格式为row，可以将InnoDB的事务隔离级别设置为READCOMMITTED，获得更好的并发性

###### mixed

混合模式

#### 慢查询日志

慢查询日志可以帮助DBA定位可能存在的问题的SQL语句，进行SQL语句层面的优化。

在Mysql启动时候设置一个阈值，将运行时间超过该值的所有SQL语句都记录到慢查询日志文件中

```mysql
show variables like 'long_query_time'
```

慢查询有关的参数

log_queries_not_using_indexes：如果运行的SQL没有使用索引，Mysql会将这条SQL语句记录到慢查询日志文件

log_throttle_queries_not_using_indexes:每分钟记录到slow log且未使用sql语句的次数

mysqldumpslow工具可以查看慢查询日志

mysqldumpslow slow.log

参数log_output 指定了慢查询输出格式默认是FILE，可以将它设置为TABLE（动态全局参数）

#### 查询日志

查询日志记录了所有对Mysql数据库的请求信息。默认文件名：主机名.log

查看一个查询日志

tail nineyou.log
## 套接字文件
UNIX 系统在本来连接MYSQL可以采用UNIX套接字方式 文件位置/tmp目录
## pid文件
当Mysql 实例启动时， 会将自己的进展ID写入一个文件中，该文件为pid文件
## 表结构文件
frm为后缀的文件,该文件记录了表结构的定义
frm也用来存放视图的定义
## Innodb存储引擎文件
### 表空间文件
InnoDB将存储的数据按照表空间（tablespace）进行存放
名为ibdata1文件
#### 参数
innodb_data_file_path：基于InnoDB存储引擎的表数据会记录共享表空间
innodb_file_per_table:每个基于InnoDB存储引擎的表产生一个独立的表空间【独立表空间命名规则】表名.ibd
独立表空间文件存储该表的数据、索引、和插入缓冲BITMAP等信息。其余信息存放在默认的表空间中。![](https://img-blog.csdnimg.cn/20190908174057551.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2xfZG9uZ3lhbmc=,size_16,color_FFFFFF,t_70)

### 重做日志文件
ib_logfile和ib_logfile1
innodb存储引擎文件，或是叫重做日志文件（redo log file）
每个InnoDb存储引擎至少又一个重做日志文件组（group)

重做日志文件参数
innodb_log_file_size 每个重做日志文件的大小
innodb_log_file_in_group 日志文件组中重做日志文件的数量
innodb_mirrored_log_groups 日志镜像文件组的数量 默认是1
innodb_log_group_home_dir 指定日志文件所在路径


### 二进制日志和存储引擎的日志的区别
1.  二进制日志会记录所有与mysql数据库有关的日志记录，InnoDB存储引擎的重做日志只记录存储引擎本身的事务日志
2.  记录的内容：二进制记录的关于事务的具体操作，逻辑日志。InnoDB的重做日志记录的是关于每个页（page）的更改的物理情况
3.  写入的时间：二进制日志文件在事务提交前进行提交，只写磁盘一次，重做日志条码在事务进行过程中不断写入到重做日志文件。

### 重做日志文件格式
1. 重做日志先写入一个重做日志缓冲（redo log buffer） ，然后按照一定的条件顺序写入日志文件。

2. 重做日志文件缓冲写入，按照512个字节，按照一个扇区的大小进行写入，是扇区写入的最小单位，可以保证写入必定成功。重做日志文件写入过程不需要doublewrite

3. innodb_flush_log_at_trx_commit:表示在提交操作时候,处理重做日志的方式
   
	 	0:当事务提交时，不将事务的重做日志写入磁盘上的日志文件，等待主线程每秒的刷新
    	1：在提交时重做日志文件缓冲同步到磁盘，并伴有fsync的调用
		2:重做日志异步写到磁盘。不能保证执行commit时肯定写入重做日志文件。
	
4. 保证持久性innodb_flush_log_at_trx_commit 需要设置为1

## 表
### 索引组织表
在 InnoDB存储引擎表中，每张表都有个主键，如果创建表没有显式地定义主键，则会
 首先判断表中是否有非空唯一索引，如果有，则该列为主键
 如果没有，InnoDB存储引擎自动创建一个6字节大小的指针
 主键的选择是根据定义索引的顺序
### InnoDB逻辑存储结构
表空间：所有数据被逻辑的存放在一个空间中。
表空间的组成：段、区、页（block）

![](https://images2015.cnblogs.com/blog/524341/201604/524341-20160416104932379-1446683950.jpg)
#### 表空间
没有启用`innodb_file_per_table`参数，所有数据存放在共享表ibdata1中。
启用`innodb_file_per_table`参数，单独表空间内存中只放数据，索引，和插入缓冲Bitmap页。其他（回滚信息，插入缓冲索引页、系统事务信息、二次写缓冲）存放在原来的共享表空间内。
#### 段
表空间由段组成，常见的有数据段、索引段、回滚段。常见的段有数据段、索引段、回滚段
#### 区
区是连续的页组成的。区的大小在任何情况下都是1MB
#### 页
InnoDB默认页大小16KB，innodb_page_size 可以设置为4K、8K、16K。设置后不能进行修改。
InnoDB常见的页类型

- 数据页（B-tree Node）
- undo页（undo Log Page）
- 系统页（System Page）
- 事务数据页（Transaction system Page）
- 插入缓冲位图页（Insert Buffer Bitmap）
- 插入缓冲空闲列表页（Uncompressed BLOB Page）
- 未压缩的二进制大对象页（Unconpressed BLOB Page）
- 压缩的二进制大对象页（compressed BLOB Page）

#### 行

InnoDB存储引擎面向列。数据是按行进行存放。

每个页存放的行记录有硬性定义，最多允许存放16KB/2-200行记录，即7992行记录

##### InnoDB行记录格式
页中保存着表中一行行数据。
Compact
一个页中存放的行数据越多，性能就越高。

| 变长字段长度列表 |NULL标志位      |记录头信息      | 列1数据     |列2数据      |      |
| ---- | ---- | ---- | ---- | ---- | ---- |

1. 变长字段长度列表:若列的长度<255Byte用1Byte表示.列的长度>255Byte字节 用2Byte字节表示.变长字段的长度最大不超过2字节:因为Mysql数据库中Varchar类型的最大长度限制为65535
2. Null标志位 指示了该行数据中是否有NULL值.有用1表示
3. 记录头信息:固定5字节(40位)[Compact记录头信息]
4. 实际存储每个列的数据,NULL不占用任何空间,有NULL标志位,每行数据除了用户定义的列.还有两个隐藏列:事务ID列和回滚指针列.分别大小为6字节和7字节.若InnoDB表没有定义主键.每行还会增加一个6字节的rowid列

Redundant

Mysql5.0之前InnoDB的行记录存储方式.

Compact,Redundant 称为Antelope文件格式

##### 行溢出数据

一般认为BLOB,LOB大对象列类型的存储会把数据存放在数据页面之外.BLOB可以不将数据放在溢出页面,即使VARCHAR列数据类型,依然有可能行溢出数据.

InnODB存储引擎并不支持65535长度的Varchar,通过实际测试发现能存放的VARCHAR类型最大长度为65532.

MYsql官方手册中定义的65535长度是指所有VARCHAR列长度总和.

InnoDB存储引擎的数据是存放在页类型为B-tree node中.当发生行溢出时,数据存放在页类型为UncompressedBLOB页中.

InnoDB存储引擎表是索引组织的(B+Tree):每个页面至少应该有两条行记录.如果页中只能存放下一条记录.那么InnoDB存储引擎会自动将行数据存放到溢出页中. 

##### Compressed和Dynamic行记录格式:Barracuda 文件格式
这两种记录格式对于存放BLOB中数据采用完全的行溢出方式
Compredssed 按照zlib的算法进行压缩,对于BLOB,TEXT,VARCHAR大长度的数据能够进行有效的存储.
##### CHAR的行结构存储
在不同的字符集下,CHAR类型列内部存储的可能不是定长的数据.
对于多字节字符编码的CHAR数据类型的存储.InnoDB存储引擎在内部视为变长字符类型.
#### InnoDB 数据页结构

- File Header（文件头）
  - 用来记录头信息
- Page Header(页头)
  - 用来记录数据页的状态信息
- Infimun和Supremum Records
  - 虚拟行记录，限定记录的边界。
  - Infimun记录比该页中任何主键值都要小的值
  - Supremun指比任何可能大的值还要大的值
- User Records（用户记录，即行记录）
  - 实际存储行记录的内容，InnoDB存储引擎表总是B+树索引组织
- Free Space（空闲空间）
  - 空闲空间，链表数据结构，在一条记录被删除后，空间会被加入到空闲链表中。
- Page Directory（页目录）
  - 存放了记录的相对位置
- File Trailer（文件结尾信息）
  - 检测页是否完整的写入磁盘

#### Named File Formats 机制

InnoDB通过Named File Formats 机制来解决不同版本下页结构兼容性问题

### 约束

#### 数据完整性

域完整性通过以下几种途径来保证

1. 选择合适的数据类型确保一个数据值满足特定条件
2. 外键（Foreign Key）约束
3. 编写触发器
4. 使用DEFAULT约束作为强制域完整性的一个方面

对于InnoDB存储引擎本身而言，提供了几种约束

1. Primary KEY
2. Unique KEY
3. Foreign KEY
4. Default
5. NOT NULL

约束和索引的区别

1. 约束是一个逻辑概念，保证数据的完整性。
2. 索引是一个数据结构，既有逻辑上的概念。在数据库中还代表着物理存储方式。

对错误数据的约束

1. 向NOT NULL 的列插入一个NULL 值、向列date插入一个非法日期'2009-02-30' 数据库没有报错
2. 可以通过约束对数据的非法数据插入或更新
3. 即Mysql数据库提示报错而不是警告
4. 用户必须设置参数sql_mode，严格审核输入的参数

##### ENUM 和SET约束

### 分区表

分区功能并不是在存储引擎层完成的

分区的过程是将一个表或者索引分解为多个更小，更可管理的部分。

Mysql数据库支持的分区类型为水平分区，MySQL数据库的分区是局部分区索引，一个分区中既存放了数据又存放了索引。全局分区指:数据存放在各个分区中，所有的数据的索引放在一个对象中。Mysql还不支持全局分区。

误区：

启用了分区，数据库就会运行的更快，这个结论存在很多问题，分区可能会给某些SQL语句性能带来提高，分区主要用于数据库高可用的管理。

mysql数据库支持以下几种类型的分区

1. RANGE分区：行数据基于属性一个给定连续区间的列被放入分区
2. LIST分区:和RANGE分区类似，只是LIST分区面向离散的值
3. HASH分区：根据用户自定义的表达式的返回值进行分区，返回值不能为负数
4.  KEY分区：根据MYsql数据库提供的哈希函数来进行分区、

## 索引与算法

### 常见的索引

- B+树索引
- 全文索引
- 哈希索引

InnoDB存储引擎支持哈希索引是自适应的，InnoDB存储引擎会根据表的使用情况自动为表生成哈希索引，不能人为干预是否在一张表中生成哈希索引。

常被忽略的问题：B+树索引并不能找到一个给定键值的具体行。B+树索引能找到的是被查找数据行所在的页，然后数据库通过把页读入到内存，在内存中进行查找。

#### 数据结构与算法

1. 二分查找法：将记录按有序化排列，在查找过程中采用跳跃式方式查找。
2. 二叉查找树和平衡二叉树
   1. 二叉查找树：左子树的键值总是小于跟的键值，右子树的键值总是大于跟的键值。二叉查找树可以任意构造。
   2. 平衡二叉树的定义：首先符合二叉查找树的定义，其次满足任何节点的两个子树的高度最大差为1 
3. B+树
   1. B+树由B树和索引顺序访问方式（MyISAM引擎最初参考的数据结构）演化而来
   2. 在B+树中,所有记录节点按键值的大小顺序存放在同一层的叶子节点上、由各叶子节点指针进行连接。
   3. B+树插入操作的3种情况
   4. 为了保持平衡对于新插入的键值可能需要大量的拆分页（split）操作或旋转操作
   5. B+树的删除操作
   6. 在数据库中，B+树的高度一般在2~4层。

#### B+树下的索引

1. 聚集索引：每张表的主键构造一棵b+树，叶子节点中存放的为整张表的行记录数据。
2. 辅助索引

区别：叶子节点存放的是否是一整行的信息。

##### B+树索引的分裂

##### B+树索引的管理

- 通过ALTER TABLE

- 通过CREATE/DROP INDEX
- 用户可以设置对整列的数据进行索引，也可以索引一个列的开头部分数据如：ALTER TABLE ADD KEY id_xb (b(100))指索引前100个字段
- SHOW INDEX FROM 查询表的索引
- 索引属性中Cardinality值关键：优化器会根据这个值来判断是否使用这个索引

###### Fast Index Creation

##### Online Schema Change

##### Cardinality

1. 查看索引是否是高选择性
2. 表示索引中不重复记录数量的预估值
3. 在实际应用中，Cardinality/n_rows_in_table尽可能地接近1

InnoDB存储引擎的Cardinality统计

1. 通过采样（sample）方法统计
2. 统计信息的更新发生在两个操作中，INSERT和UPDATE
   1. 表中1/16的数据已发生过变换
   2. stat_modified_counter>2 000 000 000:发生变化的次数
      1. 取得B+树索引中叶子节点的数量，记为A
      2. 随机取得B+树索引中的8个叶子节点,统计每个页不同记录的个数记为P1，P2，，，P8
      3. 预估值：Cardinality=（P1+P2+,,+P8）*A/8

#### B+树索引的使用

1. 不同应用B+树索引的使用
   1. OLTP:联机事务处理：通过B+树索引取得表中少部分数据
   2. OLAP:联机分析处理：访问大量数据
2. 联合索引
3. 覆盖索引：从辅助索引中可以得到查询记录，不需要查询聚集索引中的记录
4. 优化器选择不使用索引的情况
   1. 范围查找、JOIN链接操作情况
   2. 使用非聚簇索引不能覆盖整行信息，还需要一次书签访问。虽然非聚簇索引中的数据是顺序存放的，但是在一次书签查找的数据是无须的，因此变成了磁盘上的离散读操作。
   3. 如果要求访问的数据量很小，则优化器会选择辅助索引，当访问的数据占整个表中数据的蛮大一部分时候（20%），优化器会选择通过聚集索引查找数据。
   4. 顺序读远远快于离散度

##### 索引提示

Mysql数据库支持索引提示（INDEX HINT），显示地告诉优化器使用哪个索引。

USE INDEX 只是告诉优化器可以选择该索引，优化器还是会再根据自己的判断选择

FORCE INDEX 

##### Multi-Range Read优化

Multi-Range Read优化的目的是减少磁盘的随机访问，并且将随机访问转化为较为顺序的数据访问。Multi优化可适用于range,ref,eq_ref类型的查询。

好处：

1. ​	MRR使数据访问变得较为顺序。在查询辅助索引时，首先根据得到的查询结果，按照主键顺序排序。并按照主键排序的顺序进行书签查找。
2. 减少缓冲池中页被替换的次数
3. 批量处理对键值的查询操作

mrr=on，mrr_cost_based=off

##### Index Condition Pushdown(ICP) 优化

当进行索引查询时，首先根据索引来查找记录，然后再根据WHERE条件来过滤记录，在支持（ICP）后MYSQL会在取出索引的同时，判断是否可以进行WHERE条件查询过滤，也就是将WHERE的部分过滤操作放在了存储引擎层。

ICP优化支持range,ref,eq_ref,ref_or_null

### 哈希算法

#### 哈希表

#### InnoDB存储引擎中的哈希算法

InnoDB 存储引擎使用哈希算法对字典进行查找，冲突机制采用链表方式，哈希函数采用除法散列方式。

对于缓冲池页的哈希表，缓冲池的Page页都有一个chain指针，对于除法散列，m的取值为略大于2倍的缓冲池页数量的质数。 

#### 自适应哈希索引

### 全文检索

## 锁

### lock与latch

latch一般称为闩锁（轻量级锁），要求锁定的时间非常短，在InnoDB存储引擎中，latch分为mutex(互斥量)和rwlock（读写锁）。其目的用来保证并发线程操作临界资源的正确性，并且通常没有死锁检测机制。

lock的对象是事务，用来锁定数据库中的对象，如表，页，行，并且lock的对象仅在事务commit或rollback后进行释放，lock是有死锁机制。

### InnoDB存储引擎中的锁

#### InnoDB存储引擎实现两种标准的行级锁

1. 共享锁
2. 排他锁

#### InnoDB 意向锁

意向锁即为表级别的锁

1. 意向共享锁，事务想要获得一张表中某几行的共享锁
2. 意向排他锁，事务想要获得一张表中某几行的排他锁

在INFORMARTION_SCHEMA架构下添加了表INNODB_TRX、INNODB_LOCKS、INNODB_LOCK_WAITS。通过这3张表，用户可以更简单的监控当前事务并分析可能存在的锁问题。

#### 一致性非锁定读

1. 是指InnoDB存储引擎通过行多版本控制的方式读取当前执行时间数据库中行的的数据。
2. 如果读取的行正在执行DELETE或UPDATE操作。这时读取操作不会去等待行上锁的释放。相反，InnoDB存储引擎会去读行的一个快照数据。
3. 快照数据是该行之前版本的数据，实现通过undo段完成。undo用来事务中的回滚数据，因此快照数据本身没有额外的开销。读取快照数据不需要上锁，因此没有事务需要对历史的数据进行修改操作。
4. 在不同事务隔离级别下，读取的方式不同。不是每个事务隔离级别下都采用非锁定的一致性读。即使都是使用非锁定一致性读，对于快照数据的定义也不相同。
5. 快照数据是当前行数据之前的历史版本，每行记录可能有多个版本。一个行记录可能不止一个快照数据，这种技术一般成为多版本并发控制。
6. InnoDB存储引擎使用非锁定一致性读：发生在事务READ COMMITTED 和 REPEATABLE READ隔离级别下：其中，READ COMMITTED 是行最新版本的一份快照数据，如果行被锁定，读取该行版本最新一个快照。REPEATABLE READ是最新事务开始行数据版本。



#### 一致性锁定读

在某些情况下，用户需要显示地对数据库读取操作进行加锁以保证数据逻辑的一致性

SELECT FOR UPDATE：对读取的行记录加一个X锁

SELECT LOCK IN SHARE MODE：对读取的行记录加一个S锁

#### 自增长与锁



### 锁的算法

#### 行锁的三种算法

Record Lock：单个行记录上的锁

Gap Lock：间隙锁，锁定范围,不包含记录本身

Next-Key Lock: 锁定一个范围，并且锁定记录本身

1. Gap Lock的作用是为了组织多个事务将记录插入到同一个范围内，这会导致Phantom Problem（幻读） 问题产生
2. 关闭 GAP Lock 
   - 将事务的隔离级别设置为READ COMMITTED
   - 将参数innodb_locks_unsafe_for_binlog 设置为1

#### 锁问题

- 脏读
- 不可重复读、幻读

不可重复读是指在一个事务内多次读取同一数据集合。在这个事务还没结束时候，另外一个事务也访问该同一数据集合，并做了一些DML操作。

### 阻塞

### 死锁

### 锁升级

