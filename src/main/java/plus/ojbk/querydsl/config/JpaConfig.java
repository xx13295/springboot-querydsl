package plus.ojbk.querydsl.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
//@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }


    /**
     *
     * @EnableJpaRepositories(basePackages = "plus.ojbk.querydsl.repository.*", transactionManagerRef = "transactionManagerJpa")
     * @EntityScan(basePackages = "plus.ojbk.querydsl.entity.*")
     */
    /*@Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean(name = "transactionManagerJpa")
    public PlatformTransactionManager transactionManagerJpa() {
        return new JpaTransactionManager(entityManagerFactory);
    }*/
}
