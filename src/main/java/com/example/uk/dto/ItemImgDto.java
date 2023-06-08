package com.example.uk.dto;

import com.example.uk.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        /*
         * ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto 로 값을 복사해서 반환
         * static 메소드로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있게 했음
         */
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
