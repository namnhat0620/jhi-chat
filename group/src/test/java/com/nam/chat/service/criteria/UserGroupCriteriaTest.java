package com.nam.chat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserGroupCriteriaTest {

    @Test
    void newUserGroupCriteriaHasAllFiltersNullTest() {
        var userGroupCriteria = new UserGroupCriteria();
        assertThat(userGroupCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userGroupCriteriaFluentMethodsCreatesFiltersTest() {
        var userGroupCriteria = new UserGroupCriteria();

        setAllFilters(userGroupCriteria);

        assertThat(userGroupCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userGroupCriteriaCopyCreatesNullFilterTest() {
        var userGroupCriteria = new UserGroupCriteria();
        var copy = userGroupCriteria.copy();

        assertThat(userGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userGroupCriteria)
        );
    }

    @Test
    void userGroupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userGroupCriteria = new UserGroupCriteria();
        setAllFilters(userGroupCriteria);

        var copy = userGroupCriteria.copy();

        assertThat(userGroupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userGroupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userGroupCriteria = new UserGroupCriteria();

        assertThat(userGroupCriteria).hasToString("UserGroupCriteria{}");
    }

    private static void setAllFilters(UserGroupCriteria userGroupCriteria) {
        userGroupCriteria.id();
        userGroupCriteria.login();
        userGroupCriteria.isSeen();
        userGroupCriteria.isTurnOnNoti();
        userGroupCriteria.groupId();
        userGroupCriteria.distinct();
    }

    private static Condition<UserGroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLogin()) &&
                condition.apply(criteria.getIsSeen()) &&
                condition.apply(criteria.getIsTurnOnNoti()) &&
                condition.apply(criteria.getGroupId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserGroupCriteria> copyFiltersAre(UserGroupCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLogin(), copy.getLogin()) &&
                condition.apply(criteria.getIsSeen(), copy.getIsSeen()) &&
                condition.apply(criteria.getIsTurnOnNoti(), copy.getIsTurnOnNoti()) &&
                condition.apply(criteria.getGroupId(), copy.getGroupId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
