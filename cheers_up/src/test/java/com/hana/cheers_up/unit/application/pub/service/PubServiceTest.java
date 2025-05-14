package com.hana.cheers_up.unit.application.pub.service;

import com.hana.cheers_up.application.pub.dto.response.PubResponse;
import com.hana.cheers_up.application.pub.infrastructure.kakao.KakaoSearch;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.DocumentDto;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.KakaoResponse;
import com.hana.cheers_up.application.pub.infrastructure.kakao.dto.MetaDto;
import com.hana.cheers_up.application.pub.service.PubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(MockitoExtension.class)
class PubServiceTest {

    @InjectMocks
    private PubService pubService;

    @Mock
    private KakaoSearch kakaoSearch;

    @Test
    void 주소검색시_최대_3번의_카테고리검색과_함께_성공한다1_isEndTrue() {
        //given
        String address = "서울 동작구 상도로 357";
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;

        // 주소 -> 좌표
        MetaDto coordiateMetaDto = new MetaDto(1, 1L, true);
        List<DocumentDto> coordinateDocumentDtoList = List.of(new DocumentDto(null, address, latitude, longitude, 0.0, null));
        KakaoResponse coordinate = new KakaoResponse(coordiateMetaDto, coordinateDocumentDtoList);

        given(kakaoSearch.getCoordinatesByAddress(address)).willReturn(coordinate);
        // 주소 -> 좌표

        // 좌표 -> 음식점
        MetaDto restaurantsMetaDto1 = new MetaDto(7206, 45L, false);
        MetaDto restaurantsMetaDto2 = new MetaDto(7206, 45L, false);
        MetaDto restaurantsMetaDto3 = new MetaDto(7206, 45L, true);

        List<DocumentDto> restaurantsDocumentList1 = getRestaurantsDocumentList1();
        List<DocumentDto> restaurantsDocumentList2 = getRestaurantsDocumentList2();
        List<DocumentDto> restaurantsDocumentList3 = getRestaurantsDocumentList3();

        KakaoResponse restaurantResponse1 = new KakaoResponse(restaurantsMetaDto1, restaurantsDocumentList1);
        KakaoResponse restaurantResponse2 = new KakaoResponse(restaurantsMetaDto2, restaurantsDocumentList2);
        KakaoResponse restaurantResponse3 = new KakaoResponse(restaurantsMetaDto3, restaurantsDocumentList3);

        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,1)).willReturn(restaurantResponse1);
        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,2)).willReturn(restaurantResponse2);
        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,3)).willReturn(restaurantResponse3);
        // 좌표 -> 음식점

        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).should().getCoordinatesByAddress(address);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,1);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,2);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,3);


        assertThat(result).isNotNull();

        // 카테고리에 술집 키워드 포함
        assertThat(result.get(0).categoryName()).contains("술집");
        assertThat(result.get(1).categoryName()).contains("술집");
        assertThat(result.get(2).categoryName()).contains("술집");
    }


    @Test
    void 주소검색시_최대_3번의_카테고리검색과_함께_성공한다2_isEndFalse() {
        //given
        String address = "서울 동작구 상도로 357";
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;

        // 주소 -> 좌표
        MetaDto coordiateMetaDto = new MetaDto(1, 1L, true);
        List<DocumentDto> coordinateDocumentDtoList = List.of(new DocumentDto(null, address, latitude, longitude, 0.0, null));
        KakaoResponse coordinate = new KakaoResponse(coordiateMetaDto, coordinateDocumentDtoList);

        given(kakaoSearch.getCoordinatesByAddress(address)).willReturn(coordinate);
        // 주소 -> 좌표

        // 좌표 -> 음식점
        MetaDto restaurantsMetaDto1 = new MetaDto(7206, 45L, false);
        MetaDto restaurantsMetaDto2 = new MetaDto(7206, 45L, false);
        MetaDto restaurantsMetaDto3 = new MetaDto(7206, 45L, false);

        List<DocumentDto> restaurantsDocumentList1 = getRestaurantsDocumentList1();
        List<DocumentDto> restaurantsDocumentList2 = getRestaurantsDocumentList2();
        List<DocumentDto> restaurantsDocumentList3 = getRestaurantsDocumentList3();

        KakaoResponse restaurantResponse1 = new KakaoResponse(restaurantsMetaDto1, restaurantsDocumentList1);
        KakaoResponse restaurantResponse2 = new KakaoResponse(restaurantsMetaDto2, restaurantsDocumentList2);
        KakaoResponse restaurantResponse3 = new KakaoResponse(restaurantsMetaDto3, restaurantsDocumentList3);

        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,1)).willReturn(restaurantResponse1);
        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,2)).willReturn(restaurantResponse2);
        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,3)).willReturn(restaurantResponse3);
        // 좌표 -> 음식점

        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).should().getCoordinatesByAddress(address);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,1);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,2);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,3);


        assertThat(result).isNotNull();

        // 카테고리에 술집 키워드 포함
        assertThat(result.get(0).categoryName()).contains("술집");
        assertThat(result.get(1).categoryName()).contains("술집");
        assertThat(result.get(2).categoryName()).contains("술집");
    }


    @Test
    void 주소검색결과의_document가_null일경우_emptyList를_반환한다() {
        //given
        String address = "서울 동작구 상도로 357";
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;

        // 주소 -> 좌표
        MetaDto coordiateMetaDto = new MetaDto(1, 1L, true);
//        List<DocumentDto> coordinateDocumentDtoList = List.of();
        KakaoResponse coordinate = new KakaoResponse(coordiateMetaDto, null);

        given(kakaoSearch.getCoordinatesByAddress(address)).willReturn(coordinate);
        // 주소 -> 좌표

        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).should().getCoordinatesByAddress(address);

        assertThat(result).size().isEqualTo(0);
    }

    @Test
    void 빈_주소값으로_주소검색시_emptyList를_반환한다() {
        //given
        String address = null;

        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).shouldHaveNoInteractions();
        assertThat(result).isEmpty();
    }

    @Test
    void 주소검색시_최대_3번의_카테고리검색과_함께_성공한다_결과가_작은경우() {
        //given
        String address = "서울 동작구 상도로 357";
        double latitude = 37.4969397553084;
        double longitude = 126.953540835787;
        double radius = 3.0;

        // 주소 -> 좌표
        MetaDto coordiateMetaDto = new MetaDto(1, 1L, true);
        List<DocumentDto> coordinateDocumentDtoList = List.of(new DocumentDto(null, address, latitude, longitude, 0.0, null));
        KakaoResponse coordinate = new KakaoResponse(coordiateMetaDto, coordinateDocumentDtoList);

        given(kakaoSearch.getCoordinatesByAddress(address)).willReturn(coordinate);
        // 주소 -> 좌표

        // 좌표 -> 음식점
        MetaDto restaurantsMetaDto1 = new MetaDto(7206, 45L, false);
        MetaDto restaurantsMetaDto2 = new MetaDto(7206, 45L, true);
//        MetaDto restaurantsMetaDto3 = new MetaDto(7206, 45L, true);

        List<DocumentDto> restaurantsDocumentList1 = getRestaurantsDocumentList1();
        List<DocumentDto> restaurantsDocumentList2 = getRestaurantsDocumentList2();

        KakaoResponse restaurantResponse1 = new KakaoResponse(restaurantsMetaDto1, restaurantsDocumentList1);
        KakaoResponse restaurantResponse2 = new KakaoResponse(restaurantsMetaDto2, restaurantsDocumentList2);

        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,1)).willReturn(restaurantResponse1);
        given(kakaoSearch.getRestaurantsByLocation(latitude,longitude,radius,2)).willReturn(restaurantResponse2);
        // 좌표 -> 음식점


        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).should().getCoordinatesByAddress(address);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,1);
        then(kakaoSearch).should().getRestaurantsByLocation(latitude,longitude,radius,2);


        assertThat(result).isNotNull();

        // 카테고리에 술집 키워드 포함
        assertThat(result.get(0).categoryName()).contains("술집");
        assertThat(result.get(1).categoryName()).contains("술집");
        assertThat(result.get(2).categoryName()).contains("술집");
    }


    @Test
    void kakaoResponse가_null인경우_emptyList를_반환한다() {
        //given
        String address = "서울 동작구 상도로 357";

        given(kakaoSearch.getCoordinatesByAddress(address)).willReturn(null);

        //when
        List<PubResponse> result = pubService.recommendPubs(address);

        //then
        then(kakaoSearch).should().getCoordinatesByAddress(address);
        then(kakaoSearch).shouldHaveNoMoreInteractions();
        assertThat(result).isEmpty();
    }



    private static List<DocumentDto> getRestaurantsDocumentList3() {
        return List.of(
                new DocumentDto("자월당", "서울 동작구 상도동 492-13", 37.4958909800973, 126.953303331816, 118.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("김밥이맛있는집", "서울 동작구 상도동 489-7", 37.495873988371, 126.95362676089, 118.0, "음식점 > 분식"),
                new DocumentDto("벙커", "서울 동작구 상도동 489-7", 37.49586317007792, 126.95361093590714, 119.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("파동추야", "서울 동작구 상도동 490-6", 37.4959025828941, 126.953025139347, 123.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("황새골", "서울 동작구 상도동 477-17", 37.4979243653182, 126.952833887642, 125.0, "음식점 > 한식"),
                new DocumentDto("뚜레쥬르 상도래미안점", "서울 동작구 상도동 477-17", 37.4979315787846, 126.952847453484, 125.0, "음식점 > 간식 > 제과,베이커리 > 뚜레쥬르"),
                new DocumentDto("79대포 숭실대점", "서울 동작구 상도동 489-8", 37.4958037271631, 126.953670906816, 126.0, "음식점 > 술집 > 실내포장마차 > 79대포"),
                new DocumentDto("맥주창고YOLO", "서울 동작구 상도동 489-8", 37.4958091549262, 126.953726314276, 126.0, "음식점 > 술집"),
                new DocumentDto("상도동솥뚜껑 숭실대점", "서울 동작구 상도동 492-14", 37.4957801911274, 126.953391605732, 129.0, "음식점 > 한식 > 육류,고기 > 삼겹살"),
                new DocumentDto("브레댄코 숭실대점", "서울 동작구 상도동 514", 37.49581469115424, 126.95405990659631, 133.0, "음식점 > 간식 > 제과,베이커리 > 브레댄코"),
                new DocumentDto("햇살아래테이블", "서울 동작구 상도동 477-16", 37.498003626748, 126.952765985829, 136.0, "음식점 > 치킨"),
                new DocumentDto("교촌치킨 숭실대점", "서울 동작구 상도동 489-9", 37.4957199579422, 126.9537331543, 136.0, "음식점 > 치킨 > 교촌치킨"),
                new DocumentDto("롯데리아 숭실대입구역점", "서울 동작구 상도동 477-15", 37.49792878209975, 126.95261336621236, 136.0, "음식점 > 패스트푸드 > 롯데리아"),
                new DocumentDto("맛닭꼬 숭실대입구역점", "서울 동작구 상도동 477-16", 37.4980108329882, 126.952761457826, 137.0, "음식점 > 치킨 > 맛닭꼬"),
                new DocumentDto("이모이모", "서울 동작구 상도동 489-10", 37.49571273978382, 126.95375566570743, 137.0, "음식점 > 술집 > 호프,요리주점")
        );
    }

    private static List<DocumentDto> getRestaurantsDocumentList2() {
        return List.of(
                new DocumentDto("파리바게뜨 숭실대점", "서울 동작구 상도동 475-9", 37.49648104698364, 126.95307001073994, 65.0, "음식점 > 간식 > 제과,베이커리 > 파리바게뜨"),
                new DocumentDto("60계치킨 서울상도점", "서울 동작구 상도동 486-3", 37.4974577333049, 126.953058090966, 71.0, "음식점 > 치킨 > 60계치킨"),
                new DocumentDto("생활맥주 숭실대입구역점", "서울 동작구 상도동 486", 37.49763435656411, 126.953123570314, 85.0, "음식점 > 술집 > 호프,요리주점 > 생활맥주"),
                new DocumentDto("원미명태촌", "서울 동작구 상도동 490", 37.496244071072, 126.953041888045, 88.0, "음식점 > 한식 > 해물,생선"),
                new DocumentDto("닭갈비생각 숭실대점", "서울 동작구 상도동 489-3", 37.4961621839277, 126.953305424627, 88.0, "음식점 > 한식 > 육류,고기 > 닭요리"),
                new DocumentDto("춘천닭갈비 숭실대점", "서울 동작구 상도동 489-3", 37.49616037967993, 126.95329977155967, 88.0, "음식점 > 한식 > 육류,고기 > 닭요리"),
                new DocumentDto("바다해물칼국수", "서울 동작구 상도동 490", 37.4962215391683, 126.953024939566, 91.0, "음식점 > 한식 > 국수 > 칼국수"),
                new DocumentDto("배스킨라빈스 숭실대점", "서울 동작구 상도동 489-5", 37.4961009726151, 126.953450209916, 93.0, "음식점 > 간식 > 아이스크림 > 배스킨라빈스"),
                new DocumentDto("불타는소금구이 상도동점", "서울 동작구 상도동 490-5", 37.4960324103746, 126.953233131905, 104.0, "음식점 > 한식 > 육류,고기"),
                new DocumentDto("불타는소금구이 상도점", "서울 동작구 상도동 490-5", 37.4960297010798, 126.95321730188735, 105.0, "음식점 > 한식 > 육류,고기"),
                new DocumentDto("쿠시야끼하루 상도점", "서울 동작구 상도동 490-5", 37.4960252005324, 126.953228613056, 105.0, "음식점 > 술집 > 일본식주점"),
                new DocumentDto("광양불고기준서네", "서울 동작구 상도동 474-16", 37.4962340414098, 126.952744483583, 105.0, "음식점 > 한식 > 육류,고기 > 불고기,두루치기"),
                new DocumentDto("누들보쌈배달만족 상도점", "서울 동작구 상도동 490-5", 37.4960251960548, 126.953217304697, 105.0, "음식점 > 한식 > 육류,고기 > 족발,보쌈"),
                new DocumentDto("맘스터치 숭실대입구역점", "서울 동작구 상도동 489-6", 37.4959208144982, 126.953560012694, 113.0, "음식점 > 패스트푸드 > 맘스터치"),
                new DocumentDto("따뜻한밥상 숭실대점", "서울 동작구 상도동 492-13", 37.4958909841198, 126.953313509321, 118.0, "음식점 > 한식")
        );
    }

    private static List<DocumentDto> getRestaurantsDocumentList1() {
        return List.of(
                new DocumentDto("민아정", "서울 동작구 상도동 488", 37.496939845594, 126.953541288071, 0.0, "음식점 > 분식"),
                new DocumentDto("전부치는집 숭실대점", "서울 동작구 상도동 488", 37.4969515617887, 126.953549196764, 1.0, "음식점 > 술집 > 호프,요리주점"),
                new DocumentDto("상도곱창", "서울 동작구 상도동 488", 37.49688127707114, 126.95353340841018, 6.0, "음식점 > 한식 > 육류,고기 > 곱창,막창"),
                new DocumentDto("피자스쿨 숭실대점", "서울 동작구 상도동 488", 37.4968938528668, 126.953436147581, 10.0, "음식점 > 양식 > 피자 > 피자스쿨"),
                new DocumentDto("준호네즉석떡볶이", "서울 동작구 상도동 488", 37.49683172972156, 126.95355379438007, 12.0, "음식점 > 분식 > 떡볶이"),
                new DocumentDto("가춘", "서울 동작구 상도동 488", 37.496818209298, 126.95354023257572, 13.0, "음식점 > 간식 > 제과,베이커리"),
                new DocumentDto("쉐프의목장 상도점", "서울 동작구 상도동 488", 37.49700556739255, 126.95341006864511, 13.0, "음식점 > 한식 > 육류,고기"),
                new DocumentDto("화를품은닭 상도점", "서울 동작구 상도동 487-11", 37.497100330250724, 126.95381033151949, 29.0, "음식점 > 한식 > 육류,고기 > 닭요리"),
                new DocumentDto("야키오진격", "서울 동작구 상도동 485-1", 37.497302971559364, 126.95359308246445, 40.0, "음식점 > 일식"),
                new DocumentDto("깍뚝집", "서울 동작구 상도동 485-1", 37.4973020971875, 126.953660934331, 41.0, "음식점 > 한식 > 육류,고기"),
                new DocumentDto("미학당 2호점", "서울 동작구 상도동 485-1", 37.4973498447507, 126.953646203698, 46.0, "음식점 > 간식 > 제과,베이커리"),
                new DocumentDto("상도국수", "서울 동작구 상도동 486-8", 37.49735872568539, 126.9533182498855, 50.0, "음식점 > 한식 > 국수"),
                new DocumentDto("마루스시", "서울 동작구 상도동 485", 37.4974849361353, 126.953494585199, 60.0, "음식점 > 일식 > 초밥,롤"),
                new DocumentDto("참베이커리", "서울 동작구 상도동 486-5", 37.4974524017111, 126.953245816681, 62.0, "음식점 > 간식 > 제과,베이커리"),
                new DocumentDto("본죽&비빔밥cafe 숭실대역점", "서울 동작구 상도동 486-6", 37.497361317991796, 126.95303892676043, 64.0, "음식점 > 퓨전요리 > 퓨전한식 > 본죽&비빔밥cafe")
        );
    }


}