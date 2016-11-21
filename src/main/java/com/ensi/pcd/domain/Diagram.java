package com.ensi.pcd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Diagram.
 */
@Entity
@Table(name = "diagram")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "diagram")
public class Diagram implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    //@Size(max=20480)
    @Lob @Type(type="org.hibernate.type.TextType")
    @Column(name = "content")
    private String content;

    @Column(name = "validation")
    private Boolean validation;

    @Lob @Type(type="org.hibernate.type.TextType")
    @Column(name = "errors_report")
    private String errors_report;

    @Column(name = "visibility")
    private Boolean visibility;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isValidation() {
        return validation;
    }

    public void setValidation(Boolean validation) {
        this.validation = validation;
    }

    public String geterrors_report() {
        return errors_report;
    }

    public void seterrors_report(String errors_report) {
        this.errors_report = errors_report;
    }


    public Boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Diagram diagram = (Diagram) o;
        if(diagram.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diagram.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Diagram{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", content='" + content + "'" +
            ", validation='" + validation + "'" +
            ", errorsReport='" + errors_report + "'" +
            ", visibility='" + visibility + "'" +
            '}';
    }
}
