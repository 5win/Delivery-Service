package com.oheat.common.sigungu;

import com.oheat.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "ogrFid", callSuper = false)
@Entity
@Table(name = "sigungu")
public class Sigungu extends BaseTimeEntity {

    @Id
    @Column(name = "ogr_fid", nullable = false)
    private Integer ogrFid;

    @Column(name = "geometry", nullable = false)
    private Geometry geometry;

    @Column(name = "sig_cd", nullable = false, unique = true, length = 2)
    private String sigCd;

    @Column(name = "sig_kor_nm", nullable = false, length = 40)
    private String sigKorNm;

    @Column(name = "sig_eng_nm", nullable = false, length = 40)
    private String sigEngNm;

    @Builder
    public Sigungu(Integer ogrFid, Geometry geometry, String sigCd, String sigKorNm, String sigEngNm) {
        this.ogrFid = ogrFid;
        this.geometry = geometry;
        this.sigCd = sigCd;
        this.sigKorNm = sigKorNm;
        this.sigEngNm = sigEngNm;
    }
}
