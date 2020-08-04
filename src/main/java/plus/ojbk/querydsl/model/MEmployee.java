package plus.ojbk.querydsl.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MEmployee {

    private Long id;
    private String name;  //员工名称
    private LocalDate birthday;
    private String roleName;   //角色名称
    private String departmentName; //部门名称

}
