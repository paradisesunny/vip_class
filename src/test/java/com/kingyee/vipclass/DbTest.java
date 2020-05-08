// ======================================
// Project Name:meddb-starter
// Package Name:cn.meddb.core.starter
// File Name:DbTest.java
// Create Date:2019年10月11日  10:45
// ======================================
package com.kingyee.vipclass;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 李旭光
 * @version 2019年10月11日  10:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class DbTest {

    // @Autowired
    // private UserService userService;
    //
    // @Test
    // public void testInsert() {
    //     userService.save(new User("a", "b", 1L, Timestamp.from(Instant.now())));
    //     userService.save(new User("aa", "bb", 1L, Timestamp.from(Instant.now())));
    //     userService.save(new User("aaa", "bbb", 2L, Timestamp.from(Instant.now())));
    // }
    //
    // @Test
    // public void testDelete() {
    //     userService.removeById(2L);
    // }
    //
    // @Test
    // public void testSelect() {
    //     Page<User> page = new Page<>(1, 2);
    //     QueryWrapper<User> queryWrapper = new QueryWrapper<User>().like("us_name", "a").select("us_name", "us_passwd");
    //     IPage<User> page1 = userService.page(page, queryWrapper);
    //     System.out.println(page1.getRecords().size());
    //     System.out.println(page1.getTotal());
    // }
}