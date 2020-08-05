package plus.ojbk.querydsl;

import com.alibaba.fastjson.JSON;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import plus.ojbk.querydsl.entity.*;
import plus.ojbk.querydsl.model.MEmpGroup;
import plus.ojbk.querydsl.model.MEmployee;
import plus.ojbk.querydsl.repository.IDepartmentRepository;
import plus.ojbk.querydsl.repository.IEmployeeRepository;
import plus.ojbk.querydsl.repository.IRoleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
class QuerydslApplicationTests {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    IEmployeeRepository employeeRepository;
    @Autowired
    IRoleRepository roleRepository;
    @Autowired
    IDepartmentRepository departmentRepository;

    /**
     * 初始化demo数据
     */
    @Test
    public void init(){
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();
        roleRepository.deleteAll();
        //初始化部门数据
        List<Department> listDepartment = new ArrayList<>();
        listDepartment.add(new Department("研发部"));
        listDepartment.add(new Department("销售部"));
        listDepartment.add(new Department("后勤部"));
        departmentRepository.saveAll(listDepartment);

        //初始化角色数据
        List<Role> list = new ArrayList<>();
        List<String> departmentIds = listDepartment.stream().map(Department::getId).collect(Collectors.toList());
        list.add(new Role("java程序员", departmentIds.get(0)));
        list.add(new Role("php程序员", departmentIds.get(0)));
        list.add(new Role("c++程序员", departmentIds.get(0)));
        list.add(new Role("python程序员", departmentIds.get(0)));
        list.add(new Role("产品经理", departmentIds.get(0)));
        list.add(new Role("研发总监", departmentIds.get(0)));
        list.add(new Role("销售经理", departmentIds.get(1)));
        list.add(new Role("销售总监", departmentIds.get(1)));
        list.add(new Role("商务经理", departmentIds.get(2)));
        roleRepository.saveAll(list);

        //初始化员工数据
        List<Employee> listEmployee = new ArrayList<>();
        listEmployee.add(new Employee("小明", LocalDate.now().minusDays(8942), list.get(0)));
        listEmployee.add(new Employee("小红", LocalDate.now().minusDays(9000), list.get(1)));
        listEmployee.add(new Employee("小刚", LocalDate.now().minusDays(9125), list.get(2)));
        listEmployee.add(new Employee("小强", LocalDate.now().minusDays(7954), list.get(3)));
        listEmployee.add(new Employee("憨憨", LocalDate.now().minusDays(5555), list.get(4)));
        listEmployee.add(new Employee("小美", LocalDate.now().minusDays(8761), list.get(6)));
        listEmployee.add(new Employee("小花", LocalDate.now().minusDays(8761), list.get(7)));
        listEmployee.add(new Employee("小羊", LocalDate.now().minusDays(8761), list.get(8)));
        employeeRepository.saveAll(listEmployee);
    }

    /**
     * 基本查询部门全部信息
     */
    @Test
    void demoFirst() {
        QDepartment qd = QDepartment.department;
        List<Department> departmentList = jpaQueryFactory.selectFrom(qd).fetch();
        log.info("查询部门信息= {}", JSON.toJSONString(departmentList));
    }

    /**
     * 查询某部门部分信息或全部信息
     */
    @Test
    void demoSecond() {
        QDepartment qd = QDepartment.department;
        Department department = jpaQueryFactory.selectFrom(qd).where(qd.name.eq("研发部")).fetchFirst();
        //使用fetchFirst 相比于fetchOne 的优势是不报错如果 因为他是查询符合条件的第一条数据，
        // 存在多条name相同的数据 使用fetchOne会报错 当然因此需可以给name增加唯一约束条件
        log.info("查询某个部门信息1= {}", JSON.toJSONString(department));
        //通过部门 name 查询部门 id   [ select() 中可以增加检索的列进行查询返回对应的数据类型]
        String id = jpaQueryFactory.select(qd.id).from(qd).where(qd.name.eq("研发部")).fetchFirst();
        JPAQuery<Tuple> departmentTuple =  jpaQueryFactory.select(qd.id, qd.name).from(qd).where(qd.name.eq("研发部"));
        Tuple department2 = departmentTuple.fetchFirst();
        //由于该种方式返回的是Tuple 因此如果要取值可以使用如下方式
        String id2 = department2.get(qd.id);
        log.info("查询某个部门信息2= {} , id {} ", JSON.toJSONString(department2.toString()), id.equals(id2));
        //建议 使用 select(Projections.bean(你的实体.class, 字段一,字段二 ....)) ，在第三个demo 中会提及
    }

