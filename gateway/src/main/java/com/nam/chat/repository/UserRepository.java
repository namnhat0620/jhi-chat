package com.nam.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import org.springframework.stereotype.Repository;

import com.nam.chat.domain.Authority;
import com.nam.chat.domain.User;
import com.nam.chat.domain.criteria.UserCriteria;
import com.nam.chat.repository.rowmapper.ColumnConverter;
import com.nam.chat.repository.rowmapper.UserRowMapper;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import tech.jhipster.service.ConditionBuilder;

/**
 * Spring Data R2DBC repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<User, String>, UserRepositoryInternal {
    Mono<User> findOneByLogin(String login);

    Flux<User> findAllByIdNotNull(Pageable pageable);

    Flux<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Mono<Long> count();

    @Query("INSERT INTO jhi_user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(String userId, String authority);

    @Query("DELETE FROM jhi_user_authority")
    Mono<Void> deleteAllUserAuthorities();

    @Query("DELETE FROM jhi_user_authority WHERE user_id = :userId")
    Mono<Void> deleteUserAuthorities(String userId);
}

interface UserRepositoryInternal {
    Mono<User> findOneWithAuthoritiesByLogin(String login);

    Mono<User> create(User user);

    Flux<User> findAllWithAuthorities(Pageable pageable);

    Flux<User> findByCriteria(UserCriteria criteria, Pageable pageable);
}

class UserRepositoryInternalImpl implements UserRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final R2dbcConverter r2dbcConverter;
    private final ColumnConverter columnConverter;
    private final EntityManager entityManager;
    private final UserRowMapper userRowMapper;

    private static final Table entityTable = Table.aliased("jhi_user", EntityManager.ENTITY_ALIAS);

    public UserRepositoryInternalImpl(DatabaseClient db, R2dbcEntityTemplate r2dbcEntityTemplate,
            EntityManager entityManager,

            R2dbcConverter r2dbcConverter,
            ColumnConverter columnConverter,
            UserRowMapper userRowMapper) {
        this.db = db;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
        this.r2dbcConverter = r2dbcConverter;
        this.columnConverter = columnConverter;
        this.entityManager = entityManager;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Mono<User> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Flux<User> findAllWithAuthorities(Pageable pageable) {
        String property = pageable.getSort().stream().map(Sort.Order::getProperty).findFirst().orElse("id");
        String direction = String.valueOf(
                pageable.getSort().stream().map(Sort.Order::getDirection).findFirst().orElse(Sort.DEFAULT_DIRECTION));
        long page = pageable.getPageNumber();
        long size = pageable.getPageSize();

        return db
                .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id")
                .map((row, metadata) -> Tuples.of(r2dbcConverter.read(User.class, row, metadata),
                        Optional.ofNullable(row.get("authority_name", String.class))))
                .all()
                .groupBy(t -> t.getT1().getLogin())
                .flatMap(l -> l.collectList().map(t -> updateUserWithAuthorities(t.get(0).getT1(), t)))
                .sort(
                        Sort.Direction.fromString(direction) == Sort.DEFAULT_DIRECTION
                                ? new BeanComparator<>(property)
                                : new BeanComparator<>(property).reversed())
                .skip(page * size)
                .take(size);
    }

    @Override
    public Mono<User> create(User user) {
        return r2dbcEntityTemplate.insert(User.class).using(user).defaultIfEmpty(user);
    }

    @Override
    public Flux<User> findByCriteria(UserCriteria userCriteria, Pageable page) {
        return createQuery(page, buildConditions(userCriteria)).all();
    }

    RowsFetchSpec<User> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = UserSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);
        // we do not support Criteria here for now as of
        // https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, User.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    private Mono<User> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db
                .sql("SELECT * FROM jhi_user u LEFT JOIN jhi_user_authority ua ON u.id=ua.user_id WHERE u." + fieldName
                        + " = :" + fieldName)
                .bind(fieldName, fieldValue)
                .map((row, metadata) -> Tuples.of(r2dbcConverter.read(User.class, row, metadata),
                        Optional.ofNullable(row.get("authority_name", String.class))))
                .all()
                .collectList()
                .filter(l -> !l.isEmpty())
                .map(l -> updateUserWithAuthorities(l.get(0).getT1(), l));
    }

    private User updateUserWithAuthorities(User user, List<Tuple2<User, Optional<String>>> tuples) {
        user.setAuthorities(
                tuples
                        .stream()
                        .filter(t -> t.getT2().isPresent())
                        .map(t -> {
                            Authority authority = new Authority();
                            authority.setName(t.getT2().orElseThrow());
                            return authority;
                        })
                        .collect(Collectors.toSet()));

        return user;
    }

    private Condition buildConditions(UserCriteria criteria) {
        ConditionBuilder builder = new ConditionBuilder(this.columnConverter);
        if (criteria != null) {
            if (criteria.getId() != null) {
                builder.buildFilterConditionForField(criteria.getId(), entityTable.column("id"));
            }
            if (criteria.getLogin() != null) {
                builder.buildFilterConditionForField(criteria.getLogin(), entityTable.column("login"));
            }
            if (criteria.getFirstName() != null) {
                builder.buildFilterConditionForField(criteria.getFirstName(), entityTable.column("first_name"));
            }
            if (criteria.getLastName() != null) {
                builder.buildFilterConditionForField(criteria.getLastName(), entityTable.column("last_name"));
            }
            if (criteria.getEmail() != null) {
                builder.buildFilterConditionForField(criteria.getEmail(), entityTable.column("email"));
            }
        }
        return builder.buildConditions();
    }

    private User process(Row row, RowMetadata metadata) {
        User entity = userRowMapper.apply(row, "e");
        return entity;
    }
}
