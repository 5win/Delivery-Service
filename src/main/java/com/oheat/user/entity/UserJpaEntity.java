package com.oheat.user.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.user.constant.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"password", "address", "role"}, callSuper = false)
@Entity
@Table(name = "users")
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address")
    private String address;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<CartJpaEntity> carts = new ArrayList<>();

    @Builder
    public UserJpaEntity(String username, String password, String address, Role role) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public void addToCart(CartJpaEntity cart) {
        cart.changeUser(this);
        this.carts.add(cart);
    }

    public boolean hasOtherShopMenuInCart(ShopJpaEntity shop) {
        if (carts.isEmpty()) {
            return false;
        }
        return !carts.getFirst().getShop().equals(shop);
    }
}
