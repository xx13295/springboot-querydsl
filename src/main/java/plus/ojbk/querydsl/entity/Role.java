package plus.ojbk.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "op_querydsl_role")
public class Role {

    @Id
    @GeneratedValue(generator = "roleGenerator")
    @GenericGenerator(name = "roleGenerator", strategy = "uuid")
    @Column(name = "id", columnDefinition = "VARCHAR(64) NOT NULL DEFAULT '' COMMENT '角色ID'")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; // 主键ID
    @Column(name = "name", columnDefinition = "VARCHAR(32) NULL DEFAULT '' COMMENT '角色名称'")
    private String name; // 角色名称

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", columnDefinition = "VARCHAR(64) NULL DEFAULT '' COMMENT '部门id'", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Department department; // 部门

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private List<Employee> employees; // 拥有角色的员工

    public Role(String name, String departmentId) {
        this.name = name;
        Department department = new Department();
        department.setId(departmentId);
        this.department = department;
    }
}
