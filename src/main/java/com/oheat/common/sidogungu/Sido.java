package com.oheat.common.sidogungu;


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
@EqualsAndHashCode(of = {"ogrFid"}, callSuper = false)
@Entity
@Table(name = "sido")
public class Sido extends BaseTimeEntity {

    @Id
    @Column(name = "ogr_fid", nullable = false)
    private Integer ogrFid;

    @Column(name = "geometry", nullable = false)
    private Geometry geometry;

    @Column(name = "ctprvn_cd", nullable = false, unique = true, length = 2)
    private String ctprvnCd;

    @Column(name = "ctp_kor_nm", nullable = false, length = 40)
    private String ctpKorNm;

    @Column(name = "ctp_eng_nm", nullable = false, length = 40)
    private String ctpEngNm;

    @Builder
    public Sido(Integer ogrFid, Geometry geometry, String ctprvnCd, String ctpKorNm, String ctpEngNm) {
        this.ogrFid = ogrFid;
        this.geometry = geometry;
        this.ctprvnCd = ctprvnCd;
        this.ctpKorNm = ctpKorNm;
        this.ctpEngNm = ctpEngNm;
    }
}
