package com.uade.tpo.demo.entity;

import java.util.Date;
import java.util.List;

import com.uade.tpo.demo.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Temporal(TemporalType.TIMESTAMP) 
    private Date registrationDate = new Date();

<<<<<<< HEAD:demo/src/main/java/com/uade/tpo/demo/entity/Usuario.java
    @OneToMany(mappedBy = "usuario")
    private List<Cart> carts;
=======
    @OneToMany(mappedBy = "user")
    private List<Carrito> carts;
>>>>>>> d0798b6b797e56dcdc0695cfb93c9e8fa7be55ac:demo/src/main/java/com/uade/tpo/demo/entity/User.java

    @OneToMany(mappedBy = "user")
    private List<Pedido> orders;

}
