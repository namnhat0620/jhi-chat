package com.nam.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.domain.Persistable;

/**
 * A Group.
 */
@Entity
@Table(name = "jhi_group")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Group implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "last_message_id")
    private String lastMessageId;

    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group")
    @JsonIgnoreProperties(value = { "group" }, allowSetters = true)
    private Set<UserGroup> userGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Group id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Group type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Group name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Group avatar(String avatar) {
        this.setAvatar(avatar);
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessageId() {
        return this.lastMessageId;
    }

    public Group lastMessageId(String lastMessageId) {
        this.setLastMessageId(lastMessageId);
        return this;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Group setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<UserGroup> getUserGroups() {
        return this.userGroups;
    }

    public void setUserGroups(Set<UserGroup> userGroups) {
        if (this.userGroups != null) {
            this.userGroups.forEach(i -> i.setGroup(null));
        }
        if (userGroups != null) {
            userGroups.forEach(i -> i.setGroup(this));
        }
        this.userGroups = userGroups;
    }

    public Group userGroups(Set<UserGroup> userGroups) {
        this.setUserGroups(userGroups);
        return this;
    }

    public Group addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
        userGroup.setGroup(this);
        return this;
    }

    public Group removeUserGroup(UserGroup userGroup) {
        this.userGroups.remove(userGroup);
        userGroup.setGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        return getType() != null && getType().equals(((Group) o).getType());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Group{" +
                "type=" + getType() +
                ", id='" + getId() + "'" +
                ", name='" + getName() + "'" +
                ", avatar='" + getAvatar() + "'" +
                ", lastMessageId='" + getLastMessageId() + "'" +
                "}";
    }
}
