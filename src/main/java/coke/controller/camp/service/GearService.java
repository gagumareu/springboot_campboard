package coke.controller.camp.service;

import coke.controller.camp.dto.BoardImageDTO;
import coke.controller.camp.dto.GearDTO;
import coke.controller.camp.dto.GearImageDTO;
import coke.controller.camp.entity.BoardImage;
import coke.controller.camp.entity.Gear;
import coke.controller.camp.entity.GearImage;
import coke.controller.camp.entity.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface GearService {

    Long register(GearDTO gearDTO);
    List<GearDTO> getList(String email);
    void remove(Long gno);
    void modify(GearDTO gearDTO);
    List<GearImageDTO> getImagesList(Long gno);
    GearDTO getByGno(Long gno);


    default Map<String, Object> dtoToEntity(GearDTO gearDTO){

        Map<String, Object> map = new HashMap<>();

        Gear gear = Gear.builder()
                .gno(gearDTO.getGno())
                .gname(gearDTO.getGname())
                .brand(gearDTO.getBrand())
                .size(gearDTO.getSize())
                .material(gearDTO.getMaterial())
                .script(gearDTO.getScript())
                .sort(gearDTO.getSort())
                .member(Member.builder().email(gearDTO.getEmail()).build())
                .build();

        map.put("gear", gear);

        List<GearImageDTO> gearImageDTOList = gearDTO.getGearImageDTOList();

        if (gearImageDTOList != null && gearImageDTOList.size() > 0){
            List<GearImage> gearImageList = gearImageDTOList.stream().map(gearImageDTO -> {
                GearImage gearImage = GearImage.builder()
                        .folderPath(gearImageDTO.getFolderPath())
                        .uuid(gearImageDTO.getUuid())
                        .fileName(gearImageDTO.getFileName())
                        .gear(gear)
                        .build();
                return gearImage;
            }).collect(Collectors.toList());
            map.put("imageList", gearImageList);
        }

        return map;

    }

    default GearDTO entityToDto(Gear gear, Member member, List<GearImage> gearImageList){

        GearDTO gearDTO = GearDTO.builder()
                .gno(gear.getGno())
                .gname(gear.getGname())
                .brand(gear.getBrand())
                .size(gear.getSize())
                .material(gear.getMaterial())
                .script(gear.getScript())
                .sort(gear.getSort())
                .regDate(gear.getRegDate())
                .modDate(gear.getModDate())
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .build();

        if (gearImageList != null && gearImageList.size() > 0){
            List<GearImageDTO> gearDTOList = gearImageList.stream().map(gearImage -> {
                if (gearImage != null ){
                    return GearImageDTO.builder()
                            .folderPath(gearImage.getFolderPath())
                            .uuid(gearImage.getUuid())
                            .fileName(gearImage.getFileName())
                            .build();
                }else {
                    return null;
                }
            }).collect(Collectors.toList());
            gearDTO.setGearImageDTOList(gearDTOList);
        }
        return gearDTO;
    }

    default List<GearImageDTO> entityImagesToDTO(List<GearImage> gearImageList) {

        List<GearImageDTO> gearImageDTOList = new ArrayList<>();

        if (gearImageList != null && gearImageList.size() > 0) {
            gearImageDTOList = gearImageList.stream().map(gearImage -> {
                if (gearImage != null) {
                    return GearImageDTO.builder()
                            .folderPath(gearImage.getFolderPath())
                            .uuid(gearImage.getUuid())
                            .fileName(gearImage.getFileName())
                            .build();
                } else {
                    return null;
                }
            }).collect(Collectors.toList());

        }
        return gearImageDTOList;
    }




}
