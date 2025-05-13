package com.hana.cheers_up.application.pub.infrastructure.kakao.dto;

import lombok.Getter;

/**
 * @param inputAddress       주소검색 결과 검색한 주소
 * @param inputLatitude      검색한 주소의 x좌표
 * @param inputLongitude     검색한 주소의 y좌표
 * @param targetPubName      술집데이터 술집 이름
 * @param targetAddress      술집 주소
 * @param targetLatitude     술집의 x좌표
 * @param targetLongitude    술집의 y좌표
 * @param targetCategoryName 술집 세부 카테고리 이름
 * @param distance           두 지점간의 거리
 */
public record LocationSearchResult(
        String inputAddress,
        Double inputLatitude,
        Double inputLongitude,
        String targetPubName,
        String targetAddress,
        Double targetLatitude,
        Double targetLongitude,
        String targetCategoryName,
        Double distance
) {

    //    public Direction(Long id, String inputAddress, Double inputLatitude, Double inputLongitude, String targetPubName, String targetAddress, Double targetLatitude, Double targetLongitude, String targetCategoryName, Double distance) {
    //        this.id = id;

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

