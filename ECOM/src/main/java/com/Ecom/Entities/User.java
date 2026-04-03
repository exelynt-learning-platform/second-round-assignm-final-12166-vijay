package com.Ecom.Entities;

import com.Ecom.Security.ROLE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String name;

    @Column(unique = true, length = 100 ,comment = "email should be unique")
    private String email;

    private String password;

    private  boolean enable=true;

    @Enumerated(EnumType.STRING)
    private ROLE role= ROLE.NORMAL;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private  Set<PaymentMethodInfo> paymentMethodInfos=new LinkedHashSet<>();

}
