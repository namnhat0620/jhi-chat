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
@Table(name = "jhi_user_group")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserGroup implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @GeneratedValue
    @Id
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

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

    public UserGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public UserGroup login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
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
    public Long getId() {
        return this.id;
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
        return getLogin() != null && getLogin().equals(((UserGroup) o).getLogin());
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
            "Login=" + getLogin() +
            ", id='" + getId() + "'" +
            ", isSeen='" + getIsSeen() + "'" +
            ", isTurnOnNoti='" + getIsTurnOnNoti() + "'" +
            "}";
    }
}
