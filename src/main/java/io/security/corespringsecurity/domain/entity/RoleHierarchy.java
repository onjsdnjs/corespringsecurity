package io.security.corespringsecurity.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ROLE_HIERARCHY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"parentName", "roleHierarchy"})
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class RoleHierarchy implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ROLE_HIERARCHY_ID")
    private Long id;

    @Column(name = "CHILD_NAME")
    private String childName;

    @ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_NAME", referencedColumnName = "CHILD_NAME")
    private RoleHierarchy parentName;

    @OneToMany(mappedBy = "parentName", cascade={CascadeType.ALL})
    private Set<RoleHierarchy> roleHierarchy = new HashSet<RoleHierarchy>();
}