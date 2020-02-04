package io.security.corespringsecurity.domain.entity;

import lombok.*;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PERSISTENT_LOGINS")
@Data
@ToString
@NoArgsConstructor
public class PersistentLogin implements Serializable {


    @Id
    private String series;

    private String username;
    private String token;
    private Date lastUsed;

    public PersistentLogin(PersistentRememberMeToken token){
        this.series = token.getSeries();
        this.username = token.getUsername();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate();
    }

    private static final long serialVersionUID = 8433999509932007961L;


}
