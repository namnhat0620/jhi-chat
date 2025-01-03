package com.nam.chat.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GroupCriteriaTest {

    @Test
    void newGroupCriteriaHasAllFiltersNullTest() {
        var groupCriteria = new GroupCriteria();
        assertThat(groupCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void groupCriteriaFluentMethodsCreatesFiltersTest() {
        var groupCriteria = new GroupCriteria();

        setAllFilters(groupCriteria);

        assertThat(groupCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void groupCriteriaCopyCreatesNullFilterTest() {
        var groupCriteria = new GroupCriteria();
        var copy = groupCriteria.copy();

        assertThat(groupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(groupCriteria)
        );
    }

    @Test
    void groupCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var groupCriteria = new GroupCriteria();
        setAllFilters(groupCriteria);

        var copy = groupCriteria.copy();

        assertThat(groupCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(groupCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var groupCriteria = new GroupCriteria();

        assertThat(groupCriteria).hasToString("GroupCriteria{}");
    }

    private static void setAllFilters(GroupCriteria groupCriteria) {
        groupCriteria.id();
        groupCriteria.type();
        groupCriteria.lastMessageId();
        groupCriteria.avatar();
        groupCriteria.userGroupId();
        groupCriteria.distinct();
    }

    private static Condition<GroupCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getLastMessageId()) &&
                condition.apply(criteria.getAvatar()) &&
                condition.apply(criteria.getUserGroupId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GroupCriteria> copyFiltersAre(GroupCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getLastMessageId(), copy.getLastMessageId()) &&
                condition.apply(criteria.getAvatar(), copy.getAvatar()) &&
                condition.apply(criteria.getUserGroupId(), copy.getUserGroupId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
