package com.millionaire_project.millionaire_project.entity;

import com.millionaire_project.millionaire_project.constant.Static;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event_economic_calendar")
public class EconomicEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.EVENT_SEQ)
    @SequenceGenerator(name = Static.EVENT_SEQ , sequenceName = Static.EVENT_SEQ, allocationSize = 1)
    private Integer id;

    @Column(name = "event")
    private String event;

    @Column(name = "schedule")
    private Date schedule;

    @Column(name = "expected")
    private String expected;

    @Column(name = "created_at")
    private Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
