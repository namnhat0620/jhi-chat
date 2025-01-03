package com.nam.chat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nam.chat.domain.UserGroup} entity. This class is used
 * in {@link com.nam.chat.web.rest.UserGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter userId;

    private BooleanFilter isSeen;

    private BooleanFilter isTurnOnNoti;

    private StringFilter groupId;

    private Boolean distinct;

    public UserGroupCriteria() {}

    public UserGroupCriteria(UserGroupCriteria other) {
        this.id = other.optionalId().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.isSeen = other.optionalIsSeen().map(BooleanFilter::copy).orElse(null);
        this.isTurnOnNoti = other.optionalIsTurnOnNoti().map(BooleanFilter::copy).orElse(null);
        this.groupId = other.optionalGroupId().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UserGroupCriteria copy() {
        return new UserGroupCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public Optional<StringFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public StringFilter id() {
        if (id == null) {
            setId(new StringFilter());
        }
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public BooleanFilter getIsSeen() {
        return isSeen;
    }

    public Optional<BooleanFilter> optionalIsSeen() {
        return Optional.ofNullable(isSeen);
    }

    public BooleanFilter isSeen() {
        if (isSeen == null) {
            setIsSeen(new BooleanFilter());
        }
        return isSeen;
    }

    public void setIsSeen(BooleanFilter isSeen) {
        this.isSeen = isSeen;
    }

    public BooleanFilter getIsTurnOnNoti() {
        return isTurnOnNoti;
    }

    public Optional<BooleanFilter> optionalIsTurnOnNoti() {
        return Optional.ofNullable(isTurnOnNoti);
    }

    public BooleanFilter isTurnOnNoti() {
        if (isTurnOnNoti == null) {
            setIsTurnOnNoti(new BooleanFilter());
        }
        return isTurnOnNoti;
    }

    public void setIsTurnOnNoti(BooleanFilter isTurnOnNoti) {
        this.isTurnOnNoti = isTurnOnNoti;
    }

    public StringFilter getGroupId() {
        return groupId;
    }

    public Optional<StringFilter> optionalGroupId() {
        return Optional.ofNullable(groupId);
    }

    public StringFilter groupId() {
        if (groupId == null) {
            setGroupId(new StringFilter());
        }
        return groupId;
    }

    public void setGroupId(StringFilter groupId) {
        this.groupId = groupId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserGroupCriteria that = (UserGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(isSeen, that.isSeen) &&
            Objects.equals(isTurnOnNoti, that.isTurnOnNoti) &&
            Objects.equals(groupId, that.groupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, isSeen, isTurnOnNoti, groupId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserGroupCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalIsSeen().map(f -> "isSeen=" + f + ", ").orElse("") +
            optionalIsTurnOnNoti().map(f -> "isTurnOnNoti=" + f + ", ").orElse("") +
            optionalGroupId().map(f -> "groupId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
