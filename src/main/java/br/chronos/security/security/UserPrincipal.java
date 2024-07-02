package br.chronos.security.security;

import br.chronos.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    @JsonIgnore
    private Boolean ativo;

    public UserPrincipal(List<Object[]> objs) {
        this.id = Long.valueOf(objs.get(0)[0].toString());
        this.name = objs.get(0)[1].toString();
        this.username = objs.get(0)[2].toString();
        this.email = objs.get(0)[3].toString();
        this.password = objs.get(0)[4].toString();
        this.authorities = objs.stream().map(o -> new SimpleGrantedAuthority(o[5].toString())).toList();
        this.ativo = Boolean.valueOf(objs.get(0)[6].toString());
    }

    public UserPrincipal(Long id, String name, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

        return new UserPrincipal(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Boolean getAtivo() {
        return ativo;
    }

}