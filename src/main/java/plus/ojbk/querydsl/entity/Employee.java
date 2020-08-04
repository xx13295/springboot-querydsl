package plus.ojbk.querydsl.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "op_querydsl_employee") // 指定关联的数据库的表名
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 主键ID
    @Column(name = "name", columnDefinition = "VARCHAR(16) NULL DEFAULT '' COMMENT '员工名称'")
    private String name; // 姓名

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday", columnDefinition = "DATE NULL DEFAULT NULL COMMENT '生日'")
    private LocalDate birthday; // 生日

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", columnDefinition = "VARCHAR(64) NULL DEFAULT '' COMMENT '角色id'", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Role role; // 角色

    public Employee(String name, LocalDate birthday, Role role) {
        this.name = name;
        this.birthday = birthday;
        this.role = role;
    }
}
