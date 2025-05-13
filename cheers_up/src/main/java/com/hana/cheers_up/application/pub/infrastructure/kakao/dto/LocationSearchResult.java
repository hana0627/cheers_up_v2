package com.hana.cheers_up.application.pub.infrastructure.kakao.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
//@Entity
public class LocationSearchResult {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

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


    protected LocationSearchResult direction() {
        return new LocationSearchResult();
    }


//    public Direction(Long id, String inputAddress, Double inputLatitude, Double inputLongitude, String targetPubName, String targetAddress, Double targetLatitude, Double targetLongitude, String targetCategoryName, Double distance) {
    public LocationSearchResult(String inputAddress, Double inputLatitude, Double inputLongitude, String targetPubName, String targetAddress, Double targetLatitude, Double targetLongitude, String targetCategoryName, Double distance) {
//        this.id = id;
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

    public static LocationSearchResult from(DocumentDto addressDto, DocumentDto pubDto) {
        return new LocationSearchResult(
                addressDto.addressName(),
                addressDto.latitude(),
                addressDto.longitude(),
                pubDto.placeName(),
                pubDto.addressName(),
                pubDto.latitude(),
                pubDto.longitude(),
                pubDto.categoryName(),
                pubDto.distance()
        );
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
