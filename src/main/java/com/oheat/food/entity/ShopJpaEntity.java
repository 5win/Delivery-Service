package com.oheat.food.entity;

import com.oheat.common.BaseTimeEntity;
import com.oheat.common.sido.Sido;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.food.dto.ShopUpdateRequest;
import com.oheat.food.exception.DuplicateMenuException;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
@Entity
@Table(name = "shop")
public class ShopJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "minimum_order_amount", nullable = false)
    private int minimumOrderAmount;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sido_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Sido sido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sigungu_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Sigungu sigungu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "name", nullable = false)
    private CategoryJpaEntity category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private final Set<MenuJpaEntity> menuSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private final List<MenuGroupJpaEntity> menuGroups = new ArrayList<>();

    @Builder
    public ShopJpaEntity(String name, String phone, CategoryJpaEntity category,
        int minimumOrderAmount, int deliveryFee, String address, Double latitude, Double longitude,
        Sido sido, Sigungu sigungu) {
        this.name = name;
        this.phone = phone;
        this.category = category;
        this.minimumOrderAmount = minimumOrderAmount;
        this.deliveryFee = deliveryFee;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sido = sido;
        this.sigungu = sigungu;
    }

    public void updateShopInfo(ShopUpdateRequest updateRequest, CategoryJpaEntity category, Sido sido,
        Sigungu sigungu) {
        this.name = updateRequest.getName();
        this.phone = updateRequest.getPhone();
        this.category = category;
        this.minimumOrderAmount = updateRequest.getMinimumOrderAmount();
        this.deliveryFee = updateRequest.getDeliveryFee();
        this.address = updateRequest.getAddress();
        this.latitude = updateRequest.getLatitude();
        this.longitude = updateRequest.getLongitude();
        this.sido = sido;
        this.sigungu = sigungu;
    }

    public void addMenu(MenuJpaEntity menu) {
        if (isDuplicateMenuName(menu)) {
            throw new DuplicateMenuException();
        }
        menuSet.add(menu);
    }

    private boolean isDuplicateMenuName(MenuJpaEntity menu) {
        Optional<MenuJpaEntity> target = menuSet.stream()
            .filter(menuJpaEntity -> menuJpaEntity.getName().equals(menu.getName()))
            .findFirst();
        return target.isPresent();
    }

    public void addMenuGroup(MenuGroupJpaEntity menuGroup) {
        menuGroups.add(menuGroup);
    }
}
