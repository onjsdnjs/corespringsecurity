package io.security.corespringsecurity.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RESOURCES")
@Data
@ToString(exclude = {"roleSet"})
@EntityListeners(value = { AuditingEntityListener.class })
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resources implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "RESOURCE_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "RESOURCE_NAME", nullable = false)
    private String resourceName;

    @Column(name = "HTTP_METHOD")
    private String httpMethod;

    @Column(name = "ORDERNUM")
    private int ordernum;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ROLE_RESOURCES", joinColumns = {
            @JoinColumn(name = "RESOURCES_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roleSet = new HashSet<>();

}
