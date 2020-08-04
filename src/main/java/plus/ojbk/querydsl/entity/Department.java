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
@Table(name = "op_querydsl_department")
public class Department {

    @Id
    @GeneratedValue(generator = "departmentGenerator")
    @GenericGenerator(name = "departmentGenerator", strategy = "uuid")
    @Column(name = "id", columnDefinition = "VARCHAR(64) NOT NULL DEFAULT '' COMMENT '部门ID'")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id; // 主键ID
    @Column(name = "name", columnDefinition = "VARCHAR(32) NULL DEFAULT '' COMMENT '部门名称'")
    private String name; // 部门名称

    public Department(String name) {
        this.name = name;
    }
}