    /**
     * 查询全部员工的部分基本信息
     */
    @Test
    void demoThird() {
        QEmployee qe = QEmployee.employee;
        List<MEmployee> employeeList = jpaQueryFactory.select(
                Projections.bean(MEmployee.class,
                        qe.id,
                        qe.name,
                        qe.birthday
                       ))
                .from(qe).fetch();

        log.info("查询全部员工信息结果= {}", JSON.toJSONString(employeeList));
        //之所以推荐 使用Projections.bean()。 因为如果不使用返回的类型便是Tuple
        // 往往该Tuple类型 需要重新对数据进行包装 ，涉及业务如果复杂就会出现大量的  Tuple.get(Q.param) 很不美观
    }

    /**
     * 分页查询
     */
    @Test
    void demoFour() {
        QEmployee qe = QEmployee.employee;
        // 分页查询增加参数 offset 和 limit
        QueryResults<MEmployee> employeeQueryResults = jpaQueryFactory.select(
                Projections.bean(MEmployee.class,
                        qe.id,
                        qe.name,
                        qe.birthday
                ))
                .from(qe)
                .where(qe.name.like("小" + "%"))   //模糊查询 .like("%" + key + "%"))
                .orderBy(qe.id.desc())     // 排序  : id 倒叙。 条件当然可以自己定义了这里只是举个栗子
                .offset(0)  //数据的下标
                .limit(2)   //每页显示的条数
                .fetchResults();
        long total = employeeQueryResults.getTotal();
        List<MEmployee> employeeList = employeeQueryResults.getResults();
        log.info("总条数 ={} ，分页查询结果 = {}", total, JSON.toJSONString(employeeList));

    }

    /**
     * 关联查询
     */
    @Test
    void demoFive() {
        QRole qr = QRole.role;
        QEmployee qe = QEmployee.employee;
        List<MEmployee> roleList = jpaQueryFactory.select(
                Projections.bean(MEmployee.class,
                        qe.id,
                        qe.name,
                        qe.birthday,
                        qe.role.name.as("roleName"),   //as 别名映射
                        qr.department.name.as("departmentName")
                ))
                .from(qe)
                .leftJoin(qe.role, qr)
                .fetch();
        log.info("关联查询结果= {}", JSON.toJSONString(roleList));

    }

    /**
     * 分组
     */
    @Test
    void demoSix() {
        QRole qr = QRole.role;
        QEmployee qe = QEmployee.employee;
        List<MEmpGroup> roleList = jpaQueryFactory.select(
                Projections.bean(MEmpGroup.class,
                        qr.department.name.as("departmentName"),
                        qr.department.name.count().as("departmentCount")
                ))
                .from(qe)
                .leftJoin(qe.role, qr)
                .groupBy(qr.department.name)
                .fetch();

        log.info("关联查询结果= {}", JSON.toJSONString(roleList));
    }

    /**
     * 多条件查询
     */
    @Test
    void demoSeven() {
        QEmployee qe = QEmployee.employee;
        List<MEmployee> employeeList = jpaQueryFactory.select(
                Projections.bean(MEmployee.class,
                        qe.id,
                        qe.name,
                        qe.birthday
                ))
                .from(qe)             // 多条件 使用 and 连接
                .where(qe.name.like("小" + "%").and(qe.role.name.like( "%"+"程序员")))
                .fetch();

        log.info("多条件查询员工信息结果1= {}", JSON.toJSONString(employeeList));
        // 不推荐上面这种写法，建议单独把条件剥离出来 使用BooleanBuilder来构建查询条件 这样条件更加直观
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qe.name.like("小" + "%"));
        builder.and(qe.role.name.like( "%"+"程序员"));
        //那么只需要 在 where 中填入 builder即可 。
        List<MEmployee> employeeList2 = jpaQueryFactory.select(
                Projections.bean(MEmployee.class,
                        qe.id,
                        qe.name,
                        qe.birthday
                ))
                .from(qe)
                .where(builder)
                .fetch();
        log.info("多条件查询员工信息结果2= {}", JSON.toJSONString(employeeList2));

    }

    /**
     * 更新数据
     */
    @Test
    @Rollback(false) //注意这里 单元测试类中事物会自动回滚 因此要加上Rollback注解设置为不回滚
    @Transactional
    void update() {
        QEmployee qe = QEmployee.employee;
        long count = jpaQueryFactory.update(qe)
                .set(qe.birthday, LocalDate.now())  //设置 要修改更新的值
                .where(qe.name.eq("憨憨"))
                .execute();

        log.info("更新操作执行的条数= {}", count);

    }

    /**
     * 删除数据
     */
    @Test
    @Rollback(false)
    @Transactional
    void delete() {
        QEmployee qe = QEmployee.employee;
        long count = jpaQueryFactory.delete(qe)
                .where(qe.name.eq("小刚"))  //由于小刚 上班摸鱼被开除了
                .execute();

        log.info("删除操作执行的条数= {}", count);
    }


}
