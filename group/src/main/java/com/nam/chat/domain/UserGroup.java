package com.nam.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

/**
 * A UserGroup.
 */
@Entity
@Table(name = "user_group")
@JsonIgnoreProperties(value = { "new", "id" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserGroup implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    private String id;

    @NotNull
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "is_seen")
    private Boolean isSeen;

    @Column(name = "is_turn_on_noti")
    private Boolean isTurnOnNoti;

    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userGroups" }, allowSetters = true)
    private Group group;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UserGroup id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserGroup userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsSeen() {
        return this.isSeen;
    }

    public UserGroup isSeen(Boolean isSeen) {
        this.setIsSeen(isSeen);
        return this;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Boolean getIsTurnOnNoti() {
        return this.isTurnOnNoti;
    }

    public UserGroup isTurnOnNoti(Boolean isTurnOnNoti) {
        this.setIsTurnOnNoti(isTurnOnNoti);
        return this;
    }

    public void setIsTurnOnNoti(Boolean isTurnOnNoti) {
        this.isTurnOnNoti = isTurnOnNoti;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Override
    public String getId() {
        return this.userId;
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public UserGroup setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public UserGroup group(Group group) {
        this.setGroup(group);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroup)) {
            return false;
        }
        return getUserId() != null && getUserId().equals(((UserGroup) o).getUserId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserGroup{" +
            "userId=" + getUserId() +
            ", id='" + getId() + "'" +
            ", isSeen='" + getIsSeen() + "'" +
            ", isTurnOnNoti='" + getIsTurnOnNoti() + "'" +
            "}";
    }
}
