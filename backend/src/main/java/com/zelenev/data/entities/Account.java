package com.zelenev.data.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Account")
@Table(
        name = "account",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_login",
                        columnNames = "login"
                )
        }
)
public class Account implements Serializable {

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            schema = "public",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "account_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "login",
            nullable = false
    )
    private String login;

    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    @OneToOne(
            fetch = FetchType.EAGER,
            mappedBy = "account",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private Card card;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    @JoinTable(
            name = "user_task",
            schema = "public",
            joinColumns = @JoinColumn(
                    name = "account_id",
                    referencedColumnName = "id",
                    nullable = false,
                    foreignKey = @ForeignKey(
                            name = "account_task_to_account_fk"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "task_id",
                    referencedColumnName = "id",
                    nullable = false,
                    foreignKey = @ForeignKey(
                            name = "account_task_to_task_fk"
                    )
            )
    )
    private List<Task> tasks = new LinkedList<>();

    public Account() {
    }

    public Account(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Card getCard() {
        return card;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void takeTask(Task task) {
        this.tasks.add(task);
        task.getAccounts().add(this);
    }

    public void completeTask(Task task) {
        this.tasks.remove(task);
        task.getAccounts().remove(this);
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) &&
                login.equals(account.login) &&
                password.equals(account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
