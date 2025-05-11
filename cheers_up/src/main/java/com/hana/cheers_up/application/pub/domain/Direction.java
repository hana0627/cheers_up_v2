package com.hana.cheers_up.application.pub.domain;

import com.hana.cheers_up.application.api.dto.DocumentDto;
import com.hana.cheers_up.application.pub.dto.PubDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Direction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //주소검색 결과
    private String inputAddress; // 검색한 주소
    private Double inputLatitude; //검색한 주소의 x좌표
    private Double inputLongitude;//검색한 주소의 y좌표

    // 술집데이터
    private String targetPubName; // 술집 이름
    private String targetAddress; // 술집 주소
    private Double targetLatitude; //술집의 x좌표
    private Double targetLongitude; //술집의 y좌표
    private String targetCategoryName; //술집 세부 카테고리 이름

    // 두 지점간의 거리
    private Double distance;


//    public static Direction from(DocumentDto documentDto, PubDto pubDto) {
//        return Direction.builder()
//                .inputAddress(documentDto.addressName())
//                .inputLatitude(documentDto.latitude())
//                .inputLongitude(documentDto.longitude())
//                .targetPubName(pubDto.pubName())
//                .targetAddress(pubDto.pubAddress())
//                .targetLatitude(pubDto.latitude())
//                .targetLongitude(pubDto.longitude())
//                .targetCategoryName(pubDto.categoryName())
//                .distance(
//                        calculateDistance(documentDto.latitude(), documentDto.longitude(),
//                                pubDto.latitude(), pubDto.longitude())
//                )
//                .build();
//    }

    protected Direction direction() {
        return new Direction();
    }


    @Builder
    public Direction(Long id, String inputAddress, Double inputLatitude, Double inputLongitude, String targetPubName, String targetAddress, Double targetLatitude, Double targetLongitude, String targetCategoryName, Double distance) {
        this.id = id;
        this.inputAddress = inputAddress;
        this.inputLatitude = inputLatitude;
        this.inputLongitude = inputLongitude;
        this.targetPubName = targetPubName;
        this.targetAddress = targetAddress;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.targetCategoryName = targetCategoryName;
        this.distance = distance;
    }

    public static Direction from(DocumentDto addressDto, DocumentDto pubDto) {
        return Direction.builder()
                .inputAddress(addressDto.addressName())
                .inputLatitude(addressDto.latitude())
                .inputLongitude(addressDto.longitude())
                .targetPubName(pubDto.placeName())
                .targetAddress(pubDto.addressName())
                .targetLatitude(pubDto.latitude())
                .targetLongitude(pubDto.longitude())
                .targetCategoryName(pubDto.categoryName())
                .distance(pubDto.distance())
                .build();
    }

    // 거리계산 알고리즘
//    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        lat1 = Math.toRadians(lat1); //검색 좌표
//        lon1 = Math.toRadians(lon1); //검색 좌표
//        lat2 = Math.toRadians(lat2); //술집 좌표
//        lon2 = Math.toRadians(lon2); //술집 좌표
//
//        double earthRadius = 6371; //Kilometers
//        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
//    }
}
