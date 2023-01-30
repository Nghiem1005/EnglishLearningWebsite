package com.example.englishlearningwebsite.entities;

import com.example.englishlearningwebsite.entities.enums.AuthProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @Pattern(regexp = ("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
      + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$"), message = "Invalid email")
  @Size(max = 30, min = 10, message = "Invalid mail size")
  private String email;

  @NotNull(message = "Enable is required")
  private boolean enable;

  @Column(name = "name", length = 45)
  @NotNull(message = "Name is required")
  private String name;

  @Column(unique = true)
  @Size(max = 12, min = 9, message = "Invalid phone size")
  private String phone;

  private String password;

  @Column(name = "images", length = 3000)
  private String images;

  @Column(length = 64)
  private String verificationCode;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthProvider provider;

  private String providerId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @OneToOne(cascade={CascadeType.REMOVE})
  @JoinColumn(name = "refresh_token_id")
  private RefreshToken refreshToken;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;
}
