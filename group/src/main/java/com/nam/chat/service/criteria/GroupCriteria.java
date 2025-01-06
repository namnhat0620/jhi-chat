package com.nam.chat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nam.chat.domain.Group} entity. This class is used
 * in {@link com.nam.chat.web.rest.GroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private StringFilter lastMessageId;

    private StringFilter avatar;

    private LongFilter userGroupId;

    private Boolean distinct;

    public GroupCriteria() {}

    public GroupCriteria(GroupCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.lastMessageId = other.optionalLastMessageId().map(StringFilter::copy).orElse(null);
        this.avatar = other.optionalAvatar().map(StringFilter::copy).orElse(null);
        this.userGroupId = other.optionalUserGroupId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GroupCriteria copy() {
        return new GroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getLastMessageId() {
        return lastMessageId;
    }

    public Optional<StringFilter> optionalLastMessageId() {
        return Optional.ofNullable(lastMessageId);
    }

    public StringFilter lastMessageId() {
        if (lastMessageId == null) {
            setLastMessageId(new StringFilter());
        }
        return lastMessageId;
    }

    public void setLastMessageId(StringFilter lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public StringFilter getAvatar() {
        return avatar;
    }

    public Optional<StringFilter> optionalAvatar() {
        return Optional.ofNullable(avatar);
    }

    public StringFilter avatar() {
        if (avatar == null) {
            setAvatar(new StringFilter());
        }
        return avatar;
    }

    public void setAvatar(StringFilter avatar) {
        this.avatar = avatar;
    }

    public LongFilter getUserGroupId() {
        return userGroupId;
    }

    public Optional<LongFilter> optionalUserGroupId() {
        return Optional.ofNullable(userGroupId);
    }

    public LongFilter userGroupId() {
        if (userGroupId == null) {
            setUserGroupId(new LongFilter());
        }
        return userGroupId;
    }

    public void setUserGroupId(LongFilter userGroupId) {
        this.userGroupId = userGroupId;
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
        final GroupCriteria that = (GroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(lastMessageId, that.lastMessageId) &&
            Objects.equals(avatar, that.avatar) &&
            Objects.equals(userGroupId, that.userGroupId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, lastMessageId, avatar, userGroupId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GroupCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalLastMessageId().map(f -> "lastMessageId=" + f + ", ").orElse("") +
            optionalAvatar().map(f -> "avatar=" + f + ", ").orElse("") +
            optionalUserGroupId().map(f -> "userGroupId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
